package com.abhinandan.cocoon.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PresetDao {
    @Insert
    suspend fun insert(preset: PresetEntity)

    @Query("SELECT * FROM presets ORDER BY id ASC")
    fun getAllPresets(): Flow<List<PresetEntity>>

    @Query("SELECT COUNT(*) FROM presets")
    suspend fun count(): Int
}