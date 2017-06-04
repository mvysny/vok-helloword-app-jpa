package com.example.vok

import java.util.jar.Manifest

private val manifests: Sequence<Manifest>
    get() = Thread.currentThread().contextClassLoader.getResources("META-INF/MANIFEST.MF").asSequence().map { it.openStream().use { Manifest(it) } }

val kotlinVersion: String
    get() {
        val kotlinManifest = manifests.first { it.mainAttributes.getValue("Implementation-Title") == "kotlin-stdlib" }
        return kotlinManifest.mainAttributes.getValue("Implementation-Version")
    }
