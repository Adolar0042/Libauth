package dev.adolar0042.libauth.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.adolar0042.libauth.data.entity.User

@Dao
interface UserDao {
    @Upsert
    suspend fun insertOrUpdateUser(user: User): Long

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUser(userId: Long): User
}
