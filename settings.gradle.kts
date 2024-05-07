import java.net.URI
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties
/*apply(from = "secrets.gradle.kts")*/

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = URI("https://maven.pkg.github.com/PokeAPI/pokekotlin")
            credentials {
                username = "EmberHeart"
                /*password = "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"*/
                password = getApiKey()
            }
        }
    }
}

rootProject.name = "Pokedex"
include(":app")



fun getApiKey(): String {
    val fp = File(rootDir.path + "/secrets.properties")


    if (fp.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(fp))
        return properties.getProperty("github_pat")
    }
    else throw FileNotFoundException(fp.absolutePath)
}