package com.example.happy2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import com.example.happy2.data.FabricEntry
import com.example.happy2.data.getRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    onBack: () -> Unit,
    onGoFilter: () -> Unit,
    selectedLocations: Set<String>
) {
    val context = LocalContext.current
    val repo = getRepository(context)
    var entries by remember { mutableStateOf<List<FabricEntry>>(emptyList()) }
    val scope = rememberCoroutineScope()

    // ✅ 삭제 팝업 상태
    var showDialog by remember { mutableStateOf(false) }
    var targetEntry by remember { mutableStateOf<FabricEntry?>(null) }

    // ✅ Snackbar 상태
    val snackbarHostState = remember { SnackbarHostState() }

    // 정렬 상태
    var sortAscending by remember { mutableStateOf(true) }

    // DB 불러오기
    LaunchedEffect(Unit) {
        scope.launch {
            entries = repo.getAll()
        }
    }

    // ✅ 정렬 + 필터 적용된 리스트
    val displayedEntries = entries
        .filter { selectedLocations.isEmpty() || it.location in selectedLocations }
        .sortedWith(compareBy<FabricEntry> { it.fabricNo }.let {
            if (sortAscending) it else it.reversed()
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 16.dp, end = 16.dp)
    ) {
        // 상단 바
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
            Text(
                text = "원단 위치 리스트",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // ✅ 정렬 / 위치 선택 버튼
        Row {
            Button(onClick = { sortAscending = !sortAscending }) {
                Text(if (sortAscending) "정렬: 오름차순" else "정렬: 내림차순")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onGoFilter) {
                Text("위치 선택")
            }
        }

        Spacer(Modifier.height(16.dp))

        // 테이블 헤더
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text("원단 번호", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            Text("현재 위치", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(48.dp)) // 삭제 아이콘 자리
        }

        // 리스트 (스크롤)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(displayedEntries) { entry ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(entry.fabricNo, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                    Text(entry.location, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                    IconButton(
                        onClick = {
                            targetEntry = entry
                            showDialog = true
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "삭제", tint = Color.Red)
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFBDBDBD))
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }

    // ✅ 삭제 확인 팝업
    if (showDialog && targetEntry != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        repo.delete(targetEntry!!.fabricNo)  // Entry + History 삭제
                        entries = repo.getAll()              // 리스트 갱신
                        showDialog = false

                        // Snackbar 0.5초 표시
                        val job = launch {
                            snackbarHostState.showSnackbar(
                                "원단번호 ${targetEntry!!.fabricNo} 삭제 완료",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                        delay(500)
                        snackbarHostState.currentSnackbarData?.dismiss()
                        job.cancel()
                    }
                }) { Text("예") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("아니오") }
            },
            title = { Text("삭제 확인") },
            text = { Text("원단번호 ${targetEntry!!.fabricNo} 를 삭제하시겠습니까?") }
        )
    }
}
