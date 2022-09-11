package com.plotn.cleverNest.auth

import com.plotn.cleverNest.exceptions.AuthNeededException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CheckAuth {

    fun chechAuth() {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipalName = authentication.name
        if (currentPrincipalName.uppercase(Locale.getDefault()).startsWith("UNAUTH")) throw AuthNeededException("Authroization is required")
    }
}