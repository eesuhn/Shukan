package com.eesuhn.habittracker

//import com.eesuhn.habittracker.feature.widgets.base.WidgetUpdater
import com.eesuhn.habittracker.feature.misc.settings.AppInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideAppInfo() = AppInfo(
        versionName = BuildConfig.VERSION_NAME,
        buildType = BuildConfig.BUILD_TYPE,
        appId = BuildConfig.APPLICATION_ID,
        urlPrivacyPolicy = BuildConfig.URL_PRIVACY_POLICY,
        urlSourceCode = BuildConfig.URL_SOURCE_CODE
    )

    @Provides
    @Singleton
    @AppCoroutineScope
    fun provideAppCoroutineScope() = CoroutineScope(SupervisorJob())
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppCoroutineScope
