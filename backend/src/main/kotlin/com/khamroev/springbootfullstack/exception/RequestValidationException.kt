package com.khamroev.springbootfullstack.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class RequestValidationException(message: String) : RuntimeException(message)
