package com.example.happy2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.painterResource
import com.example.happy2.R
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow

@Composable
fun HomeScreen(    onGoSearch: () -> Unit,
                   onGoInput: () -> Unit,
                   onDbImport: () -> Unit,
                   onDbExport: () -> Unit,
                   onGoList: () -> Unit
    ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).background(Color(0xFFF7F7F7)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onGoInput,
            modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, shape = RoundedCornerShape(5.dp), clip = false), // ✅ 그림자
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFFFF),
                contentColor = Color.Black
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_login_24), // ✅ 직접 추가한 아이콘
                contentDescription = "로그인",
                tint = Color.Black  // 흑백 벡터면 색상을 입힐 수 있어요
            )
            Spacer(Modifier.width(8.dp))
            Text("원단 위치 입력", style = MaterialTheme.typography.titleLarge)
        }
        //버튼 사이 spacer
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onGoSearch,
            modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, shape = RoundedCornerShape(12.dp), clip = false), // ✅ 그림자
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFFFF),
                contentColor = Color.Black
            )
        ){
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "원단 위치 검색",
                tint = Color.Black
            )
            Spacer(Modifier.width(8.dp))
            Text("원단 위치 검색", style = MaterialTheme.typography.titleLarge)
        }
        //버튼 사이 spacer
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onGoList,
            modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .shadow(4.dp, shape = RoundedCornerShape(12.dp), clip = false), // ✅ 그림자
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFFFF),
                contentColor = Color.Black
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_format_list_bulleted_24), // ✅ 직접 추가한 아이콘
                contentDescription = "리스트",
                tint = Color.Black  // 흑백 벡터면 색상을 입힐 수 있어요
            )
            Spacer(Modifier.width(8.dp))
            Text("원단 위치 리스트", style = MaterialTheme.typography.titleLarge)
        }

        //버튼 사이 spacer
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onDbImport,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("DB 불러오기")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = onDbExport,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("DB 내보내기")
            }
        }

    }
}
