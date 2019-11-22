package com.plotn.cleverNest.auth

import com.plotn.cleverNest.exceptions.JwtErrorException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter : GenericFilterBean() {

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {

        println("JWTAuthenticationFilter.doFilter")
        var authentication: Authentication? = null
        try {
            authentication = TokenAuthenticationService.getAuthentication(servletRequest as HttpServletRequest)
        } catch (e: JwtErrorException) {
            (servletResponse as HttpServletResponse).status = HttpServletResponse.SC_FORBIDDEN
            servletResponse.addHeader("auth-error", e.message)
            servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
        }

        if (authentication != null)
            if (authentication.principal != null)
                println("principal is " + authentication.principal.toString())

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(servletRequest, servletResponse)
    }

}
