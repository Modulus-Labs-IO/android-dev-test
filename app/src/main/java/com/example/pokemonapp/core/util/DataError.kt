package com.example.pokemonapp.core.util

sealed interface DataError: Error {
    enum class Network(val errorMessage: String): DataError {
        REQUEST_TIMEOUT("Error 408: Request Timeout"),
        UNAUTHORIZED("Error 401: Unauthorized"),
        CONFLICT("Error 409: Conflict"),
        NO_INTERNET("No internet connection"),
        MULTIPLE_CHOICES("Error 300: Multiple Choices"), // 300
        MOVED_PERMANENTLY("Error 301: Moved Permanently"), // 301
        FOUND("Error 302: Found"), // 302
        SEE_OTHER("Error 303: See Other"), // 303
        NOT_MODIFIED("Error 304: Not Modified"), // 304
        USE_PROXY("Error 305: Use Proxy"), // 305
        TEMPORARY_REDIRECT("Error 307: Temporary Redirect"), // 307
        PERMANENT_REDIRECT("Error 308: Permanent Redirect"), // 308
        BAD_REQUEST("Error 400: Bad Request"), // 400
        FORBIDDEN("Error 403: Forbidden"), // 403
        NOT_FOUND("Error 404: Not Found"), // 404
        METHOD_NOT_ALLOWED("Error 405: Method Not Allowed"), // 405
        NOT_ACCEPTABLE("Error 406: Not Acceptable"), // 406
        PROXY_AUTHENTICATION_REQUIRED("Error 407: Proxy Authentication Required"), // 407
        GONE("Error 410: Gone"),  // 410
        LENGTH_REQUIRED("Error 411: Length Required"), // 411
        PRECONDITION_FAILED("Error 412: Precondition Failed"), // 412
        PAYLOAD_TOO_LARGE("Error 413: Payload Too Large"), // 413
        URI_TOO_LONG("Error 414: Uri Too Long"), // 414
        UNSUPPORTED_MEDIA_TYPE("Error 415: Unsupported Media Type"), // 415
        RANGE_NOT_SATISFIABLE("Error 416: Range Not Satisfiable"), // 416
        EXPECTATION_FAILED("Error 417: Expectation Failed"), // 417
        IM_A_TEAPOT("Error 418: I'm a teapot"), // 418
        MISDIRECTED_REQUEST("Error 421: Misdirected Request"), // 421
        UNPROCESSABLE_ENTITY("Error 422: Unprocessable Entity"), // 422
        LOCKED("Error 423: Locked"), // 423
        FAILED_DEPENDENCY("Error 424: Failed Dependency"), // 424
        TOO_EARLY("Error 425: Too Early"), // 425
        UPGRADE_REQUIRED("Error 426: Upgrade Required"), // 426
        PRECONDITION_REQUIRED("Error 428: Precondition Required"), // 428
        TOO_MANY_REQUESTS("Error 429: Too Many Requests"), // 429
        REQUEST_HEADER_FIELDS_TOO_LARGE("Error 431: Request Header fields too large"), // 431
        UNAVAILABLE_FOR_LEGAL_REASONS("Error 451: Unavailable for legal reasons"), // 451
        INTERNAL_SERVER_ERROR("Error 500: Internal Server Error"), // 500
        NOT_IMPLEMENTED("Error 501: Not Implemented"), // 501
        BAD_GATEWAY("Error 502: Bad Gateway"), // 502
        SERVICE_UNAVAILABLE("Error 503: Service Unavailable"), // 503
        GATEWAY_TIMEOUT("Error 504: Gateway Timeout"), // 504
        HTTP_VERSION_NOT_SUPPORTED("Error 505: HTTP Version not supported"), // 505
        VARIANT_ALSO_NEGOTIATES("Error 506: Variant also negotiates"), // 506
        INSUFFICIENT_STORAGE("Error 507: Insufficient Storage"), // 507
        LOOP_DETECTED("Error 508: Loop Detected"), // 508
        NOT_EXTENDED("Error 510: Not Extended"), // 510
        NETWORK_AUTHENTICATION_REQUIRED("Error 511: Network Authentication Required"), // 511
        UNKNOWN("An unexpected error occurred, plea try again!"),
        TIMEOUT("Network Timeout")
    }
}

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.DynamicString(DataError.Network.REQUEST_TIMEOUT.errorMessage)
        DataError.Network.UNAUTHORIZED -> UiText.DynamicString(DataError.Network.UNAUTHORIZED.errorMessage)
        DataError.Network.CONFLICT -> UiText.DynamicString(DataError.Network.CONFLICT.errorMessage)
        DataError.Network.NO_INTERNET -> UiText.DynamicString(DataError.Network.NO_INTERNET.errorMessage)
        DataError.Network.MULTIPLE_CHOICES -> UiText.DynamicString(DataError.Network.MULTIPLE_CHOICES.errorMessage)
        DataError.Network.MOVED_PERMANENTLY -> UiText.DynamicString(DataError.Network.MOVED_PERMANENTLY.errorMessage)
        DataError.Network.FOUND -> UiText.DynamicString(DataError.Network.FOUND.errorMessage)
        DataError.Network.SEE_OTHER -> UiText.DynamicString(DataError.Network.SEE_OTHER.errorMessage)
        DataError.Network.NOT_MODIFIED -> UiText.DynamicString(DataError.Network.NOT_MODIFIED.errorMessage)
        DataError.Network.USE_PROXY -> UiText.DynamicString(DataError.Network.USE_PROXY.errorMessage)
        DataError.Network.TEMPORARY_REDIRECT -> UiText.DynamicString(DataError.Network.TEMPORARY_REDIRECT.errorMessage)
        DataError.Network.PERMANENT_REDIRECT -> UiText.DynamicString(DataError.Network.PERMANENT_REDIRECT.errorMessage)
        DataError.Network.BAD_REQUEST -> UiText.DynamicString(DataError.Network.BAD_REQUEST.errorMessage)
        DataError.Network.FORBIDDEN -> UiText.DynamicString(DataError.Network.FORBIDDEN.errorMessage)
        DataError.Network.NOT_FOUND -> UiText.DynamicString(DataError.Network.NOT_FOUND.errorMessage)
        DataError.Network.METHOD_NOT_ALLOWED -> UiText.DynamicString(DataError.Network.METHOD_NOT_ALLOWED.errorMessage)
        DataError.Network.NOT_ACCEPTABLE -> UiText.DynamicString(DataError.Network.NOT_ACCEPTABLE.errorMessage)
        DataError.Network.PROXY_AUTHENTICATION_REQUIRED -> UiText.DynamicString(DataError.Network.PROXY_AUTHENTICATION_REQUIRED.errorMessage)
        DataError.Network.GONE -> UiText.DynamicString(DataError.Network.GONE.errorMessage)
        DataError.Network.LENGTH_REQUIRED -> UiText.DynamicString(DataError.Network.LENGTH_REQUIRED.errorMessage)
        DataError.Network.PRECONDITION_FAILED -> UiText.DynamicString(DataError.Network.PRECONDITION_FAILED.errorMessage)
        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.DynamicString(DataError.Network.PAYLOAD_TOO_LARGE.errorMessage)
        DataError.Network.URI_TOO_LONG -> UiText.DynamicString(DataError.Network.URI_TOO_LONG.errorMessage)
        DataError.Network.UNSUPPORTED_MEDIA_TYPE -> UiText.DynamicString(DataError.Network.UNSUPPORTED_MEDIA_TYPE.errorMessage)
        DataError.Network.RANGE_NOT_SATISFIABLE -> UiText.DynamicString(DataError.Network.RANGE_NOT_SATISFIABLE.errorMessage)
        DataError.Network.EXPECTATION_FAILED -> UiText.DynamicString(DataError.Network.EXPECTATION_FAILED.errorMessage)
        DataError.Network.IM_A_TEAPOT -> UiText.DynamicString(DataError.Network.IM_A_TEAPOT.errorMessage)
        DataError.Network.MISDIRECTED_REQUEST -> UiText.DynamicString(DataError.Network.MISDIRECTED_REQUEST.errorMessage)
        DataError.Network.UNPROCESSABLE_ENTITY -> UiText.DynamicString(DataError.Network.UNPROCESSABLE_ENTITY.errorMessage)
        DataError.Network.LOCKED -> UiText.DynamicString(DataError.Network.LOCKED.errorMessage)
        DataError.Network.FAILED_DEPENDENCY -> UiText.DynamicString(DataError.Network.FAILED_DEPENDENCY.errorMessage)
        DataError.Network.TOO_EARLY -> UiText.DynamicString(DataError.Network.TOO_EARLY.errorMessage)
        DataError.Network.UPGRADE_REQUIRED -> UiText.DynamicString(DataError.Network.UPGRADE_REQUIRED.errorMessage)
        DataError.Network.PRECONDITION_REQUIRED -> UiText.DynamicString(DataError.Network.PRECONDITION_REQUIRED.errorMessage)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.DynamicString(DataError.Network.TOO_MANY_REQUESTS.errorMessage)
        DataError.Network.REQUEST_HEADER_FIELDS_TOO_LARGE -> UiText.DynamicString(DataError.Network.REQUEST_HEADER_FIELDS_TOO_LARGE.errorMessage)
        DataError.Network.UNAVAILABLE_FOR_LEGAL_REASONS -> UiText.DynamicString(DataError.Network.UNAVAILABLE_FOR_LEGAL_REASONS.errorMessage)
        DataError.Network.INTERNAL_SERVER_ERROR -> UiText.DynamicString(DataError.Network.INTERNAL_SERVER_ERROR.errorMessage)
        DataError.Network.NOT_IMPLEMENTED -> UiText.DynamicString(DataError.Network.NOT_IMPLEMENTED.errorMessage)
        DataError.Network.BAD_GATEWAY -> UiText.DynamicString(DataError.Network.BAD_GATEWAY.errorMessage)
        DataError.Network.SERVICE_UNAVAILABLE -> UiText.DynamicString(DataError.Network.SERVICE_UNAVAILABLE.errorMessage)
        DataError.Network.GATEWAY_TIMEOUT -> UiText.DynamicString(DataError.Network.GATEWAY_TIMEOUT.errorMessage)
        DataError.Network.HTTP_VERSION_NOT_SUPPORTED -> UiText.DynamicString(DataError.Network.HTTP_VERSION_NOT_SUPPORTED.errorMessage)
        DataError.Network.VARIANT_ALSO_NEGOTIATES -> UiText.DynamicString(DataError.Network.VARIANT_ALSO_NEGOTIATES.errorMessage)
        DataError.Network.INSUFFICIENT_STORAGE -> UiText.DynamicString(DataError.Network.INSUFFICIENT_STORAGE.errorMessage)
        DataError.Network.LOOP_DETECTED -> UiText.DynamicString(DataError.Network.LOOP_DETECTED.errorMessage)
        DataError.Network.NOT_EXTENDED -> UiText.DynamicString(DataError.Network.NOT_EXTENDED.errorMessage)
        DataError.Network.NETWORK_AUTHENTICATION_REQUIRED -> UiText.DynamicString(DataError.Network.NETWORK_AUTHENTICATION_REQUIRED.errorMessage)
        DataError.Network.TIMEOUT -> UiText.DynamicString(DataError.Network.TIMEOUT.errorMessage)
        else -> UiText.DynamicString(DataError.Network.UNKNOWN.errorMessage)
    }
}