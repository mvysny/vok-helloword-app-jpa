package com.example.vok

import com.github.vok.framework.db
import org.hibernate.validator.constraints.Length
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class Article(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @field:NotNull
        @field:Length(min = 5)
        var title: String? = null,

        var text: String? = null
) : Serializable {
    companion object {
        fun find(id: Long): Article? = db { em.find(Article::class.java, id) }
    }
}
