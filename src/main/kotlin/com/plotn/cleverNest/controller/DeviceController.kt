package com.plotn.cleverNest.controller

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.exceptions.AuthNeededException
import com.plotn.cleverNest.model.db.Device
import com.plotn.cleverNest.model.db.User
import com.plotn.cleverNest.repository.ConfAliasRepository
import com.plotn.cleverNest.repository.DeviceRepository
import com.plotn.cleverNest.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/devices")
class DeviceController {

    private val logger = LoggerFactory.getLogger(javaClass)
    fun log(s: String) = logger.info(s)

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    @Autowired
    lateinit var confAliasRepository: ConfAliasRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    @GetMapping("/id", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getDevById(@RequestParam("dId") dId: Long): Device? {
        checkAuth.chechAuth()
        return deviceRepository.findBydId(dId)
    }

    @GetMapping("/idandconf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getDevByIdAndConf(@RequestParam("aLinkConf") aLinkConf: Long,
                     @RequestParam("dId") dId: Long,
                     @RequestParam(value = "password", required = false) password: String?): Device? {
        if (!password.isNullOrBlank()) { // we'll check internal user
            log("Checking for internal user")
            var user: User = userRepository.findByUserId(-1L) ?:
            throw AuthNeededException("Authroization is required - internal user id=-1 not found")
            //if (BCryptPasswordEncoder().encode(password) != user.userPassw)
            if (password != user.userPassw)
                throw AuthNeededException("Authroization is required, passw did not match")
        } else checkAuth.chechAuth()
        var confAliasList = confAliasRepository.findByConf(aLinkConf)
        var dev:Device? = deviceRepository.findBydId(dId)
        for (confAlias in confAliasList) {
            dev?.dCommandOn = dev?.dCommandOn?.replace("[${confAlias.aName}]", confAlias.aValue)
            dev?.dCommandOff = dev?.dCommandOff?.replace("[${confAlias.aName}]",confAlias.aValue)
            dev?.dCommandStatus = dev?.dCommandStatus?.replace("[${confAlias.aName}]",confAlias.aValue)
        }
        return dev
    }

    @GetMapping("/byloc", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getDevByLoc(@RequestParam("aLinkConf") aLinkConf: Long,
                     @RequestParam("dLinkLoc") dLinkLoc: Long): List<Device> {
        checkAuth.chechAuth()
        var confAliasList = confAliasRepository.findByConf(aLinkConf)
        val devices = ArrayList<Device>()
        for (dev in deviceRepository.findBydLinkLoc(dLinkLoc)) {
            if (dev.dEnabled == 1) {
                for (confAlias in confAliasList) {
                    dev.dCommandOn = dev.dCommandOn?.replace("[${confAlias.aName}]", confAlias.aValue)
                    dev.dCommandOff = dev.dCommandOff?.replace("[${confAlias.aName}]", confAlias.aValue)
                    dev.dCommandStatus = dev.dCommandStatus?.replace("[${confAlias.aName}]", confAlias.aValue)
                }
                devices.add(dev)
            }
        }
        return devices
    }
}