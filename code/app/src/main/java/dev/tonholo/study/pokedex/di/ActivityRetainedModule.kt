package dev.tonholo.study.pokedex.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandler
import dev.tonholo.study.pokedex.ui.theme.state.ThemeStateHandlerImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    fun bindsThemeStateHandler(themeStateHandlerImpl: ThemeStateHandlerImpl): ThemeStateHandler
}
