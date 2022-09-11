package com.plotn.cleverNest.controller.ESPDevice

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.controller.DeviceController
import com.plotn.cleverNest.exceptions.AuthNeededException
import com.plotn.cleverNest.exceptions.DeviceCommException
import com.plotn.cleverNest.model.db.User
import com.plotn.cleverNest.repository.DeviceRepository
import com.plotn.cleverNest.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.OffsetDateTime
import java.util.*


@RestController
@RequestMapping("/espdevice")
class ESPEasyTasmotaController {

    enum class ESPEasyCommand {
        ON, OFF, STATUS
    }

    private val logger = LoggerFactory.getLogger(javaClass)
    fun log(s: String) = logger.info(s)

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var deviceController: DeviceController

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {
        val timeout = 3000
        val clientHttpRequestFactory = SimpleClientHttpRequestFactory()
        clientHttpRequestFactory.setConnectTimeout(timeout)
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory
    }

    @GetMapping("/control", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun doControl(@RequestParam("aLinkConf") aLinkConf: Long,
                  @RequestParam("dId") dId: Long,
                  @RequestParam("Command") command: String,
                  @RequestParam("domovenokFormat") domovenokFormat: Boolean,
                  @RequestParam(value = "password", required = false) password: String?): String {
        log("Control is called")
        if (!password.isNullOrBlank()) { // we'll check internal user
            log("Checking for internal user")
            var user: User = userRepository.findByUserId(-1L) ?:
                throw AuthNeededException("Authroization is required - internal user id=-1 not found")
            //if (BCryptPasswordEncoder().encode(password) != user.userPassw)
            if (password != user.userPassw)
                throw AuthNeededException("Authroization is required, passw did not match")
        } else checkAuth.chechAuth()
        val dev = deviceController.getDevByIdAndConf(aLinkConf,dId,password)?:
            throw DeviceCommException("Cannot find device")
        var cmd: String = ""
        when (command.uppercase(Locale.getDefault())) {
            "ON", "1" -> cmd = dev.dCommandOn?: ""
            "OFF", "0" -> cmd = dev.dCommandOff?: ""
            "STATUS" -> cmd = dev.dCommandStatus?: ""
        }
        if (cmd.isNullOrBlank()) throw DeviceCommException("Unknown command for ESPEasy device")
        val restTemplate = RestTemplate(getClientHttpRequestFactory())
        log("device command is: $cmd")
        val resp = restTemplate.getForEntity<String>(cmd, String::class.java)
        var sBody = resp.body
        if (sBody.isNullOrBlank()) throw DeviceCommException("Empty response from device URL")
        log("device response is: $sBody")
        sBody = sBody.replace("\n", " ")
        var idx = sBody.lastIndexOf("}",0)
        log("index of }: $idx")
        if (sBody.lastIndexOf("}",0)>=0)
            sBody = sBody.substring(0,sBody.lastIndexOf("}",0))
        if (sBody.endsWith("Ok")) sBody = sBody.substring(0,sBody.length-2)
        log("device '${dev.dName}' updated response is: $sBody")
        // try to parse json returned
        var state: String = ""
        try {
            val jsonObject = JSONObject(sBody)
            if (jsonObject.has("state")) state = jsonObject["state"].toString()
            if (jsonObject.has("POWER")) state = jsonObject["POWER"].toString()
        } catch (E: Exception) {
            log("cannot parse response JSON")
        }
        when (command.uppercase(Locale.getDefault())) {
            "ON", "1" ->  {
                dev.dLastState = "1"
                dev.dLastStateBy = SecurityContextHolder.getContext()?.authentication?.name?: "system"
                dev.dLastStateWhen = OffsetDateTime.now()
                dev.dLastStatusCheck = dev.dLastStateWhen
                log("update device '${dev.dName}' last state to ON")
                deviceRepository.save(dev)
            }
            "OFF", "0" -> {
                dev.dLastState = "0"
                dev.dLastStateBy = SecurityContextHolder.getContext()?.authentication?.name?: "system"
                dev.dLastStateWhen = OffsetDateTime.now()
                dev.dLastStatusCheck = dev.dLastStateWhen
                log("update device '${dev.dName}' last state to OFF")
                deviceRepository.save(dev)
            }
            "STATUS" -> {
                val lastState = dev.dLastState?: "?"
                var wasSet = false
                if (((state == "1")||(state == "ON")) && (lastState != "1")) {
                    dev.dLastState = "1"
                    dev.dLastStateBy = SecurityContextHolder.getContext()?.authentication?.name?: "system"
                    dev.dLastStateWhen = OffsetDateTime.now()
                    dev.dLastStatusCheck = dev.dLastStateWhen
                    log("update device '${dev.dName}' last state to ON")
                    wasSet = true
                    deviceRepository.save(dev)
                }
                if (((state == "0")||(state == "OFF")) && (lastState != "0")) {
                    dev.dLastState = "1"
                    dev.dLastStateBy = SecurityContextHolder.getContext()?.authentication?.name?: "system"
                    dev.dLastStateWhen = OffsetDateTime.now()
                    dev.dLastStatusCheck = dev.dLastStateWhen
                    log("update device '${dev.dName}' last state to OFF")
                    wasSet = true
                    deviceRepository.save(dev)
                }
                if (!wasSet) {
                    log("update device '${dev.dName}' last status timestamp")
                    dev.dLastStatusCheck = OffsetDateTime.now()
                    deviceRepository.save(dev)
                }
            }
        }
        if (domovenokFormat) {
            var sstate = "false"
            if ((state == "1")||(state == "ON")) sstate = "true"
            return "{\"value\": $sstate}"
        }
        return sBody
    }

}