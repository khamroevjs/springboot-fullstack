package com.khamroev.springbootfullstack.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.CONFLICT)
class DuplicateResourceException(message: String) : RuntimeException(message)
