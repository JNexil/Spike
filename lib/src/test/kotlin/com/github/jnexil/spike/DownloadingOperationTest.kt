package com.github.jnexil.spike

import com.winterbe.expekt.*
import org.eclipse.aether.graph.*
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*
import java.io.*

class DownloadingOperationTest: Spek(spec = {
    fun artifact() = object: Artifact {
        override val type: String get() = throw UnsupportedOperationException()
        override val dependencies: Sequence<Dependency> get() = throw UnsupportedOperationException()
    }
    describe("without varargs") {
        context("combined sequential operations"){
            given("should save old properties [into any file | downloading one]"){
                val artifact = artifact()
                val file = File("")

                val spike = Spike.into(file).downloading(artifact)
                val iteratedArtifacts = spike.artifacts.toList()

                it("should contain single artifact"){
                    iteratedArtifacts.should.have.elements(artifact)
                }
                it("should have given output"){
                    spike.output.should.equal(file)
                }
            }
        }
        context("single operation"){
            given("downloading one"){
                val artifact = artifact()
                val spike = Spike.downloading(artifact)
                val iteratedArtifacts = spike.artifacts.toList()

                it("should contain single artifact"){
                    iteratedArtifacts.should.have.elements(artifact)
                }
            }
        }
    }
    describe("with varargs") {
        context("combined sequential operations") {
            given("should save old properties [into any file | downloading one]") {
                val first = artifact()
                val second = artifact()
                val file = File("")

                val spike = Spike.into(file).downloading(first, second)
                val iteratedArtifacts = spike.artifacts.toList()

                it("should contain single artifact") {
                    iteratedArtifacts.should.have.elements(first, second)
                }
                it("should have given output"){
                    spike.output.should.equal(file)
                }
            }
        }
        context("single operation") {
            given("downloading none") {
                val spike = Spike.downloading()
                val iteratedArtifacts = spike.artifacts.toList()

                it("should not contain any artifact") {
                    iteratedArtifacts.should.have.elements()
                }
            }
            given("downloading few") {
                val first = artifact()
                val second = artifact()

                val spike = Spike.downloading(first, second)
                val iteratedArtifacts = spike.artifacts.toList()

                it("should contain two artifacts") {
                    iteratedArtifacts.should.have.elements(first, second)
                }
            }
        }
    }
})