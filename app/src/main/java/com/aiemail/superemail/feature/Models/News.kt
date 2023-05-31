package com.aiemail.superemail.feature.Models

class News(sourceName: String, articleList: List<Article>) {
    private var sourceName: String = sourceName
    private var articleList: List<Article> = articleList



    fun getSourceName(): String? {
        return sourceName
    }

    fun getArticleList(): List<Article>? {
        return articleList
    }

}