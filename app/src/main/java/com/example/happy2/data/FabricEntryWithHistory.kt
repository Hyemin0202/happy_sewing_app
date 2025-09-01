package com.example.happy2.data

data class FabricEntryWithHistory(
    val entry: FabricEntry,
    val history: List<FabricHistory>
)