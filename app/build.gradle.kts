plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.21"
//    id("kotlin-kapt") // плагин kotlin-kapt для работы зависимостей kapt("
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pavlov.melody_quest_discover_play_and_playlist"
        minSdk = 29
        targetSdk = 34
        versionCode = 23
        versionName = "2.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // более новая сборка виртуальной машины: 17
        targetCompatibility = JavaVersion.VERSION_17 // более новая сборка виртуальной машины: 17
    }
    kotlinOptions {
        jvmTarget = "17" // более новая сборка виртуальной машины: 17
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
// БАЗОВЫЕ

    // Расширения Kotlin для работы с Activity.
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.activity:activity-ktx:1.9.0")

    // Библиотека для работы с API Android.
    implementation("androidx.core:core-ktx:1.13.1")

    // Библиотека для поддержки современного дизайна пользовательского интерфейса.
    implementation("com.google.android.material:material:1.12.0")

    // Библиотека для обеспечения совместимости с новыми возможностями платформы Android на более старых устройствах.
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Библиотека для создания сложных макетов пользовательского интерфейса.
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Библиотеки для работы с изображениями и их кэширования.
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

//ДАННЫЕ

    // Библиотека для сериализации и десериализации JSON.
    implementation("com.google.code.gson:gson:2.10.1")

    // Библиотека для работы с JSON на Kotlin.
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    // HTTP-клиент для обмена данными с удаленными серверами.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Библиотека для создания функций отправки в Intents с помощью mailto: URI
    implementation("de.cketti.mailto:email-intent-builder:2.0.0")

// ТЕСТИРОВАНИЕ

    // Фреймворк для написания и запуска тестов в Java.
    testImplementation("junit:junit:4.13.2")

    // Библиотека для написания и запуска тестов на Android.
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

// ПРОДВИНУТЫЕ

    // Библиотека для управления зависимостями.
    implementation("io.insert-koin:koin-android:3.3.0")

    // Расширения Kotlin для работы с жизненным циклом компонентов.
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

    // Расширения Kotlin для фрагментов
    implementation("androidx.fragment:fragment-ktx:1.8.1")

    // Компонент ViewPager2 для реализации горизонтальных и вертикальных листалок.
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Jetpack Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.fragment:fragment-ktx:1.8.1")

    // корутин
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Permission для работы с корутин
    implementation("com.markodevcic:peko:3.0.5")

    // библиотека Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // поддержка легаси для ярлычков (надо или нет я хз)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}

