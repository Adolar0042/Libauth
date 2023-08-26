package dev.adolar0042.libauth.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.adolar0042.libauth.data.dao.UserDao
import dev.adolar0042.libauth.data.entity.User

@Database(
    version = 1,
    entities = [User::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}
