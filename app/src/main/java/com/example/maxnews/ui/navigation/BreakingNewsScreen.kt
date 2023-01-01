package com.example.maxnews.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.maxnews.R
import com.example.maxnews.data.model.Article
import com.example.maxnews.data.model.encodeToString
import com.example.maxnews.ui.NewsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun BreakingNewsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        state.error?.let {
            if (state.articles.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = it, color = Color.Red)
                    Button(onClick = {
                        viewModel.getBreakingNews("us")
                        Toast.makeText(context, "retry", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "retry")
                    }
                }
            }
        }

        val lazyListState = rememberLazyListState()
        var reIndex by remember { mutableStateOf(0) }
        val goNext by remember {
            derivedStateOf {
                reIndex >= state.articles.size - 1
            }
        }
        if (goNext && !state.isLoading && state.articles.isNotEmpty()) {
            viewModel.getBreakingNews("us")
        }
        LazyColumn(
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            itemsIndexed(state.articles) { index, item ->
                reIndex = index
                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.ArticleScreen.route + "/${item.encodeToString()}/false")
                        }
                        .padding(10.dp)
                ) {
                    key(item.url) {
                        NewsItem(item)
                    }
                }
            }
            if (state.isLoading) {
                item {
                    CircularProgressIndicator(

                    )
                }
            }
        }

    }
}

@Composable
fun NewsItem(
    article: Article
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.width(80.dp)
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_favorite),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(70.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            article.author?.let { author ->
                Text(
                    text = author,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = article.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 2
        )
    }
}

