pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Expenny"

include(":app")
include(":core:common")
include(":core:resources")
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:datastore")
include(":core:database")
include(":feature:welcome")
include(":feature:profilesetup")
include(":feature:currencyunits")
include(":feature:dashboard")
include(":feature:accounts")
include(":feature:currencies")
include(":core:network")
include(":feature:records")
include(":feature:categories")
include(":feature:daterangepicker")
include(":feature:analytics")
include(":feature:settings")
include(":feature:passcode")
include(":feature:institution")
include(":feature:budgets")
