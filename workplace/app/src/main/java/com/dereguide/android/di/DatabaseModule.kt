package com.dereguide.android.di

import android.content.Context
import androidx.room.Room
import com.dereguide.android.data.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DereGuideDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DereGuideDatabase::class.java,
            DereGuideDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideCardDao(database: DereGuideDatabase): CardDao = database.cardDao()
    
    @Provides
    fun provideCharacterDao(database: DereGuideDatabase): CharacterDao = database.characterDao()
    
    @Provides
    fun provideSongDao(database: DereGuideDatabase): SongDao = database.songDao()
    
    @Provides
    fun provideEventDao(database: DereGuideDatabase): EventDao = database.eventDao()
    
    @Provides
    fun provideTeamDao(database: DereGuideDatabase): TeamDao = database.teamDao()
    
    @Provides
    fun provideGachaPoolDao(database: DereGuideDatabase): GachaPoolDao = database.gachaPoolDao()
}
