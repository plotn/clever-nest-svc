package com.plotn.cleverNest.controller

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.model.db.UsefulLink
import com.plotn.cleverNest.repository.UsefulLinkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usefullinks")
class UsefulLinkController {

    @Autowired
    lateinit var usefulLinksRepository: UsefulLinkRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    @GetMapping("/id", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLocById(@RequestParam("ulId") ulId: Long): UsefulLink? {
        checkAuth.chechAuth()
        return usefulLinksRepository.findByUlId(ulId)
    }

    @GetMapping("/byconf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLocByConf(@RequestParam("ulLinkConf") ulLinkConf: Long): List<UsefulLink> {
        checkAuth.chechAuth()
        return usefulLinksRepository.findByConf(ulLinkConf)
    }
}