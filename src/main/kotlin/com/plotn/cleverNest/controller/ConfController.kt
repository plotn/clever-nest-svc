package com.plotn.cleverNest.controller

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.exceptions.AuthNeededException
import com.plotn.cleverNest.model.db.Conf
import com.plotn.cleverNest.repository.ConfRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/confs")
class ConfController {

    @Autowired
    lateinit var confRepository: ConfRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    @GetMapping("/raw", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getConfRaw(): List<Conf> {
        checkAuth.chechAuth()
        return confRepository.findAll()
    }

    @GetMapping("/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getConf(): List<Conf> {
        checkAuth.chechAuth()
        return confRepository.findAllFiltered()
    }
}