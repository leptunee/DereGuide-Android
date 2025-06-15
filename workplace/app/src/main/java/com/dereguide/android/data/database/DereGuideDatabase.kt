package com.dereguide.android.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.dereguide.android.data.model.*

@Database(
    entities = [
        Card::class,
        Character::class,
        Song::class,
        Event::class,
        Team::class,
        GachaPool::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DereGuideDatabase : RoomDatabase() {
    
    abstract fun cardDao(): CardDao
    abstract fun characterDao(): CharacterDao
    abstract fun songDao(): SongDao
    abstract fun eventDao(): EventDao
    abstract fun teamDao(): TeamDao
    abstract fun gachaPoolDao(): GachaPoolDao
    
    companion object {
        const val DATABASE_NAME = "dereguide_database"
        
        @Volatile
        private var INSTANCE: DereGuideDatabase? = null
        
        fun getDatabase(context: Context): DereGuideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DereGuideDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
