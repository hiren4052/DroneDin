package com.grewon.dronedin.error


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.enum_class.ErrorType
import com.grewon.dronedin.utils.ValidationUtils
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

        fun handleMapError(errorBeanString: String) {
            val yourHashMap = Gson().fromJson(errorBeanString, HashMap::class.java) as HashMap<*, *>
            if (yourHashMap != null) {
                val keys: MutableSet<out Any> = yourHashMap.keys
                for (key in keys) {
                    if (yourHashMap[key] != null && !ValidationUtils.isEmptyFiled(yourHashMap[key].toString())) {
                        DroneDinApp.getAppInstance().showToast(yourHashMap[key].toString())
                        return
                    }
                }
            }
        }


    }
}

