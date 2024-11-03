dependencies {
	val springKafkaVersion: String by project

	implementation(project(":spring-template-core-application"))
	implementation(project(":spring-template-core-domain"))

	implementation("org.springframework.kafka:spring-kafka:${springKafkaVersion}")
}

tasks {
	bootJar {
		enabled = false
	}
}