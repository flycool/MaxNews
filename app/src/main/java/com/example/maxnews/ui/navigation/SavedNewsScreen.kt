package com.example.maxnews.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.maxnews.data.model.encodeToString
import com.example.maxnews.ui.NewsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SavedNewsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val savedNews by viewModel.savedNews.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(savedNews) { item ->
            Column(
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.ArticleScreen.route + "/${item.encodeToString()}")
                    }
                    .padding(10.dp)
            ) {
                Text(text = item.title)
            }
        }
    }
}