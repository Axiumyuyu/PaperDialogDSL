plugins {
    kotlin("jvm") version  "2.4.0" 
    id("com.gradleup.shadow") version "9.4.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
}

val withTest = project.hasProperty("addTest")

sourceSets {
    main {
        kotlin {
            // 如果没有传入 -PwithTest，则排除测试相关代码
            if (!withTest) {
                exclude("**/test/**")
                println(">>> 编译模式：生产模式 (已排除测试代码)")
            } else {
                println(">>> 编译模式：测试模式 (包含测试代码)")
            }
        }
    }
}

tasks.named<org.gradle.jvm.tasks.Jar>("jar") {
    if (withTest) {
        // 只有开启了测试模式时，才添加 -test 后缀
        archiveClassifier.set("test")
    } else {
        // 生产模式下，文件名不带任何 classifier
        archiveClassifier.set("")
    }
}

kotlin {
    jvmToolchain(25)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version )
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
