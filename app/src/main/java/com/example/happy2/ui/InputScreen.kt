package com.example.happy2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.happy2.data.FabricRepository
import com.example.happy2.data.getRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

@Composable
fun InputScreen(onSaved: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val repo: FabricRepository = getRepository(context)

    var fabricNo by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var oldLocation by remember { mutableStateOf("") }
    var newLocation by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(top= 200.dp, start = 30.dp, end = 30.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("원단 번호",
                modifier = Modifier.width(100.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            OutlinedTextField(
                value = fabricNo,
                onValueChange = { fabricNo = it },
                singleLine = true,
                modifier = Modifier.weight(0.6f)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("원단 위치",
                modifier = Modifier.width(100.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal)
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                singleLine = true,
                modifier = Modifier.weight(0.6f)
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End  // ✅ 오른쪽 정렬
        ) {
            Button(
                onClick = {
                    scope.launch {
                        val existing = repo.find(fabricNo.trim())
                        if (existing == null) {
                            repo.upsert(fabricNo.trim(), location.trim())
                            fabricNo = ""
                            location = ""

                            val job = launch {
                                snackbar.showSnackbar(
                                    "저장 완료",
                                    duration = SnackbarDuration.Indefinite
                                )
                            }
                            kotlinx.coroutines.delay(700)
                            snackbar.currentSnackbarData?.dismiss()
                            job.cancel()

                            //onSaved()
                        } else {
                            oldLocation = existing.location
                            newLocation = location.trim()
                            showDialog = true
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("저장",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White)
            }
        }

        Spacer(Modifier.height(12.dp)) // ✅ 저장 버튼과 간격

// 뒤로 버튼 추가
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End  // ✅ 오른쪽 정렬
        ) {
            Button(
                onClick = onBack,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray) // 배경색 약간 차별화
            ) {
                Text("뒤로", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        SnackbarHost(hostState = snackbar)

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            repo.upsert(fabricNo.trim(), newLocation)
                            // ✅ Snackbar 0.5초 표시
                            val job = launch {
                                snackbar.showSnackbar("위치가 수정되었습니다.", duration = SnackbarDuration.Indefinite)
                            }
                            kotlinx.coroutines.delay(500)
                            snackbar.currentSnackbarData?.dismiss()
                            job.cancel()

                            // ✅ 팝업 닫기
                            showDialog = false
                        }
                    }) { Text("예") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        // ✅ 아니오 눌러도 팝업 닫기
                        showDialog = false
                    }) { Text("아니오") }
                },
                title = { Text("위치 변경 확인") },
                text = { Text("원단 $fabricNo 의 위치를 $oldLocation 에서 $newLocation 으로 변경하겠습니까?") }
            )
        }

    }
}
