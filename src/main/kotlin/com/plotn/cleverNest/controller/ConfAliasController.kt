package com.plotn.cleverNest.controller

import com.plotn.cleverNest.model.db.ConfAlias
import com.plotn.cleverNest.repository.ConfRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/confalias")
class ConfAliasController {

    @Autowired
    lateinit var confAliasRepository: ConfRepository

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
