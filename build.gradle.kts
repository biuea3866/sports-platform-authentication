import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm")
    id("com.google.cloud.tools.jib")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.spring.dependency-management")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }

    val addGitHooks = register<Copy>("addGitHooks") {
        from(".githooks/pre-commit").into(".git/hooks")
    }

    compileKotlin {
        dependsOn(addGitHooks)
    }
}

allprojects {
    group = "biuea.spring.template"
    version = "1.0.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven {
            url =
                uri("https://maven.pkg.github.com/biuea3866/maven-artifact")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinJvmCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

subprojects {
    val kotlinVersion: String by project
    val coroutinesVersion: String by project
    val springBootVersion: String by project
    val junitJupiterVersion: String by project
    val logbackContribVersion: String by project
    val retrofitVersion: String by project
    val okHttpVersion: String by project
    val jacksonVersion: String by project
    val jakartaValidationApiVersion: String by project
    val fixtureMonkeyVersion: String by project
    val dataFakerVersion: String by project
    val mockitoKotlinVersion: String by project

    apply(plugin = "jacoco")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    tasks {
        withType<Test> {
            useJUnitPlatform()
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
        }

        compileKotlin {
            kotlinOptions.jvmTarget = "17"
        }

        compileTestKotlin {
            kotlinOptions.jvmTarget = "17"
        }

        clean {
            delete("out/")
            delete("build/")
        }
    }

    allOpen {
        annotations(
            "jakarta.persistence.Entity",
            "jakarta.persistence.MappedSuperclass",
            "jakarta.persistence.Embeddable"
        )
    }

    if (System.getProperty("os.arch") == "aarch64") {
        val target = file("${org.gradle.internal.jvm.Jvm.current().javaHome}/lib/libaws-crt-jni.dylib")
        if (!target.exists()) file("${rootProject.projectDir}/lib/libaws-crt-jni.dylib").copyTo(target)
    }

    dependencies {
        val fixtureMonkeyVersion: String by project
        val dataFakerVersion: String by project
        val mockitoKotlinVersion: String by project

        // kotlin
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")

        // spring
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")

        // jackson
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:2.16.0")

        // validation
        implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationApiVersion")

        // logback
        implementation("ch.qos.logback.contrib:logback-jackson:$logbackContribVersion")
        implementation("ch.qos.logback.contrib:logback-json-classic:$logbackContribVersion")

        implementation("javax.inject:javax.inject:1")

        // Coroutines Dependencies
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        implementation("javax.xml.bind:jaxb-api:2.3.1")
        implementation("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
        implementation("org.springframework.boot:spring-boot-starter-parent:3.2.0")

        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "junit")
            exclude(group = "org.junit.vintage")
        }
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-mockito:$fixtureMonkeyVersion")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:$fixtureMonkeyVersion")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jakarta-validation:$fixtureMonkeyVersion")
        testImplementation("net.datafaker:datafaker:$dataFakerVersion")
        testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    }
}
