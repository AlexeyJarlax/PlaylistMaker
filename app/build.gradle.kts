plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.21"
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
        targetSdk = 33
        versionCode = 20
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
//БАЗОВЫЕ

    // Расширения Kotlin для работы с Activity.
    implementation("androidx.activity:activity-ktx:1.9.0")

    // Библиотека для работы с API Android.
    implementation("androidx.core:core-ktx:1.13.1")

    // Библиотека для поддержки современного дизайна пользовательского интерфейса.
    implementation("com.google.android.material:material:1.12.0")

    // Библиотека для обеспечения совместимости с новыми возможностями платформы Android на более старых устройствах.
    implementation("androidx.appcompat:appcompat:1.6.1")

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

    // Библиотека для логирования.
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Фреймворк для написания и запуска тестов в Java.
    testImplementation("junit:junit:4.13.2")

    // Библиотека для написания и запуска тестов на Android.
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

// ПРОДВИНУТЫЕ

    // Библиотека для управления зависимостями.
    implementation("io.insert-koin:koin-android:3.3.0")

    // Расширения Kotlin для работы с жизненным циклом компонентов.
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Расширения Kotlin для фрагментов
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    // Компонент ViewPager2 для реализации горизонтальных и вертикальных листалок.
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Jetpack Navigation Component (библиотеки не обновляю, чтобы не получить конфликт с версией Sdk 33)
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.fragment:fragment-ktx:1.5.6")

    // корутин
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
}

