package com.example.maxnews.data.model

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Moshi
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    //val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?,
) : Parcelable {

    override fun hashCode(): Int {
        var result = id.hashCode()
        if (url.isNullOrEmpty()) {
            result = 31 * result + url.hashCode()
        }
        return result
    }

}

class ArticleParamType : NavType<Article>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Article? {
        return bundle.getParcelable(key)
    }

    @SuppressLint("CheckResult")
    override fun parseValue(value: String): Article {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Article::class.java)
        return adapter.fromJson(value)!!
    }

    override fun put(bundle: Bundle, key: String, value: Article) {
        bundle.putParcelable(key, value)
    }

}

fun Article.encodeToString() =
    Uri.encode(
        Moshi.Builder().build().adapter(Article::class.java).toJson(this)
    )
