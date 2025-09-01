package com.example.happy2.data

import androidx.room.*

@Dao
interface FabricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: FabricEntry)

    @Query("SELECT * FROM fabric_location WHERE fabricNo = :no LIMIT 1")
    suspend fun get(no: String): FabricEntry?

    @Insert
    suspend fun insertHistory(history: FabricHistory)

    @Query("SELECT * FROM fabric_history WHERE fabricNo = :no ORDER BY changedAt DESC")
    suspend fun getHistory(no: String): List<FabricHistory>

    // ✅ 원단 데이터 삭제
    @Query("DELETE FROM fabric_location WHERE fabricNo = :no")
    suspend fun delete(no: String)

    @Query("DELETE FROM fabric_history WHERE fabricNo = :no")
    suspend fun deleteHistory(no: String)

    // ✅ 모든 원단 데이터 조회
    @Query("SELECT * FROM fabric_location")
    suspend fun getAll(): List<FabricEntry>

    // ✅ 특정 원단 번호 조회 (find 용도)
    @Query("SELECT * FROM fabric_location WHERE fabricNo = :no LIMIT 1")
    suspend fun find(no: String): FabricEntry?
}