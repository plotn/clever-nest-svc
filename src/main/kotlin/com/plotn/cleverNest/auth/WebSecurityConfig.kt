package com.plotn.cleverNest.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.ArrayList
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var authEntryPoint: AuthenticationEntryPoint

    @Autowired
    internal lateinit var dataSource: DataSource

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.cors().and().csrf().disable().authorizeRequests().antMatchers("/").
            permitAll().antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/login").permitAll() // For Test on Browser
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        httpSecurity.sessionManagement().maximumSessions(1)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val o = ArrayList<String>()
        o.add("*")
        configuration.allowedOrigins = o
        val m = ArrayList<String>()
        m.add("GET")
        m.add("POST")
        m.add("PUT")
        m.add("DELETE")
        m.add("OPTIONS")
        configuration.allowedMethods = m
        configuration.allowCredentials = true
        val h = ArrayList<String>()
        h.add("Authorization")
        h.add("Cache-Control")
        h.add("Content-Type")
        h.add("platform")
        h.add("auth-error")
        configuration.allowedHeaders = h
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsManager(): JdbcUserDetailsManager {
        return JdbcUserDetailsManager(dataSource)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication().dataSource(dataSource)
            .passwordEncoder(passwordEncoder())
            .usersByUsernameQuery(
                "select user_login, user_passw, case when USER_STATUS=0 then true else false end as enabled from cnest.cnest_users where user_login=?")
            .authoritiesByUsernameQuery("select user_login, user_role as role from cnest.cnest_users where user_login=?")
        //val mngConfig = auth.jdbcAuthentication()
    }

}