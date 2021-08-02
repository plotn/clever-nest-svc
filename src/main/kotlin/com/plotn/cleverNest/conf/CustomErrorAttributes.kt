package com.plotn.cleverNest.conf

import com.plotn.cleverNest.controller.MainController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.error.ErrorAttributeOptions
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

    override fun getErrorAttributes(webRequest: WebRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val bTrace = env.getProperty("app.err_include_stacktrace")?.equals("1")?: false
        val opts = if (bTrace)
            options?.including(
                ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.BINDING_ERRORS,
                ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE)
        else
            options?.including(
                ErrorAttributeOptions.Include.BINDING_ERRORS,
                ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE)
        val errorAttributes = super.getErrorAttributes(webRequest, opts)
        val onow = OffsetDateTime.now()
        val dtf = DateTimeFormatter.ISO_DATE_TIME
        errorAttributes["datetime_iso"] = dtf.format(onow)
        errorAttributes["version"] = MainController.APP_VERSION_NUMBER
//        val s: String? = errorAttributes["trace"] as String?
//        if (s != null) {
//            if (s.indexOf("ERROR:") >=0 )
//                errorAttributes["add_error"] = s.substring(s.indexOf("ERROR:")  )
//        }
        return errorAttributes
    }

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }

}