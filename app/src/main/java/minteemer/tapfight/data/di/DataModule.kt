package minteemer.tapfight.data.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import minteemer.tapfight.repository.config.GameConfigRepository
import minteemer.tapfight.repository.config.GameConfigRepositoryImpl

/**
 * @author t.valiev
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface DataModule {

    @Binds
    @Reusable
    fun bindGameConfigRepository(impl: GameConfigRepositoryImpl): GameConfigRepository
}
