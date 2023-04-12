import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.20"
	kotlin("plugin.spring") version "1.8.20"
}

group = "com.talk"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
	implementation(platform("io.kotest:kotest-bom:5.5.5"))

	implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:2.2.1.RELEASE")

	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
	implementation("org.springdoc:springdoc-openapi-javadoc:1.6.0")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.0")
	implementation("com.github.therapi:therapi-runtime-javadoc:0.15.0")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	runtimeOnly("com.nimbusds:oauth2-oidc-sdk:10.7")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.kotest:kotest-runner-junit5")
	testImplementation("io.kotest:kotest-assertions-core")
	testImplementation("io.kotest:kotest-property")
	testImplementation("org.testcontainers:postgresql:1.17.6")
	testImplementation("org.testcontainers:testcontainers:1.17.6")
	testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
	val dockerUsername = System.getenv("DOCKER_USERNAME")
	val imageName = System.getenv("IMAGE_NAME")
	this.imageName.set(imageName)
	publish.set(true)
	docker {
		publishRegistry {
			username.set(dockerUsername)
			password.set(System.getenv("DOCKER_PASSWORD"))
		}
	}
}

openApi {
	customBootRun {
		args.add("--spring.profiles.active=oas")
	}
}
