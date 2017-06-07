package com.example.vok

import com.github.vok.framework.db
import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.UserError
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class ArticleView: VerticalLayout(), View {
    private lateinit var article: Article
    private lateinit var title: Label
    private lateinit var text: Label
    private val comments: Label
    private val commentBinder = beanValidationBinder<Comment>()
    private lateinit var createComment: Button
    init {
        formLayout {
            title = label {
                caption = "Title:"
            }
            text = label {
                caption = "Text:"
            }
        }
        comments = label { caption = "Comments"; isMargin = false }
        formLayout {
            caption = "Add a comment:"
            textField("Commenter:") {
                bind(commentBinder).bind(Comment::commenter)
            }
            textField("Body:") {
                bind(commentBinder).bind(Comment::body)
            }
            createComment = button("Create", { createComment() })
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
        refreshComments()
    }
    private fun createComment() {
        val comment = Comment(article = article)
        if (!commentBinder.validate().isOk || !commentBinder.writeBeanIfValid(comment)) {
            createComment.componentError = UserError("There are invalid fields")
        } else {
            createComment.componentError = null
            db { em.persist(comment) }
            refreshComments()
            commentBinder.readBean(Comment())  // this clears the comment fields
        }
    }
    private fun refreshComments() {
        comments.html(db {
            // workaround to avoid Hibernate's LazyInitializationException on collections...
            Article.find(article.id!!)!!.comments.joinToString("") { comment ->
                "<p><strong>Commenter:</strong>${comment.commenter}</p><p><strong>Comment:</strong>${comment.body}</p>"
            }
        })
    }
    companion object {
        fun navigateTo(articleId: Long) = navigateToView<ArticleView>(articleId.toString())
    }
}
