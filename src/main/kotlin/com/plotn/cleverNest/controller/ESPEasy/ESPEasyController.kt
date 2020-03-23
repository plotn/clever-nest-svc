package com.plotn.cleverNest.controller.ESPEasy

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


@RestController
@RequestMapping("/espeasy")
class ESPEasyController {

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
        when (command.toUpperCase()) {
            "ON", "1" -> cmd = dev.dCommandOn?: ""
            "OFF", "0" -> cmd = dev.dCommandOff?: ""
            "STATUS" -> cmd = dev.dCommandStatus?: ""
        }
        if (cmd.isNullOrBlank()) throw DeviceCommException("Unknown command for ESPEasy device")
        val restTemplate = RestTemplate(getClientHttpRequestFactory())
        log("device command is: $cmd")
        val resp = restTemplate.getForEntity<String>(cmd, String::class.java)
        val sBody = resp.body
        if (sBody.isNullOrBlank()) throw DeviceCommException("Empty response from device URL")
        // try to parse json returned
        var state: String = ""
        try {
            val jsonObject = JSONObject(sBody)
            if (jsonObject.has("state")) state = jsonObject["state"].toString()
        } catch (E: Exception) {
            log("cannot parse rsponse JSON")
        }
        when (command.toUpperCase()) {
            "ON", "1" ->  {
                dev.dLastState = "1"
                dev.dLastStateBy = SecurityContextHolder.getContext().authentication.name
                dev.dLastStateWhen = OffsetDateTime.now()
                deviceRepository.save(dev)
            }
            "OFF", "0" -> {
                dev.dLastState = "0"
                dev.dLastStateBy = SecurityContextHolder.getContext().authentication.name
                dev.dLastStateWhen = OffsetDateTime.now()
                deviceRepository.save(dev)
            }
        }
        if (domovenokFormat) {
            var sstate = "false"
            if (state == "1") sstate = "true"
            return "{\"value\": $sstate}"
        }
        return sBody
    }

}