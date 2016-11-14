package com.github.jnexil.spike

import java.io.*

interface ArtifactSession {
    fun writeTo(targetFile: File)
}