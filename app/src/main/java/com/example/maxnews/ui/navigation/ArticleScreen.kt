package com.example.maxnews.ui.navigation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maxnews.R
import com.example.maxnews.data.model.Article
import com.example.maxnews.ui.NewsViewModel
import kotlinx.coroutines.launch

@Composable
fun ArticleScreen(
    article: Article?,
    viewModel: NewsViewModel,
    isSaved: Boolean? = false
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (!isSaved!!) {
                FloatingActionButton(
                    onClick = {
                        article?.let {
                            viewModel.saveArticle(article)
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("save article successful")
                            }
                        }
                    },
                    backgroundColor = Color.Yellow,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }
            }
        },
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
    }
}