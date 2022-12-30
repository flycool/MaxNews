package com.example.maxnews.ui.navigation

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import com.example.maxnews.data.model.Article
import com.example.maxnews.data.model.encodeToString
import com.example.maxnews.ui.NewsViewModel
import com.squareup.moshi.Moshi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun BreakingNewsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        state.error?.let {
            Column {
                Text(text = it, color = Color.Red)
                Button(onClick = {
                    viewModel.getBreakingNews("us")
                }) {
                    Text(text = "retry")
                }
            }
        }
        Column {
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
        }
    }
}

