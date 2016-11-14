package com.github.jnexil.spike

class MavenArtifact(value: String): Artifact {
    override val type: String get() = TYPE

    companion object {
        const val TYPE = "maven"
    }
}