package com.example.maxnews.ui.navigation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maxnews.R
import com.example.maxnews.data.model.Article
import com.example.maxnews.ui.NewsViewModel
import kotlinx.coroutines.launch

@Composable
fun ArticleScreen(
    article: Article?,
    viewModel: NewsViewModel,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    webViewClient = WebViewClient()
                    article?.let {
                        loadUrl(article.url)
                    }
                }
            },
        )
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                if (article!!.id == null) {
                    FloatingActionButton(
                        onClick = {
                            article.let {
                                viewModel.saveArticle(article)
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("save article successful")
                                }
                            }
                        },
                        backgroundColor = Color.Yellow,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = null,
                            tint = Color.Blue
                        )
                    }
                }
            }
        ) {

        }
    }
}