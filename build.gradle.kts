//import org.jetbrains.kotlin.cli.jvm.compiler.findMainClass
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//
//plugins {
//    id("org.springframework.boot") version "2.4.2"
//    id("io.spring.dependency-management") version "1.0.11.RELEASE"
//    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.30-RC"
//    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.30-RC"
//    kotlin("jvm") version "1.4.21"
//    kotlin("plugin.spring") version "1.4.21"
//    kotlin("plugin.jpa") version "1.4.21"
//    kotlin("kapt") version "1.4.21"
//    application
//}
//
//allprojects {
//    apply(plugin = "org.jetbrains.kotlin.jvm")
//    apply(plugin = "org.springframework.boot")
//    apply(plugin = "io.spring.dependency-management")
//    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
//    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
//    apply(plugin = "org.jetbrains.kotlin.kapt")
//
//    allOpen {
//        annotation("javax.persistence.Entity")
//    }
//
//    noArg {
//        annotation("javax.persistence.Entity")
//    }
//    group = "com.catshi"
//    version = "0.0.1-SNAPSHOT"
//    java.sourceCompatibility = JavaVersion.VERSION_11
//
//    configurations {
//        compileOnly {
//            extendsFrom(configurations.annotationProcessor.get())
//        }
//    }
//
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
//        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//        implementation("org.springframework.boot:spring-boot-starter-jdbc")
//        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
//        implementation("org.jetbrains.kotlin:kotlin-reflect")
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//        implementation("org.springframework.boot:spring-boot-starter-web")
//        implementation("org.junit.jupiter:junit-jupiter:5.4.2")
//        implementation("org.junit.jupiter:junit-jupiter:5.4.2")
//        implementation("com.querydsl:querydsl-jpa")
//        implementation("com.querydsl:querydsl-apt::jpa")
//        kapt("com.querydsl:querydsl-apt::jpa")
//        compileOnly("com.google.code.gson:gson")
//        compileOnly("org.projectlombok:lombok")
//        developmentOnly("org.springframework.boot:spring-boot-devtools")
//        runtimeOnly("com.h2database:h2")
//        annotationProcessor("org.projectlombok:lombok")
//        testImplementation("org.springframework.boot:spring-boot-starter-test")
//    }
//
//    tasks.withType<KotlinCompile> {
//        kotlinOptions {
//            freeCompilerArgs = listOf("-Xjsr305=strict")
//            jvmTarget = "11"
//        }
//    }
//}
//
//project(":bob") {
//    dependencies {
//        implementation(project(":core"))
//    }
//}
//
//project(":nuguri") {
//    dependencies {
//        implementation(project(":core"))
//    }
//}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    freeCompilerArgs = listOf("-Xinline-classes")
//}