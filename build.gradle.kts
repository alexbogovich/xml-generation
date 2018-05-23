plugins {
    kotlin("jvm") version "1.2.41"
    application
}

dependencies {
    compile(kotlin("stdlib"))
    compile("io.github.microutils:kotlin-logging:1.4.9")
    compile("io.github.alexbogovich:xml-writer-dsl:0.1")
    compile("com.google.code.gson:gson:2.8.4")
    compile("org.slf4j:slf4j-simple:1.6.1")
}

repositories {
    jcenter()
}

group = "io.github.alexbogovich"
version = "0.1"

application {
    mainClassName = "io.github.alexbogovich.xml.generation.GenerateKt"
}