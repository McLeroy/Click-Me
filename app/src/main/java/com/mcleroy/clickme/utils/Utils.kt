package com.mcleroy.clickme.utils

import android.util.Patterns
import java.util.regex.Pattern

object Utils {

    @JvmStatic
    fun isEmailAddressValid(email: CharSequence): Boolean {
        return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email);
    }
}