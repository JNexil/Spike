package com.github.jnexil.spike

import java.io.*


interface Spike: () -> Unit, Runnable {
    override fun run(): Unit = TODO("not implemented")
    override fun invoke(): Unit = run()

    val output: Sequence<File>
    val artifacts: Sequence<Artifact>

    fun into(outputFile: File): Spike = object: Spike by this {
        override val output: Sequence<File> = this@Spike.output + outputFile
    }

    fun downloading(artifact: Artifact): Spike = object: Spike by this {
        override val artifacts: Sequence<Artifact> = this@Spike.artifacts + artifact
    }

    fun into(vararg outputFiles: File): Spike = object: Spike by this {
        override val output: Sequence<File> = this@Spike.output + outputFiles
    }

    fun downloading(vararg artifacts: Artifact): Spike = object: Spike by this {
        override val artifacts: Sequence<Artifact> = this@Spike.artifacts + artifacts
    }

    companion object Default: Spike {
        override val output: Sequence<File> = emptySequence()
        override val artifacts: Sequence<Artifact> = emptySequence()
    }
}