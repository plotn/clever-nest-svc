package com.plotn.cleverNest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
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
        s = s + "Plotn CleverNest service. Start with <a href='swagger-ui.html'>swagger-ui.html</a> or <a href='monitoring'>monitoring.</a><br>\n"
        s = s + "</body></html>";
        return s
    }

    @RequestMapping("/login")
    @ResponseBody
    fun doLogin(@RequestParam("username") username: String, @RequestParam("password") password: String): String {
        return "Login attempt as $username"
    }

    companion object {
        const val APP_VERSION_NUMBER = "0.0.1"
    }

}