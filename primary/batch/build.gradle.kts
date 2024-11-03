import com.google.cloud.tools.jib.gradle.JibExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("com.google.cloud.tools.jib")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinJvmCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val springDocVersion: String by project

    implementation(project(":spring-template-core-application"))
    implementation(project(":spring-template-core-domain"))
    implementation(project(":spring-template-secondary-mysql"))
    implementation(project(":spring-template-secondary-mongo"))
    implementation(project(":spring-template-secondary-kafka"))
    implementation(project(":spring-template-secondary-s3"))
    implementation(project(":spring-template-secondary-redis"))

    implementation("org.springdoc:springdoc-openapi-ui:$springDocVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springDocVersion")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

val profile: String? by project
val serviceName: String? by project

tasks {
    configure<JibExtension> {
        from {
            image = "amazoncorretto:17-alpine-jdk"
        }

        to {
            image = "biuea3866/${serviceName}"
        }

        container {
            jvmFlags = listOf(
                "-server",
                "-Xms$2g",
                "-Xmx$2g",
                "-XX:MaxMetaspaceSize=512m",
                "-XX:+UseContainerSupport",
                "-XX:MaxGCPauseMillis=200",
                "-XX:+ParallelRefProcEnabled",
                "-XX:-ResizePLAB"
            )

            environment = mapOf(
                "SPRING_PROFILES_ACTIVE" to (profile ?: "dev"),
            )
            ports = listOf("8080")
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}
