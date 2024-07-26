plugins {
    id("java")
    id("application")
}

group = "dev.merlock"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-simple:2.0.13")

    implementation("org.apache.kafka:kafka-clients:3.7.1")
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.40.0"));
    implementation("io.opentelemetry:opentelemetry-api");
    implementation("io.opentelemetry:opentelemetry-sdk");
    implementation("io.opentelemetry:opentelemetry-exporter-logging");
    implementation("io.opentelemetry:opentelemetry-exporter-otlp");
    implementation("io.opentelemetry.semconv:opentelemetry-semconv:1.26.0-alpha");
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure");
    implementation("io.opentelemetry.instrumentation:opentelemetry-kafka-clients-2.6:2.6.0-alpha")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "dev.merlock.Main"
    applicationDefaultJvmArgs = listOf(
        "-Dotel.service.name=test",
        "-Dotel.metrics.exporter=otlp",
        "-Dotel.exporter.otlp.protocol=http/protobuf",
        "-Dotel.traces.exporter=console",
        "-Dotel.logging.exporter=console",
        "-Dotel.metric.export.interval=15000"
    )
}

tasks.test {
    useJUnitPlatform()
}