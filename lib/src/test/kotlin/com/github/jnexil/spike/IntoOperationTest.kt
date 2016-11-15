package com.github.jnexil.spike

import com.winterbe.expekt.*
import org.eclipse.aether.graph.*
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*
import java.io.*

class IntoOperationTest: Spek(spec = {
    fun artifact() = object: Artifact {
        override val type: String get() = throw UnsupportedOperationException()
        override val dependencies: Sequence<Dependency> get() = throw UnsupportedOperationException()
    }
    given("single operation"){
        context("combined sequential operations"){
            given("should save old properties [downloading one | into any file]"){
                val artifact = artifact()
                val file = File("")

                val spike = Spike.downloading(artifact).into(file)
                val iteratedArtifacts = spike.artifacts.toList()

                it("should contain single artifact"){
                    iteratedArtifacts.should.have.elements(artifact)
                }
                it("should have given output"){
                    spike.output.should.equal(file)
                }
            }
        }
        on("into file"){
            val file = File("")
            val spike = Spike.into(file)

            it("should contain given output"){
                spike.output.should.equal(file)
            }
        }
    }
})