plugins {
    id("java")
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

group = "us.ajg0702"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo.ajg0702.us/") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    compileOnly(group = "org.spigotmc", name = "spigot-api", version = "1.18.2-R0.1-SNAPSHOT")

    compileOnly("org.spongepowered:configurate-yaml:4.0.0")

    implementation("net.kyori:adventure-api:4.11.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    implementation("net.kyori:adventure-platform-bukkit:4.1.1")

    implementation("us.ajg0702:ajUtils:1.2.11")
    implementation("us.ajg0702.commands.platforms.bukkit:bukkit:1.0.0")
    implementation("us.ajg0702.commands.api:api:1.0.0")
}

tasks.withType<ProcessResources> {
    include("**/*.yml")
    include("**/*.prop")
    include("**/*.zip")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
        "tokens" to mapOf(
            "VERSION" to project.version.toString()
        )
    )
}

tasks.shadowJar {
    relocate("us.ajg0702.utils", "us.ajg0702.lavarising.libs.utils")
    relocate("us.ajg0702.commands", "us.ajg0702.lavarising.commands.base")
    relocate("io.github.slimjar", "us.ajg0702.lavarising.libs.slimjar")
    relocate("net.kyori", "us.ajg0702.lavarising.libs.kyori")
    relocate("org.bstats", "us.ajg0702.lavarising.libs.bstats")
    relocate("org.spongepowered", "us.ajg0702.lavarising.libs")
    relocate("org.yaml", "us.ajg0702.lavarising.libs")
    relocate("io.leangen", "us.ajg0702.lavarising.libs")

    archiveBaseName.set("ajLavaRising")
    archiveClassifier.set("")
    exclude("junit/**/*")
    exclude("org/junit/**/*")
    exclude("org/slf4j/**/*")
    exclude("org/hamcrest/**/*")
    exclude("LICENSE-junit.txt")
    minimize()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}