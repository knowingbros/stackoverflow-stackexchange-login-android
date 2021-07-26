package com.reputationoverflow.session

import android.content.Intent
import android.net.Uri

object SessionUtil {
    fun fromUri(uri: Uri): AuthResponse? {
        if (uri.fragment.isNullOrBlank()) {
            return null
        }
        val errorRegex = Regex("&?([^=]*)=([^&]*)")
        val fragmentMap = uri.fragment.toString().split("&").associate { fragment ->
            errorRegex.find(fragment)!!.let { matches ->
                matches.groups.let { (propName, propValue) ->
                    propName to propValue
                }
            }
        }
        return AuthResponse(fragmentMap)
    }

    fun getAuthIntent(): Intent {
        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("stackoverflow.com")
            .appendPath("oauth")
            .appendPath("dialog")
            .appendQueryParameter("client_id", "20665")
            .appendQueryParameter("scope", "no_expiry,read_inbox")
            .appendQueryParameter("redirect_uri", "japprep1://stackexchange.com")
        return Intent(Intent.ACTION_VIEW, builder.build())
    }

    class AuthResponse(fragmentMap: Map<String, String>) {
        private var _isAuthorized: Boolean = false
        private var _error: AuthError? = null
        private var _success: AuthSuccess? = null

        fun isAuthorized(): Boolean {
            return _isAuthorized
        }

        fun getError(): AuthError? {
            return _error
        }

        fun getSuccess(): AuthSuccess? {
            return _success
        }

        init {
            _isAuthorized = fragmentMap.containsKey("access_token")
            if (!_isAuthorized) {
                _error = AuthError(
                    description = fragmentMap["error_description"] ?: error("No valid error_description param has been provided")
                )
            } else {
                _success = AuthSuccess(
                    token = fragmentMap["access_token"] ?: error("No valid access_token param has been provided"),
                    expires = fragmentMap["expires"]?.toInt()
                )
            }
        }

        data class AuthError(val description: String)
        data class AuthSuccess(val token: String, val expires: Int?)
    }
}

private operator fun MatchGroupCollection.component2(): String {
    return get(2)!!.value
}

private operator fun MatchGroupCollection.component1(): String {
    return get(1)!!.value
}