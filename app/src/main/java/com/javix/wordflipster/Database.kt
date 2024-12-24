package com.javix.wordflipster

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.games.Game
import com.google.gson.Gson
import java.util.Date

@Database(entities = [ChallengeEntity::class, QuoteEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WordFlipsterDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun quotesDao(): QuotesLevelsDao
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(Migration_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE challenges ADD COLUMN date INTEGER NOT NULL DEFAULT 0")
    }
}
val Migration_2_3 = object: Migration(2,3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE challenges ADD COLUMN gameType TEXT")
    }
}
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // SQL to create the new table
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS quote_levels (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                quote TEXT NOT NULL,
                author TEXT NOT NULL,
                birth TEXT NOT NULL,
                death TEXT NOT NULL,
                others TEXT,
                timeTaken INTEGER,
                date INTEGER NOT NULL,
                gameType TEXT
            )
            """.trimIndent()
        )
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

    @TypeConverter
    fun fromJsonGameType(value: String?): GameType? {
        return value?.let {
            Gson().fromJson(value, GameType::class.java)
        }
    }

    @TypeConverter
    fun toJsonGameType(value: GameType?): String? {
        return Gson().toJson(value)
    }
}
