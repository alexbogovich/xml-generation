package io.github.alexbogovich.xml.generation.utils

import io.github.alexbogovich.xml.generation.dirStringPath
import io.github.alexbogovich.xml.generation.model.LinkbaseEnum
import io.github.alexbogovich.xml.writer.dsl.DslXMLStreamWriter
import java.nio.file.Path
import java.nio.file.Paths

fun getRelatedHrefWithUnixSlash(startPath: Path, relatedTo: Path) =
    startPath.parent.relativize(relatedTo).toString().replace('\\', '/')

fun getLinkBaseRefPath(linkbase: LinkbaseEnum, path: Path): String {
    return Paths.get(dirStringPath)
            .resolve(linkbase.relatedPath)
            .let { getRelatedHrefWithUnixSlash(path, it) }
}

fun Path.createIfNotExist(): Path {
    toFile().run {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            println("create ${toAbsolutePath()}")
            createNewFile()
        }
    }
    return this
}

fun String.getDslSteamWriter(): DslXMLStreamWriter {
    return Paths.get(dirStringPath)
            .resolve(this)
            .createIfNotExist()
            .let { path: Path -> DslXMLStreamWriter(path) }
}