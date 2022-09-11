package com.plotn.cleverNest.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(url: String, authManager: AuthenticationManager) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {

    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        val username = request.getParameter("username")
        val password = request.getParameter("password")

        System.out.printf("JWTLoginFilter.attemptAuthentication: username/password = %s,%s", username, password)
        println()

        return authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(username, password, emptyList()))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain?,
                                          authResult: Authentication) {

        println("JWTLoginFilter.successfulAuthentication:")
        TokenAuthenticationService.addAuthentication(response, authResult.name)

        val authorizationString = response.getHeader("Authorization")

        println("Authorization String=$authorizationString")
        if (request.requestURI != null)
            if (request.requestURI.lowercase(Locale.getDefault()) == "/login") {
                response.status = HttpServletResponse.SC_OK
                response.writer.write("{\"token\": \"" +
                        authorizationString + "\", " +
                        "\"expiration\":" + TokenAuthenticationService.EXPIRATIONTIME +
                        "}")
                response.writer.flush()
                response.writer.close()
            }
    }

}