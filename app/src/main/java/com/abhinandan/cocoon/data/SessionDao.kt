package com.abhinandan.cocoon.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: SessionEntity)

    @Query("SELECT * FROM sessions ORDER BY timestampMillis DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>
}