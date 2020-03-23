package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.ConfAlias
import com.plotn.cleverNest.model.db.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long> {

    fun findBylId(lId: Long): Location?

    @Query(value = " select t from Location t where t.lLinkConf in (-1, :cId) ",
            nativeQuery = false)
    fun findByConf(cId: Long): List<Location>

}
