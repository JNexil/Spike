package com.github.jnexil.spike

import com.google.common.collect.*
import org.eclipse.aether.graph.*

class MavenArtifact(value: String): Artifact, Sequence<Dependency> {
    private val dependency = Dependency(org.eclipse.aether.artifact.DefaultArtifact(value), "compile")
    override val dependencies: Sequence<Dependency> get() = this
    override val type: String get() = TYPE

    companion object {
        const val TYPE = "maven"
    }

    override fun iterator(): Iterator<Dependency> = Iterators.singletonIterator(dependency)
}