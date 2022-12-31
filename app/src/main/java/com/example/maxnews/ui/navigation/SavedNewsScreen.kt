package com.example.maxnews.ui.navigation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.maxnews.data.model.encodeToString
import com.example.maxnews.ui.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun SavedNewsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val savedNews by viewModel.savedNews.collectAsStateWithLifecycle()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(savedNews) { item ->
                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.ArticleScreen.route + "/${item.encodeToString()}/true")
                        }
                        .padding(10.dp)
                ) {
                    key(item.url) {
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.deleteArticle(item)
                                    scope.launch {
                                        val snackbarResult =
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = "delete success",
                                                actionLabel = "Undo"
                                            )
                                        when (snackbarResult) {
                                            SnackbarResult.ActionPerformed -> {
                                                viewModel.saveArticle(item)
                                            }
                                            SnackbarResult.Dismissed -> {

                                            }
                                        }
                                    }
                                }
                                true
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {},
                            modifier = Modifier.fillMaxWidth(),
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.25f else 0.5f)
                            }
                        ) {
                            Text(text = item.title)
                        }
                    }
                }
            }
        }
    }
}