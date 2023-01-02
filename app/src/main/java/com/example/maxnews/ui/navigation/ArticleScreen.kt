package com.example.maxnews.ui.navigation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.maxnews.R
import com.example.maxnews.data.model.Article
import com.example.maxnews.ui.NewsViewModel
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
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

        Column (modifier = Modifier.fillMaxSize()) {
            val state = rememberWebViewState(url = article!!.url)
            val loadingState = state.loadingState

            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = loadingState.progress
                )
            }

            val webViewClient = remember {
                object : AccompanistWebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        Log.d("ArticleScreen", "onPageStarted: $url")
                    }
                }
            }

            WebView(
                state = state,
                onCreated = {
                    it.settings.javaScriptEnabled = true
                },
                client = webViewClient
            )

            /*AndroidView(
                factory = {
                    WebView(it).apply {
                        webViewClient = WebViewClient()
                        article?.let {
                            loadUrl(article.url)
                        }
                    }
                },
            )*/
        }

    }
}