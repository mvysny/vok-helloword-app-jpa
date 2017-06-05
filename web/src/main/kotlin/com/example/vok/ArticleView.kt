package com.example.vok

import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.themes.ValoTheme

@AutoView
class ArticleView: FormLayout(), View {
    private lateinit var article: Article
    private val title: Label
    private val text: Label
    init {
        title = label {
            caption = "Title:"
        }
        text = label {
            caption = "Text:"
        }
        button("Edit", { EditArticleView.navigateTo(article.id!!) }) {
            styleName = ValoTheme.BUTTON_LINK
        }
        button("Back", { navigateToView<ArticlesView>() }) {
            styleName = ValoTheme.BUTTON_LINK
        }
    }
    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
        val articleId = event.parameterList[0]?.toLong() ?: throw RuntimeException("Article ID is missing")
        article = Article.find(articleId)!!
        title.value = article.title
        text.value = article.text
    }

    companion object {
        fun navigateTo(articleId: Long) = navigateToView<ArticleView>(articleId.toString())
    }
}