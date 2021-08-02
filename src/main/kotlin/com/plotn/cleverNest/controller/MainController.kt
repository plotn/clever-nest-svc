package com.plotn.cleverNest.controller

import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @RequestMapping(value = ["/"], produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun welcome(): String {
        var s = "<html><body><h1>Plotn CleverNest service, version: $APP_VERSION_NUMBER</h1><hr>"
        s += "Plotn CleverNest service. Start with <a href='swagger-ui/'>/swagger-ui/</a> or <a href='monitoring'>monitoring.</a><br>\n"
        s += "<a href='passwhash?passw=123'>password encryption<a><br>"
        s += "</body></html>";
        return s
    }

    @RequestMapping("/login")
    @ResponseBody
    fun doLogin(@RequestParam("username") username: String, @RequestParam("password") password: String): String {
        return "Login attempt as $username"
    }

    @RequestMapping("/passwhash")
    @ResponseBody
    fun doLogin(@RequestParam("passw") passw: String): String {
        return "Password hash of '$passw' is " + BCryptPasswordEncoder().encode(passw)
    }

    companion object {
        const val APP_VERSION_NUMBER = "0.0.1"
    }

}