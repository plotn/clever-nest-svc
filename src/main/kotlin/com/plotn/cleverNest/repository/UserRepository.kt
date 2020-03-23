package com.plotn.cleverNest.repository

import com.plotn.cleverNest.model.db.Device
import com.plotn.cleverNest.model.db.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUserId(userId: Long): User?

    fun findByUserLogin(userLogin: String): List<User>

}
