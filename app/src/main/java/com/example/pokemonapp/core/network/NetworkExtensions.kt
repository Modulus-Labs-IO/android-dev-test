package com.example.pokemonapp.core.network

import com.example.pokemonapp.core.util.DataError
import retrofit2.Response
import java.net.SocketTimeoutException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

inline fun <reified T> safeCall(execute: () -> Response<T>): com.example.pokemonapp.core.util.Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        e.printStackTrace()
        return com.example.pokemonapp.core.util.Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return com.example.pokemonapp.core.util.Result.Error(DataError.Network.TIMEOUT)
    } catch(e: Exception) {
        if(e is CancellationException) throw e
        e.printStackTrace()
        return com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

inline fun <reified T> responseToResult(response: Response<T>): com.example.pokemonapp.core.util.Result<T, DataError.Network> {
    return when (response.code()) {
        in 200..299 -> com.example.pokemonapp.core.util.Result.Success(response.body()!!)
        300 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.MULTIPLE_CHOICES, "Error 300: Multiple Choices")
        301 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.MOVED_PERMANENTLY, "Error 301: Moved Permanently")
        302 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.FOUND, "Error 302: Found")
        303 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.SEE_OTHER, "Error 303: See Other")
        304 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NOT_MODIFIED, "Error 304: Not Modified")
        305 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.USE_PROXY, "Error 305: Use Proxy")
        307 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.TEMPORARY_REDIRECT, "Error 307: Temporary Redirect")
        308 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.PERMANENT_REDIRECT, "Error 308: Permanent Redirect")
        400 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.BAD_REQUEST, "Error 400: Bad Request")
        401 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNAUTHORIZED, "Error 401: Unauthorized")
        403 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.FORBIDDEN, "Error 403: Forbidden")
        404 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NOT_FOUND, "Error 404: Not Found")
        405 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.METHOD_NOT_ALLOWED, "Error 405: Method Not Allowed")
        406 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NOT_ACCEPTABLE, "Error 406: Not Acceptable")
        407 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.PROXY_AUTHENTICATION_REQUIRED, "Error 407: Proxy Authentication Required")
        408 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.REQUEST_TIMEOUT, "Error 408: Request Timeout")
        409 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.CONFLICT, "Error 409: Conflict")
        410 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.GONE, "Error 410: Gone")
        411 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.LENGTH_REQUIRED, "Error 411: Length Required")
        412 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.PRECONDITION_FAILED, "Error 412: Precondition Failed")
        413 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.PAYLOAD_TOO_LARGE, "Error 413: Payload Too Large")
        414 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.URI_TOO_LONG, "Error 414: Uri Too Long")
        415 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNSUPPORTED_MEDIA_TYPE, "Error 415: Unsupported Media Type")
        416 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.RANGE_NOT_SATISFIABLE, "Error 416: Range Not Satisfiable")
        417 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.EXPECTATION_FAILED, "Error 417: Expectation Failed")
        418 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.IM_A_TEAPOT, "Error 418: I'm a teapot")
        421 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.MISDIRECTED_REQUEST, "Error 421: Misdirected Request")
        422 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNPROCESSABLE_ENTITY, "Error 422: Unprocessable Entity")
        423 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.LOCKED, "Error 423: Locked")
        424 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.FAILED_DEPENDENCY, "Error 424: Failed Dependency")
        425 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.TOO_EARLY, "Error 425: Too Early")
        426 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UPGRADE_REQUIRED, "Error 426: Upgrade Required")
        428 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.PRECONDITION_REQUIRED, "Error 428: Precondition Required")
        429 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.TOO_MANY_REQUESTS, "Error 429: Too Many Requests")
        431 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.REQUEST_HEADER_FIELDS_TOO_LARGE, "Error 431: Request Header fields too large")
        451 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNAVAILABLE_FOR_LEGAL_REASONS, "Error 451: Unavailable for legal reasons")
        500 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.INTERNAL_SERVER_ERROR, "Error 500: Internal Server Error")
        501 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NOT_IMPLEMENTED, "Error 501: Not Implemented")
        502 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.BAD_GATEWAY, "Error 502: Bad Gateway")
        503 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.SERVICE_UNAVAILABLE, "Error 503: Service Unavailable")
        504 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.GATEWAY_TIMEOUT, "Error 504: Gateway Timeout")
        505 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.HTTP_VERSION_NOT_SUPPORTED, "Error 505: HTTP Version not supported")
        506 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.VARIANT_ALSO_NEGOTIATES, "Error 506: Variant also negotiates")
        507 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.INSUFFICIENT_STORAGE, "Error 507: Insufficient Storage")
        508 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.LOOP_DETECTED, "Error 508: Loop Detected")
        510 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NOT_EXTENDED, "Error 510: Not Extended")
        511 -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.NETWORK_AUTHENTICATION_REQUIRED, "Error 511: Network Authentication Required")
        else -> com.example.pokemonapp.core.util.Result.Error(DataError.Network.UNKNOWN, "An unexpected error occurred, please try again!")
    }
}