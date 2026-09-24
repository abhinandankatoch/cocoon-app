package com.abhinandan.cocoon.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.ui.graphics.vector.ImageVector

val presetIconOptions = listOf(
    "book" to Icons.AutoMirrored.Filled.MenuBook,
    "science" to Icons.Filled.Science,
    "code" to Icons.Filled.Code,
    "edit" to Icons.Filled.Edit,
    "meditation" to Icons.Filled.SelfImprovement
)

fun iconForKey(key: String): ImageVector =
    presetIconOptions.firstOrNull { it.first == key }?.second ?: Icons.AutoMirrored.Filled.MenuBook