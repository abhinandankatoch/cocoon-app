package com.abhinandan.cocoon.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddPresetDialog(
    onDismiss: () -> Unit,
    onConfirm: (label: String, minutes: Int, iconKey: String) -> Unit
) {
    var label by remember { mutableStateOf("") }
    var minutesText by remember { mutableStateOf("25") }
    var selectedIconKey by remember { mutableStateOf(presetIconOptions.first().first) }

    val minutes = minutesText.toIntOrNull()
    val isValid = label.isNotBlank() && minutes != null && minutes > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New preset") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { minutesText = it.filter { c -> c.isDigit() } },
                    label = { Text("Minutes") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    presetIconOptions.forEach { (key, icon) ->
                        val selected = key == selectedIconKey
                        Surface(
                            onClick = { selectedIconKey = key },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = key,
                                    tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(label.trim(), minutes ?: 0, selectedIconKey) }, enabled = isValid) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}