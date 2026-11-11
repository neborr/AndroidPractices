package com.example.androidpractices.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidpractices.domain.model.Launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onLaunchClick: (String) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранные запуски") }
            )
        }
    ) { paddingValues ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет избранных запусков.\nДобавляйте запуски в избранное со списка или экрана деталей.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.id }) { launch ->
                    FavoriteItem(
                        launch = launch,
                        onClick = { onLaunchClick(launch.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(launch) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    launch: Launch,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!launch.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = launch.imageUrl,
                    contentDescription = launch.name,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 12.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
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
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Удалить из избранного",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
