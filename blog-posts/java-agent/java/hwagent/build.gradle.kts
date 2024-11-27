import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("net.bytebuddy:byte-buddy:1.15.10")
}

tasks {
    val shadowJar by existing(ShadowJar::class) {
        archiveClassifier.set("")

        manifest {
            attributes(jar.get().manifest.attributes)
            attributes(
                "Agent-Class" to "dev.merlock.hwagent.HWAgent",
                "Premain-Class" to "dev.merlock.hwagent.HWAgent",
            )
        }
    }
}


tasks.test {
    useJUnitPlatform()
}