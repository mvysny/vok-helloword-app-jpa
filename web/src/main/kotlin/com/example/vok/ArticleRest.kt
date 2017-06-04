package com.example.vok

import com.github.vok.framework.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/articles")
class ArticleRest {

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun get(@PathParam("id") id: Long): Article? = db { em.find(Article::class.java, id) }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAll(): List<Article> = db { em.findAll<Article>() }
}