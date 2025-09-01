package com.example.happy2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.ImeAction
import com.example.happy2.data.FabricEntry
import com.example.happy2.data.FabricHistory
import com.example.happy2.data.FabricRepository
import com.example.happy2.data.getRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


@Composable
fun SearchScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repo: FabricRepository = getRepository(context)
    val focusManager = LocalFocusManager.current


    var query by remember { mutableStateOf("") }
    var entry by remember { mutableStateOf<FabricEntry?>(null) }
    var history by remember { mutableStateOf<List<FabricHistory>>(emptyList()) }

    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun doSearch() {
        scope.launch {
            val result = repo.find(query.trim())
            entry = result
            history = if (result != null) repo.getHistory(result.fabricNo) else emptyList()
        }
    }

    val borderColor = Color(0xFFA8A8A8) // ✅ 테두리 색
    val cellColor = Color(0xFFF0F0F0)   // ✅ 셀 배경색
    val whiteColor = Color(0xFFFFFFFF)

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(8.dp)
            .background(Color(0xFFF7F7F7))
            .padding(top= 50.dp)
    ) {
        // 검색창
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, borderColor)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("원단 번호 입력") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        doSearch()
                        keyboardController?.hide()   // ✅ 키보드 내려감
                    }
                )
            )

            IconButton(onClick = {
                doSearch()
                keyboardController?.hide()
                //focusManager.clearFocus()
            }) {
                Icon(Icons.Default.Search, contentDescription = "검색")
            }
        }

        Spacer(Modifier.height(12.dp))

        entry?.let { e ->
            // ✅ 상단 정보 표
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, borderColor)
            ) {
                // 원단 번호 행
                Row(
                    Modifier.fillMaxWidth().height(48.dp)
                        .border(0.5.dp, borderColor) // Row 자체에 테두리 → 가로선만 생김
                ) {
                    Box(
                        modifier = Modifier.weight(0.7f).fillMaxHeight()
                            .background(cellColor)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("원단 번호", style = MaterialTheme.typography.titleLarge)
                    }
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight()
                            .background(whiteColor)
                            .padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(e.fabricNo, style = MaterialTheme.typography.titleLarge)
                    }
                    Box(
                        modifier = Modifier.weight(0.5f).fillMaxHeight()
                            .background(whiteColor),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.Red)
                        }
                    }
                }

                // 현재 위치 행
                Row(
                    Modifier.fillMaxWidth().height(48.dp)
                        .border(0.5.dp, borderColor)
                ) {
                    Box(
                        modifier = Modifier.weight(0.7f).fillMaxHeight()
                            .background(cellColor)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("현재 위치", style = MaterialTheme.typography.titleLarge)
                    }
                    Box(
                        modifier = Modifier.weight(1.5f).fillMaxHeight()
                            .background(whiteColor)
                            .padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(e.location, style = MaterialTheme.typography.titleLarge)
                    }

                }
            }

            Spacer(Modifier.height(12.dp))

            // ✅ 위치 변경 기록 테이블 (스크롤 가능)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // 원하는 높이
                    .border(0.5.dp, borderColor)
            ) {
                // 헤더
                item {
                    Row(
                        Modifier.fillMaxWidth().height(48.dp).background(cellColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("위치", style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("변경 날짜", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                // 데이터 행
                items(history) { h ->
                    Row(
                        Modifier.fillMaxWidth().height(48.dp),
                            //.border(0.2.dp, borderColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .background(whiteColor)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(h.location, style = MaterialTheme.typography.bodyLarge)
                        }
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .background(whiteColor)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(h.changedAt),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        } ?: Text("검색 결과 없음", modifier = Modifier.padding(16.dp))

        // 삭제 확인 팝업
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            entry?.fabricNo?.let { repo.delete(it) }
                            entry = null
                            history = emptyList()
                            showDeleteDialog = false
                        }
                    }) { Text("예") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("아니오")
                    }
                },
                title = { Text("삭제 확인") },
                text = { Text("원단 데이터를 삭제할까요?") }
            )
        }
    }
}
