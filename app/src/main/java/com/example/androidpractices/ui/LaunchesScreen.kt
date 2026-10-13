package com.example.androidpractices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidpractices.data.Launch
import com.example.androidpractices.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(onLaunchClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Космические запуски") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.sampleLaunches) { launch ->
                LaunchItem(launch = launch, onClick = { onLaunchClick(launch.id) })
            }
        }
    }
}

@Composable
fun LaunchItem(launch: Launch, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = launch.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Оператор: ${launch.provider}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Статус: ${launch.status}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Дата: ${launch.windowStart}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}