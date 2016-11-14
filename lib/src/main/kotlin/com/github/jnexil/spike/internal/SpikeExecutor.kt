package com.github.jnexil.spike.internal

import com.github.jnexil.spike.*
import org.apache.maven.repository.internal.*
import org.eclipse.aether.*
import org.eclipse.aether.collection.*
import org.eclipse.aether.repository.*

object SpikeExecutor {
    private val system = createSystem()

    fun execute(spike: Spike) {
        val session = spike.createSession(system)
        val request = CollectRequest().apply {
            TODO("prepare request")
        }
        system.collectDependencies(session, request)
    }

    private fun Spike.createSession(system: RepositorySystem): RepositorySystemSession {
        val session = MavenRepositorySystemUtils.newSession()
        addLocalRepository(session, system)
        return session
    }

    private fun Spike.addLocalRepository(session: DefaultRepositorySystemSession, system: RepositorySystem) {
        val localRepository = LocalRepository(output)
        session.localRepositoryManager = system.newLocalRepositoryManager(session, localRepository)
    }

    private fun createSystem(): RepositorySystem = MavenRepositorySystemUtils.newServiceLocator().run {
        getService(RepositorySystem::class.java)
    }
}