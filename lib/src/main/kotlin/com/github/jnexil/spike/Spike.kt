package com.github.jnexil.spike

import com.github.jnexil.spike.internal.*
import java.io.*


interface Spike: () -> Unit, Runnable {
    override fun run(): Unit = SpikeExecutor.execute(this)
    override fun invoke(): Unit = run()

    val output: File
    val artifacts: Sequence<Artifact>

    fun into(outputFile: File): Spike = object: Spike by this {
        override val output: File = outputFile
    }

    fun downloading(artifact: Artifact): Spike = object: Spike by this {
        override val artifacts: Sequence<Artifact> = this@Spike.artifacts + artifact
    }

    fun downloading(vararg artifacts: Artifact): Spike = object: Spike by this {
        override val artifacts: Sequence<Artifact> = this@Spike.artifacts + artifacts
    }

    companion object Default: Spike {
        override val output: File = File("libraries")
        override val artifacts: Sequence<Artifact> = emptySequence()
    }
}