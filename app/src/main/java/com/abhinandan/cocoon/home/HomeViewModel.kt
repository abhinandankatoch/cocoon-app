package com.abhinandan.cocoon.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhinandan.cocoon.data.CocoonDatabase
import com.abhinandan.cocoon.data.PresetEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val presetDao = CocoonDatabase.getInstance(application).presetDao()

    private val _presets = MutableStateFlow<List<Preset>>(emptyList())
    val presets: StateFlow<List<Preset>> = _presets

    init {
        viewModelScope.launch {
            if (presetDao.count() == 0) {
                presetDao.insert(PresetEntity(label = "Physics", minutes = 45, iconKey = "science"))
                presetDao.insert(PresetEntity(label = "Coding", minutes = 60, iconKey = "code"))
                presetDao.insert(PresetEntity(label = "Reading", minutes = 30, iconKey = "book"))
            }
            presetDao.getAllPresets().collect { entities ->
                _presets.value = entities.map { Preset(it.label, it.minutes, iconForKey(it.iconKey)) }
            }
        }
    }

    fun addPreset(label: String, minutes: Int, iconKey: String) {
        viewModelScope.launch {
            presetDao.insert(PresetEntity(label = label, minutes = minutes, iconKey = iconKey))
        }
    }
}