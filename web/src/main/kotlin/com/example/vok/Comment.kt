package com.example.vok

import com.fasterxml.jackson.annotation.JsonIgnore
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
        var body: String? = null
) : Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: Article? = null
}
