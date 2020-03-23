package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.Conf
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConfRepository : JpaRepository<Conf, Long> {
    fun findBycId(cId: Long): Conf?
    fun findBycName(cName: String): Conf?

    @Query(value = "SELECT t FROM Conf t WHERE t.cId != -1 order by t.cId",
            nativeQuery = false)
    fun findAllFiltered(): List<Conf>

}
