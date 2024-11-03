dependencies {
	val querydslAptVersion: String by project
	val hibernateJpaVersion: String by project
	val hibernateTypesVersion: String by project
	val commonsPoolVersion: String by project
	val mysqlVersion: String by project

	// querydsl + jpa + mysql
	implementation("com.vladmihalcea:hibernate-types-55:$hibernateTypesVersion")
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("org.apache.commons:commons-pool2:$commonsPoolVersion")

	kapt("com.querydsl:querydsl-apt:$querydslAptVersion")
	kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:$hibernateJpaVersion")

	api("org.springframework.boot:spring-boot-starter-data-jpa")

	runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

	implementation(project(":spring-template-core-domain"))
	implementation(project(":spring-template-core-application"))
}

tasks {
	bootJar {
		enabled = false
	}
}