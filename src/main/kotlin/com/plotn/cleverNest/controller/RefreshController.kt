package com.plotn.cleverNest.controller

import com.plotn.cleverNest.controller.ESPDevice.ESPEasyTasmotaController
import com.plotn.cleverNest.model.db.Device
import com.plotn.cleverNest.model.db.User
import com.plotn.cleverNest.repository.ConfAliasRepository
import com.plotn.cleverNest.repository.ConfRepository
import com.plotn.cleverNest.repository.DeviceRepository
import com.plotn.cleverNest.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

@Controller
class RefreshController {

    private val logger = LoggerFactory.getLogger(javaClass)
    fun log(s: String) = logger.info(s)

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var confRepository: ConfRepository

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    @Autowired
    lateinit var confAliasRepository: ConfAliasRepository

    @Autowired
    lateinit var espEasyTasmotaController: ESPEasyTasmotaController

    val mapRefresh = HashMap<Long, Int>()

    var passw = ""

    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    fun scheduleFixedDelayTask() {
        log("refresh controller - " + System.currentTimeMillis() / 1000)
        val devices = ArrayList<Device>()
        for (conf in confRepository.findAll()) {
            log("conf = ${conf.cName}")
            if (conf.cDefault == 1) {
                log("conf = ${conf.cName}, default")
                var confAliasList = confAliasRepository.findByConf(conf.cId)
                for (dev in deviceRepository.findAll()) {
                    log("dev = ${dev.dName}")
                    if (dev.dEnabled == 1) {
                        log("dev = ${dev.dName}, enabled")
                        for (confAlias in confAliasList) {
                            dev.dCommandOn = dev.dCommandOn?.replace("[${confAlias.aName}]", confAlias.aValue)
                            dev.dCommandOff = dev.dCommandOff?.replace("[${confAlias.aName}]", confAlias.aValue)
                            dev.dCommandStatus = dev.dCommandStatus?.replace("[${confAlias.aName}]", confAlias.aValue)
                        }
                        devices.add(dev)
                    }
                }
                for (dev in devices) {
                    log("Device '${dev.dName}' dCheckStatusInterval = ${dev.dCheckStatusInterval}")
                    if (dev.dCheckStatusInterval > 0) {
                        var refreshTime: Int = mapRefresh[dev.dId] ?: (dev.dCheckStatusInterval * 1000)
                        log("Device '${dev.dName}' refreshTime = $refreshTime")
                        refreshTime -= 5000
                        if (refreshTime <= 0) {
                            log("Device '${dev.dName}' need refresh")
                            if (passw.isNullOrEmpty()) {
                                val user: User? = userRepository.findByUserId(-1L)
                                user?.let {
                                    passw = user.userPassw
                                }
                            }
                            if (!passw.isNullOrEmpty()) {
                                log("Device '${dev.dName}' do refresh")
                                espEasyTasmotaController.doControl(conf.cId, dev.dId, "STATUS", false, passw)
                            }
                            mapRefresh[dev.dId] = dev.dCheckStatusInterval * 1000
                        } else {
                            mapRefresh[dev.dId] = refreshTime
                        }
                    }
                }
            }
        }
    }
}
