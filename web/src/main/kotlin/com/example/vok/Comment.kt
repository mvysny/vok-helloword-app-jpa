package com.example.vok

import org.hibernate.validator.constraints.Length
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class Comment(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @field:NotNull
        @field:Length(min = 3)
        var commenter: String? = null,

        @field:NotNull
        @field:Length(min = 3)
        var body: String? = null,

        @ManyToOne
        @JoinColumn(name = "article_id")
        var article: Article? = null
) : Serializable
