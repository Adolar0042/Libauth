package dev.adolar0042.libauth.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val userId: Long,
    val username: String
)
