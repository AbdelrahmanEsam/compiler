import org.jetbrains.kotlin.gradle.internal.config.LanguageFeature

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("application")
}

group = "com.moj.najiz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflection)
    testImplementation(libs.kotlin.test)
    testImplementation (libs.junit.api)
    testRuntimeOnly (libs.junit.engine)
}

sourceSets.getByName("main") {
    java.srcDir("src/main/kotlin")
}
sourceSets.getByName("test") {
    java.srcDir("src/test/kotlin")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
    sourceSets.all {
        languageSettings {
            enableLanguageFeature(LanguageFeature.WhenGuards.name)
        }
    }
}