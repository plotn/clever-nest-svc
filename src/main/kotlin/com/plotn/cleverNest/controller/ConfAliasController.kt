package com.plotn.cleverNest.controller

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.model.db.ConfAlias
import com.plotn.cleverNest.repository.ConfAliasRepository
import com.plotn.cleverNest.repository.ConfRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/confalias")
class ConfAliasController {

    @Autowired
    lateinit var confAliasRepository: ConfAliasRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    @GetMapping("/byconf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getByConf(@RequestParam("cId") cId: Long): List<ConfAlias> {
        checkAuth.chechAuth()
        return confAliasRepository.findByConf(cId)
    }

    @GetMapping("/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): List<ConfAlias> {
        checkAuth.chechAuth()
        return confAliasRepository.findAll()
    }
}
