package com.reputationoverflow.logger

import android.util.Log

private const val TAG = "ReputationOverflow"

object Logger {
    fun log(msg: String) {
        Log.v(TAG, msg)
    }
}