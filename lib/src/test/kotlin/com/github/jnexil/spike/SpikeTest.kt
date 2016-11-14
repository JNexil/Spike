package com.github.jnexil.spike

import com.winterbe.expekt.*
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*
import java.io.*
import java.util.Arrays.*

class SpikeTest: Spek(spec = {
    on("load single artifact without configuration") {
        val artifact = Artifact.get(type = "maven", value = "su.jfdev.sheaf:sheaf-kt:0.1.0")
        val targetFile = File("build/test", "libraries-for-testing")
        beforeEach {
            Spike.downloading(artifact).into(targetFile).run()
        }
        afterEach {
            targetFile.delete()
        }
        describe("load single jar only to 'libraries'") {
            val expectedFile = File(targetFile, "sheaf-kt-0.1.0.jar")
            val directoryFiles = targetFile.listFiles().orEmpty().toList()
            it("should contains single file"){
                directoryFiles.should.have.size(1)
            }
            it("should not contains directory"){
                expectedFile.should.satisfy(File::isFile)
            }
            it("should contains library jar"){
                directoryFiles.should.have.elements(expectedFile)
            }
            it("should legal download jar"){
                val expectedContent = resource.readBytes()
                val resultContent = expectedFile.readBytes()
                resultContent.assertHaveSameContent(expectedContent)
            }
        }
    }
})

private fun ByteArray.assertHaveSameContent(expectedContent: ByteArray) = assert(equals(expectedContent, this)){
    "Expected $expectedContent, but $this"
}

val resource: InputStream = SpikeTest::class.java.getResourceAsStream("sheaf-kt-0.1.0.jar.expectedFile")