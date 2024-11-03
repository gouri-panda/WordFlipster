package com.javix.wordflipster

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.Date

@Database(entities = [ChallengeEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WordFlipsterDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var INSTANCE: WordFlipsterDatabase? = null

        fun getDatabase(context: Context): WordFlipsterDatabase {
            // If the INSTANCE is not null, return it, else create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordFlipsterDatabase::class.java,
                    "challenge_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE challenges ADD COLUMN date INTEGER NOT NULL DEFAULT 0")
    }
}




class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
