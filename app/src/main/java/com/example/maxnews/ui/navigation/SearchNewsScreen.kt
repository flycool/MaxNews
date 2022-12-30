package com.example.maxnews.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.maxnews.data.model.encodeToString
import com.example.maxnews.ui.NewsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchNewsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val state by viewModel.searchState.collectAsStateWithLifecycle()
            TextField(
                value = viewModel.searchQuery,
                onValueChange = {
                    viewModel.updateSearQuery(it)
                },
            )
            LazyColumn() {
                items(state.articles) { item ->
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

            if (state.isLoading) {
                CircularProgressIndicator()
            }
            state.error?.let {
                Text(text = it, color = Color.Red)
            }

        }

    }

}