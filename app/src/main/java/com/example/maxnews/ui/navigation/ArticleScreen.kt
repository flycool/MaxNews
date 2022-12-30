package com.example.maxnews.ui.navigation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.maxnews.data.model.Article

@Composable
fun ArticleScreen(
    articleUrl: String?,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                 WebView(it).apply {
                     webViewClient = WebViewClient()
                     articleUrl?.let {
                         loadUrl(it)
                     }
                 }
            },
        )
    }
}