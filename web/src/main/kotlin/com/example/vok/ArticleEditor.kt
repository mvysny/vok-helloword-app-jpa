package com.example.vok

import com.github.vok.framework.db
import com.github.vok.karibudsl.*
import com.vaadin.server.UserError
import com.vaadin.ui.HasComponents
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

class ArticleEditor : VerticalLayout() {
    private val binder = beanValidationBinder<Article>()
    var article: Article? = null
    set(value) {
        field = value
        if (value != null) binder.readBean(value)
    }

    init {
        isMargin = false
        textField("Title") {
            bind(binder).bind(Article::title)
        }
        textArea("Text") {
            bind(binder).bind(Article::text)
        }
        button("Save Article", { event ->
            val article = article!!
            if (binder.validate().isOk && binder.writeBeanIfValid(article)) {
                db { if (article.id == null) em.persist(article) else em.merge(article) }
                ArticleView.navigateTo(article.id!!)
            } else {
                event.button.componentError = UserError("There are invalid fields")
            }
        })
        button("Back", { navigateToView<ArticlesView>() }) {
            styleName = ValoTheme.BUTTON_LINK
        }
    }
}

fun HasComponents.articleEditor(block: ArticleEditor.()->Unit = {}) = init(ArticleEditor(), block)
