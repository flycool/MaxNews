package com.example.maxnews

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.maxnews.data.model.Article
import com.example.maxnews.data.model.ArticleParamType
import com.example.maxnews.ui.NewsViewModel
import com.example.maxnews.ui.navigation.*
import com.example.maxnews.ui.theme.MaxNewsTheme
import com.example.maxnews.util.key_article
import com.example.maxnews.util.key_article_url
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaxNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Home()
                }
            }
        }
    }
}

@Composable
fun Home() {
    val navController = rememberNavController()
    val viewModel = viewModel<NewsViewModel>()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconId),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(id = screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.BreakingNewsScreen.route,
            modifier = Modifier.padding(it)
        ) {

            composable(Screen.BreakingNewsScreen.route) {
                BreakingNewsScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.SavedNewsScreen.route) {
                SavedNewsScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.SearchNewsScreen.route) {
                SearchNewsScreen(navController = navController, viewModel = viewModel)
            }
            composable(
                route = Screen.ArticleScreen.route + "/{$key_article}/{isSaved}",
                arguments = listOf(
                    navArgument(key_article) {
                        type = ArticleParamType()
                    },
                    navArgument("isSaved") {
                        type = NavType.BoolType
                    }
                )
            ) { entry ->
                ArticleScreen(
                    article = entry.arguments?.getParcelable(key_article),
                    viewModel = viewModel,
                    isSaved = entry.arguments?.getBoolean("isSaved")
                )
            }
        }
    }
}