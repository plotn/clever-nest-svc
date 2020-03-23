package com.plotn.cleverNest.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
class AuthNeededException(errStr: String?) : RuntimeException(errStr)