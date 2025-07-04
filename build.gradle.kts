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
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20250107") // Json Json 2025
    implementation("com.github.UsUsStudios:Torgrays-Datagen:v1.0")
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


tasks.register<JavaExec>("generateData") {
    mainClass.set("net.dinglezz.torgrays_trials.data_generation.DataGenerator")
    classpath = sourceSets.main.get().runtimeClasspath
}
