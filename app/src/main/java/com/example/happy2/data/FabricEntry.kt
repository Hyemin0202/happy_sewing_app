package com.example.happy2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 현재 상태를 관리하는 테이블
@Entity(tableName = "fabric_location")
data class FabricEntry(
    @PrimaryKey val fabricNo: String,
    val location: String,
    val updatedAt: Long = System.currentTimeMillis()
)