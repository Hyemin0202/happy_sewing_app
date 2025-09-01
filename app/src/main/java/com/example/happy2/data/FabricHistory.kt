package com.example.happy2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 변경 기록을 관리하는 테이블
@Entity(tableName = "fabric_history")
data class FabricHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fabricNo: String,
    val location: String,
    val changedAt: Long = System.currentTimeMillis()
)