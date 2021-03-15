package com.grewon.dronedin.error


import com.google.gson.JsonSyntaxException
import com.grewon.dronedin.error.enum_class.ErrorType
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandler {

    companion object {

        fun handleError(e: Throwable): Int {

            when (e) {
                is IllegalStateException -> {
                    return ErrorType.ERROR_INVALID_RESPONSE.errorMessage
                }
                is SocketTimeoutException -> {
                    return ErrorType.ERROR_REQUEST_TIME_OUT.errorMessage
                }
                is UnknownHostException -> {
                    return ErrorType.ERROR_CONNECTIVITY.errorMessage
                }
                is HttpException -> {
                    return ErrorType.ERROR_INVALID_RESPONSE.errorMessage
                }
                is JsonSyntaxException -> {
                    return ErrorType.ERROR_INVALID_RESPONSE.errorMessage
                }
                else -> {
                    return ErrorType.PARSING_ERROR.errorMessage
                }
            }

        }


    }
}

