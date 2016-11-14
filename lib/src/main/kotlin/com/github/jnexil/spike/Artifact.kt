package com.github.jnexil.spike

import org.eclipse.aether.graph.*

interface Artifact {
    val type: String

    companion object {
        operator fun get(type: String, value: String): Artifact = when (type.toLowerCase()) {
            "maven" -> MavenArtifact(value)
            else    -> throw UnsupportedOperationException("Unknown type $type")
        }
    }
}