package com.example.vok

import com.github.vok.framework.db
import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.UserError
import com.vaadin.ui.Button
import com.vaadin.ui.HasComponents
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class ArticleView: VerticalLayout(), View {
    private lateinit var article: Article
    private lateinit var title: Label
    private lateinit var text: Label
    private val comments: CommentsComponent
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
        comments = comments()
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
        comments.show(article.id!!)
    }
    private fun createComment() {
        val comment = Comment()
        if (!commentBinder.validate().isOk || !commentBinder.writeBeanIfValid(comment)) {
            createComment.componentError = UserError("There are invalid fields")
        } else {
            createComment.componentError = null
            comment.article = article
            db { em.persist(comment) }
            comments.show(article.id!!)
            commentBinder.readBean(Comment())  // this clears the comment fields
        }
    }

    companion object {
        fun navigateTo(articleId: Long) = navigateToView<ArticleView>(articleId.toString())
    }
}

private class CommentsComponent : Label() {
    init {
        caption = "Comments"
    }
    fun show(articleId: Long) {
        html(db {
            // force-update the comments list.
            Article.find(articleId)!!.comments.joinToString("") { comment ->
                "<p><strong>Commenter:</strong>${comment.commenter}</p><p><strong>Comment:</strong>${comment.body}</p>"
            }
        })
    }
}
private fun HasComponents.comments(block: CommentsComponent.()->Unit = {}) = init(CommentsComponent(), block)
