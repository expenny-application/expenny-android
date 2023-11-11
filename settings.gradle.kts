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
include(":feature:splash")
include(":feature:welcome")
include(":feature:getstarted")
include(":feature:currencyunits")
include(":feature:dashboard")
include(":feature:accounts")
include(":feature:accountdetails")
include(":feature:currencies")
include(":feature:currencydetails")
include(":core:network")
include(":feature:records")
include(":feature:recorddetails")
include(":feature:categories")
include(":feature:labels")
include(":feature:daterangepicker")
include(":feature:analytics")
include(":feature:labeldetails")
include(":feature:settings")
include(":feature:accountoverview")

include(":feature:passcode")
