plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "com.pow.faceswap"
	compileSdk = 35
	
	defaultConfig {
		applicationId = "com.pow.faceswap"
		minSdk = 29
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {
	
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	
	implementation(libs.androidx.navigation.compose)
	// Jetpack Compose integration
	implementation(libs.androidx.navigation.compose)
	// Views/Fragments integration
	implementation(libs.androidx.navigation.fragment)
	implementation(libs.androidx.navigation.ui)
	// Feature module support for Fragments
	implementation(libs.androidx.navigation.dynamic.features.fragment)
	
	//ui preview
	implementation(libs.androidx.ui.tooling.preview)
	debugImplementation(libs.androidx.ui.tooling)
	
	
	//Compose MDC BOM
	implementation(libs.androidx.material3)
	
	//Retrofit
	implementation(libs.retrofit)
	//interceptor
	implementation(libs.logging.interceptor)
	//Gson
	implementation(libs.gson)
	//Gson converter
	implementation(libs.converter.gson)
	
	implementation(libs.androidx.camera.core)
	implementation(libs.androidx.camera.compose)
	implementation(libs.androidx.camera.lifecycle)
	implementation(libs.androidx.camera.camera2)
	implementation(libs.accompanist.permissions)
	
	//coil
	implementation(libs.coil.compose)
	
	//Retrofit
	implementation(libs.retrofit)
	//interceptor
	implementation(libs.logging.interceptor)
	//Gson
	implementation(libs.gson)
	//Gson converter
	implementation(libs.converter.gson)
}