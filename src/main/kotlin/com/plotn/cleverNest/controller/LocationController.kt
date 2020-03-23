package com.plotn.cleverNest.controller

import com.plotn.cleverNest.auth.CheckAuth
import com.plotn.cleverNest.model.db.Location
import com.plotn.cleverNest.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/locations")
class LocationController {

    @Autowired
    lateinit var locationRepository: LocationRepository

    @Autowired
    lateinit var checkAuth: CheckAuth

    @GetMapping("/id", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLocById(@RequestParam("lId") lId: Long): Location? {
        checkAuth.chechAuth()
        return locationRepository.findBylId(lId)
    }

    @GetMapping("/byconf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLocByConf(@RequestParam("lLinkConf") lLinkConf: Long): List<Location> {
        checkAuth.chechAuth()
        return locationRepository.findByConf(lLinkConf)
    }
}