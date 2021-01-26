package com.grewon.dronedin.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Jeff Klima on 2019-08-30.
 */
class ValidationUtils {
    companion object {
        fun isEmptyFiled(value: String): Boolean {
            return value.isEmpty()
        }

        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordMatch(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }

        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[~`@#$%!^&*()-+€]).{6,20})"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

    }
}