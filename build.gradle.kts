plugins {
    id("java")
    id("application")
}

group = "net.dinglezz.torgrays_trials"
version = "Alpha-2.3"

application {
    mainClass.set("net.dinglezz.torgrays_trials.main.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceSets["main"].java.srcDirs("src/main/src")
}

tasks.register<Jar>("buildFatJar") {
    archiveBaseName.set("Torgrays-Trials")
    from(sourceSets["main"].output)

    manifest.from(file("src/main/META-INF/MANIFEST.MF"))

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}