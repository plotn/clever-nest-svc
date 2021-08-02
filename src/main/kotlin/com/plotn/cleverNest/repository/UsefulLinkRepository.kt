package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.UsefulLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UsefulLinkRepository : JpaRepository<UsefulLink, Long> {

    fun findByUlId(ulId: Long): UsefulLink?

    @Query(value = " select t from UsefulLink t where t.ulLinkConf in (-1, :cId) ",
            nativeQuery = false)
    fun findByConf(cId: Long): List<UsefulLink>

}
