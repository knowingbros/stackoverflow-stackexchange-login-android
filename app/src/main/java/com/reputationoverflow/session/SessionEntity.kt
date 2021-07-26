package com.reputationoverflow.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_db")
data class SessionEntity constructor(
    val token: String,
    val expires: Int? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)