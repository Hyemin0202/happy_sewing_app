package com.example.happy2.data

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class FabricRepository(private val dao: FabricDao) {
    // 위치 저장/업데이트
    suspend fun upsert(no: String, newLocation: String): Boolean {
        if (no.isBlank()) return false
        val existing = dao.get(no)
        if (existing == null) {
            // 신규 저장
            dao.upsert(FabricEntry(fabricNo = no, location = newLocation))
            dao.insertHistory(FabricHistory(fabricNo = no, location = newLocation))
        } else {
            if (existing.location != newLocation) {
                // 위치 변경 시 기록 추가
                dao.upsert(FabricEntry(fabricNo = no, location = newLocation))
                dao.insertHistory(FabricHistory(fabricNo = no, location = newLocation))
            }
        }
        return true
    }

    suspend fun find(no: String): FabricEntry? = dao.get(no)

    suspend fun getHistory(no: String): List<FabricHistory> = dao.getHistory(no)

    suspend fun delete(no: String) {
        dao.delete(no)
        dao.deleteHistory(no)
    }

    suspend fun exportToFile(context: Context, uri: Uri) {
        val allEntries = getAllEntriesWithHistory()
        val json = Gson().toJson(allEntries)
        context.contentResolver.openOutputStream(uri)?.use { out ->
            out.write(json.toByteArray())
        }
    }

    suspend fun getAllEntriesWithHistory(): List<FabricEntryWithHistory> {
        val allEntries = dao.getAll()
        return allEntries.map { entry ->
            val history = dao.getHistory(entry.fabricNo)
            FabricEntryWithHistory(entry, history)
        }
    }

    suspend fun importFromFile(context: Context, uri: Uri) {
        context.contentResolver.openInputStream(uri)?.use { input ->
            val json = input.bufferedReader().readText()
            val listType = object : TypeToken<List<FabricEntryWithHistory>>() {}.type
            val data: List<FabricEntryWithHistory> = Gson().fromJson(json, listType)

            data.forEach { ewh ->
                dao.upsert(ewh.entry)
                ewh.history.forEach { h ->
                    dao.insertHistory(h)
                }
            }
        }
    }
}

// 전역에서 repository 가져오기
fun getRepository(context: Context): FabricRepository =
    FabricRepository(AppDatabase.getInstance().fabricDao())
