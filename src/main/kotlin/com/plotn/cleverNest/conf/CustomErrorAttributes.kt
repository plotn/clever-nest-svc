package com.plotn.cleverNest.conf

import com.plotn.cleverNest.controller.MainController
import org.springframework.beans.factory.annotation.Autowired
import java.time.format.DateTimeFormatter
import java.time.OffsetDateTime
import org.springframework.web.context.request.WebRequest
import java.text.SimpleDateFormat
import org.springframework.stereotype.Component
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.env.Environment

@Component
class CustomErrorAttributes : DefaultErrorAttributes() {

    @Autowired
    lateinit var env: Environment

    override fun getErrorAttributes(webRequest: WebRequest, includeStackTrace: Boolean): Map<String, Any> {

        val bTrace = env.getProperty("app.err_include_stacktrace").equals("1")
        //   bTrace = false;
        // Let Spring handle the error first, we will modify later :)
        val errorAttributes = super.getErrorAttributes(webRequest, bTrace)
        // insert a new keys
        val onow = OffsetDateTime.now()
        val dtf = DateTimeFormatter.ISO_DATE_TIME
        errorAttributes.put("datetime_iso", dtf.format(onow))
        errorAttributes.put("version", MainController.APP_VERSION_NUMBER)
        return errorAttributes
    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }

}