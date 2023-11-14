package net.wyvernia.numtseng.exceptions

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException : AuthenticationException {
    constructor() : super("Failed authentication")
    constructor(message: String?) : super(message)
}