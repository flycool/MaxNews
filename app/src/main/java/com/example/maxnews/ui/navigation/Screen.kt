package com.example.maxnews.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.maxnews.R

sealed class Screen(
    val route: String,
    @DrawableRes val iconId: Int,
    @StringRes val resourceId: Int,
) {
    object BreakingNewsScreen : Screen(
        "breakingnews",
        iconId = R.drawable.ic_breaking_news,
        resourceId = R.string.breaking_news
    )

    object SavedNewsScreen :
        Screen("savednews", iconId = R.drawable.ic_favorite, resourceId = R.string.saved_news)

    object SearchNewsScreen :
        Screen("searchnews", iconId = R.drawable.ic_all_news, resourceId = R.string.search_news)

    object ArticleScreen:Screen("article",0, 0)
}

val bottomItems = listOf(
    Screen.BreakingNewsScreen,
    Screen.SavedNewsScreen,
    Screen.SearchNewsScreen
)
