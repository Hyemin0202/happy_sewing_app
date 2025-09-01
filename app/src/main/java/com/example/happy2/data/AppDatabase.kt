package com.example.happy2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ✅ 두 개의 테이블(FabricEntry, FabricHistory)을 관리
@Database(
    entities = [FabricEntry::class, FabricHistory::class],
    version = 1,
    exportSchema = false   // 경고 방지
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fabricDao(): FabricDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "happy2.db"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
        }

        fun getInstance(): AppDatabase =
            INSTANCE ?: error("AppDatabase is not initialized. Call init(context) first.")
    }
}
