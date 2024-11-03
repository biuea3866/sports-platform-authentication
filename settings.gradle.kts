pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings

    val kotlinJvmPluginVersion: String by settings
    val jibPluginVersion: String by settings
    val springDependencyManagementVersion: String by settings

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
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

    plugins {
        kotlin("jvm") version kotlinJvmPluginVersion
        id("com.google.cloud.tools.jib") version jibPluginVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}

rootProject.name = "spring-template"

include(":spring-template-core-domain")
include(":spring-template-core-application")
include(":spring-template-primary-api")
include(":spring-template-primary-batch")
include(":spring-template-primary-consumer")
include(":spring-template-secondary-mysql")
include(":spring-template-secondary-mongo")
include(":spring-template-secondary-kafka")
include(":spring-template-secondary-redis")
include(":spring-template-secondary-s3")

project(":spring-template-core-domain").projectDir = file("core/domain")
project(":spring-template-core-application").projectDir = file("core/application")
project(":spring-template-primary-api").projectDir = file("primary/api")
project(":spring-template-primary-batch").projectDir = file("primary/consumer")
project(":spring-template-primary-consumer").projectDir = file("primary/batch")
project(":spring-template-secondary-mysql").projectDir = file("secondary/mysql")
project(":spring-template-secondary-mongo").projectDir = file("secondary/mongo")
project(":spring-template-secondary-kafka").projectDir = file("secondary/redis")
project(":spring-template-secondary-redis").projectDir = file("secondary/s3")
project(":spring-template-secondary-s3").projectDir = file("secondary/kafka")