package com.example.happy2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.happy2.R
import com.example.happy2.data.getRepository
import kotlinx.coroutines.launch

@Composable
fun FilterScreen(
    onBack: () -> Unit,
    onApply: (Set<String>) -> Unit,
    initialSelected: Set<String> = emptySet()   // ✅ 추가
) {
    val context = LocalContext.current
    val repo = getRepository(context)
    var allLocations by remember { mutableStateOf<List<String>>(emptyList()) }
    var selected by remember { mutableStateOf(initialSelected) }  // ✅ 기존 선택 유지
    val scope = rememberCoroutineScope()

    // ✅ DB에서 위치 목록 불러오기
    LaunchedEffect(Unit) {
        scope.launch {
            val entries = repo.getAll()
            allLocations = entries.map { it.location }.distinct()
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { onApply(selected) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
            ) {
                Text("적용",  style = MaterialTheme.typography.titleLarge,)
            }
        },
        floatingActionButtonPosition = FabPosition.Center, // 가운데 정렬
        contentWindowInsets = WindowInsets(0, 0, 0, 40) // FAB 패딩 조정
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 상단 바
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                }
                Text(
                    "위치 선택",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // ✅ 전체 선택 / 해제 버튼 (헤더에 고정)
            Row {
                Button(
                    onClick = { selected = allLocations.toSet() },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_check_box_24), // ✅ 직접 추가한 아이콘
                        contentDescription = "리스트",
                        tint = Color.White // 흑백 벡터면 색상을 입힐 수 있어요
                    )
                    Text("전체 선택")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { selected = emptySet() },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_check_box_outline_blank_24), // ✅ 직접 추가한 아이콘
                        contentDescription = "리스트",
                        tint = Color.White // 흑백 벡터면 색상을 입힐 수 있어요
                    )
                    Text("전체 해제")
                }
            }

            Spacer(Modifier.height(16.dp))

            // ✅ 위치 리스트 (스크롤 가능)
            LazyColumn(
                modifier = Modifier.weight(1f) // 남는 공간 전부 차지 → 스크롤 가능
            ) {
                items(allLocations) { loc ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = loc in selected,
                            onCheckedChange = { checked ->
                                selected = if (checked) selected + loc else selected - loc
                            }
                        )
                        Text(loc, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}