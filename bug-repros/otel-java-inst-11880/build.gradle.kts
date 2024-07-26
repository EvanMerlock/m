plugins {
    id("java")
}

group = "dev.merlock"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.7.1")
    implementation("io.opentelemetry.instrumentation:opentelemetry-kafka-clients-2.6:2.6.0-alpha")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}