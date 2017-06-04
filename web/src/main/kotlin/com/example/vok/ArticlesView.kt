package com.example.vok

import com.github.vok.framework.jpaDataProvider
import com.github.vok.karibudsl.*
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Grid
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@AutoView
class ArticlesView: VerticalLayout(), View {
    private val dataSource = jpaDataProvider<Article>()
    private val grid: Grid<Article>
    init {
        setSizeFull()
        button("New Article", { navigateToView<CreateArticleView>() }) {
            styleName = ValoTheme.BUTTON_LINK
        }
        grid = grid(Article::class, "Listing articles", dataSource) {
            expandRatio = 1f
            setSizeFull()
        }
    }
    override fun enter(event: ViewChangeListener.ViewChangeEvent?) {
        grid.dataProvider.refreshAll()
    }
}
