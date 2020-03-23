package com.plotn.cleverNest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class DeviceCommException : RuntimeException {
    constructor(errStr: String) : super(errStr) {}
    constructor(errStr: String, cause: Throwable) : super(errStr, cause) {}
}
