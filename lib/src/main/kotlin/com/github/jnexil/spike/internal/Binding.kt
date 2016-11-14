/**
 * Simplified access to aether methods
 */
package com.github.jnexil.spike.internal

import org.eclipse.aether.*
import org.eclipse.aether.artifact.*
import org.eclipse.aether.collection.*
import org.eclipse.aether.graph.*
import org.eclipse.aether.resolution.*
import org.eclipse.aether.util.graph.visitor.*

inline fun RepositorySystem.collectDependencies(session: RepositorySystemSession, configure: CollectRequest.() -> Unit): CollectResult {
    val request = CollectRequest().apply(configure)
    return collectDependencies(session, request)
}

inline fun RepositorySystem.resolveDependencies(session: RepositorySystemSession, configure: DependencyRequest.() -> Unit): DependencyResult {
    val request = DependencyRequest().apply(configure)
    return resolveDependencies(session, request)
}

val DependencyNode.artifacts: Sequence<Artifact> get() {
    val generator = PreorderNodeListGenerator()
    accept(generator)
    return generator.getArtifacts(false).asSequence()
}

val Artifact.fullName: String get() = when {
    classifier.isEmpty() -> "$groupId:$artifactId:$version"
    else                 -> "$groupId:$artifactId:$version:$classifier"
}