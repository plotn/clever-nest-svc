package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.ConfAlias
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConfAliasRepository : JpaRepository<ConfAlias, Long> {
    fun findByaId(aId: Long): ConfAlias

    @Query(value = "with anames AS (select A_NAME from CNEST.CN_CONF_ALIAS where A_LINK_CONF = 1) " +
            "SELECT A.* FROM CNEST.CN_CONF_ALIAS A WHERE A.A_LINK_CONF = :cId " +
            "UNION ALL " +
            "SELECT A.* FROM CNEST.CN_CONF_ALIAS A WHERE A.A_LINK_CONF = :cId " +
            "  AND A.A_NAME NOT IN (SELECT A_NAME FROM anames) ",
            nativeQuery = true)
    fun findByConf(cId: Long): List<ConfAlias>

}
