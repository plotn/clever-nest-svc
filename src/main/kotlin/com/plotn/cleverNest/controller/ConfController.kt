package com.plotn.cleverNest.controller

import com.plotn.cleverNest.model.db.Conf
import com.plotn.cleverNest.repository.ConfRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/confs")
class ConfController {

    @Autowired
    lateinit var confRepository: ConfRepository

    /**
     * Returns all entries
     * @return Conf list
     */
    @GetMapping("/raw", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getConfRaw(): List<Conf> {
        return confRepository.findAll()
    }

    @GetMapping("/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getConf(): List<Conf> {
        return confRepository.findAllFiltered()
    }
}