package com.github.jnexil.spike.internal

import com.github.jnexil.spike.*
import org.apache.maven.repository.internal.*
import org.eclipse.aether.*
import org.eclipse.aether.collection.*
import org.eclipse.aether.repository.*
import org.eclipse.aether.resolution.*
import su.jfdev.anci.logging.*
import java.io.*


object SpikeExecutor {
    private val logger = Logger[SpikeExecutor::class]
    private val cache = File("temporary_cache")
    private val system = MavenRepositorySystemUtils.newServiceLocator().run {
        getService(RepositorySystem::class.java)
    }

    private val repositoryCentral = RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2/").build()

    fun execute(spike: Spike) {
        val session = createSession(system)
        logger.trace { "create session" }
        val collectResponse = collectDependencies(session, spike)
        logger.debug { "collected dependencies: [${collectResponse.root.artifacts.joinToString()}]" }
        val resolveResponse = resolveDependencies(session, collectResponse)
        logger.debug { "resolved dependencies: [${resolveResponse.root.artifacts.joinToString()}]" }
        for (artifact in resolveResponse.root.artifacts) {
            logger.info { "try save artifact: $artifact" }
            artifact.saveArtifact(spike)
        }
    }

    private fun org.eclipse.aether.artifact.Artifact.saveArtifact(spike: Spike) {
        if (extension == "jar") {
            val newFile = File(spike.output, fullName)
            logger.info { "save artifact from: $file to $newFile" }
            file.copyTo(newFile)
        } else logger.info { "skip artifact $this" }
    }

    private fun createSession(system: RepositorySystem): RepositorySystemSession {
        val session = MavenRepositorySystemUtils.newSession()
        addLocalRepository(session, system)
        return session
    }

    private fun addLocalRepository(session: DefaultRepositorySystemSession, system: RepositorySystem) {
        val localRepository = LocalRepository(cache)
        session.localRepositoryManager = system.newLocalRepositoryManager(session, localRepository)
    }


    private fun collectDependencies(session: RepositorySystemSession, spike: Spike): CollectResult = system.collectDependencies(session) {
        addArtifacts(spike.artifacts)
        addRepository(repositoryCentral)
    }

    private fun CollectRequest.addArtifacts(artifacts: Sequence<Artifact>) {
        for (artifact in artifacts) {
            logger.info { "add artifact: $artifact" }
            for (dependency in artifact.dependencies) {
                logger.info { "add dependency: $dependency" }
                addManagedDependency(dependency)
            }
        }
        logger.info { "Was added ${managedDependencies.size} dependencies" }
    }

    private fun resolveDependencies(session: RepositorySystemSession, collectResponse: CollectResult): DependencyResult = system.resolveDependencies(session) {
        root = collectResponse.root
    }
}