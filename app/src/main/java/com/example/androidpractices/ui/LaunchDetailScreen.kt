package com.example.androidpractices.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androidpractices.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailScreen(
    launchId: String,
    onBackClick: () -> Unit
) {
    val launch = MockData.sampleLaunches.find { it.id == launchId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(launch?.name ?: "Детали запуска") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (launch == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text("Запуск не найден")
            }
        } else {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                val (
                    titleRef, providerRef, statusRef,
                    dateRef, locationRef, descTitleRef, descRef
                ) = createRefs()

                Text(
                    text = launch.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                Text(
                    text = "Провайдер: ${launch.provider}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.constrainAs(providerRef) {
                        top.linkTo(titleRef.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.constrainAs(statusRef) {
                        top.linkTo(providerRef.top)
                        bottom.linkTo(providerRef.bottom)
                        end.linkTo(parent.end)
                    }
                ) {
                    Text(
                        text = launch.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Text(
                    text = "Время: ${launch.windowStart}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(dateRef) {
                        top.linkTo(providerRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Площадка: ${launch.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(locationRef) {
                        top.linkTo(dateRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.constrainAs(createRef()) {
                        top.linkTo(locationRef.bottom, margin = 16.dp)
                    }
                )

                Text(
                    text = "Описание миссии",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(descTitleRef) {
                        top.linkTo(locationRef.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = launch.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(descRef) {
                        top.linkTo(descTitleRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }
    }
}