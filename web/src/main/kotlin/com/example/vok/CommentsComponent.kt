package com.example.vok

import com.github.vok.framework.*
import com.github.vok.karibudsl.*
import com.vaadin.ui.HasComponents
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

class CommentsComponent : VerticalLayout() {
    var articleId: Long = 0L
    set(value) { field = value; refresh() }
    init {
        caption = "Comments"; isMargin = false
    }

    fun refresh() {
        removeAllComponents()
        db {
            Article.find(articleId)!!.comments.forEach { comment ->
                label {
                    html("<p><strong>Commenter:</strong>${comment.commenter}</p><p><strong>Comment:</strong>${comment.body}</p>")
                }
                button("Delete comment") {
                    styleName = ValoTheme.BUTTON_LINK
                    onLeftClick { delete(comment) }
                }
            }
        }
    }
    private fun delete(comment: Comment) {
        db { em.delete(comment) }
        refresh()
    }
}
// the extension function which will allow us to use CommentsComponent inside a DSL
fun HasComponents.commentsComponent(block: CommentsComponent.()->Unit = {}) = init(CommentsComponent(), block)
