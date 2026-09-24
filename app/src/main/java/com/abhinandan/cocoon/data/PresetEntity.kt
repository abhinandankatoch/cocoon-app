package com.abhinandan.cocoon.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val minutes: Int,
    val iconKey: String
)