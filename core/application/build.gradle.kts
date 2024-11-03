dependencies {
	implementation(project(":spring-template-core-domain"))
}

tasks {
	bootJar {
		enabled = false
	}
}