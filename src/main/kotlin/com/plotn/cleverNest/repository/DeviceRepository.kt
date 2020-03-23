package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {

    fun findBydId(dId: Long): Device?
    fun findBydLinkLoc(dLinkLoc: Long): List<Device>

}
