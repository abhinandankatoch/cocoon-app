package com.abhinandan.cocoon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SessionEntity::class, PresetEntity::class], version = 2, exportSchema = false)
abstract class CocoonDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun presetDao(): PresetDao

    companion object {
        @Volatile private var instance: CocoonDatabase? = null

        fun getInstance(context: Context): CocoonDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CocoonDatabase::class.java,
                    "cocoon.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build().also { instance = it }
            }
        }
    }
}