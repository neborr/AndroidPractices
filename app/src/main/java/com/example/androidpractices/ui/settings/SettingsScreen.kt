package com.example.androidpractices.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onDone: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val limitFilter by viewModel.limitFilter.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("Any", "Success", "Failure", "Go", "TBD")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки фильтрации") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Настройте параметры фильтрации запусков:",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Фильтр по названию / поисковый запрос") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = statusFilter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Статус запуска") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    statuses.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                viewModel.onStatusFilterChanged(status)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Column {
                Text(text = "Количество результатов (Limit): $limitFilter")
                Slider(
                    value = limitFilter.toFloat(),
                    onValueChange = { viewModel.onLimitFilterChanged(it.toInt()) },
                    valueRange = 10f..50f,
                    steps = 3
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.resetSettings {
                            onDone()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Сбросить")
                }
                Button(
                    onClick = {
                        viewModel.saveSettings {
                            onDone()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(text = "Готово", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
