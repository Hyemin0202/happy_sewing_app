package com.example.happy2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.happy2.data.getRepository

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repo = getRepository(context)
    val scope = rememberCoroutineScope()

    // Export launcher
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                scope.launch { repo.exportToFile(context, uri) }
            }
        }
    )

    // Import launcher
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                scope.launch { repo.importFromFile(context, uri) }
            }
        }
    )

    NavHost(navController, startDestination = "home") {   // ✅ splash → home
        composable("home") {
            HomeScreen(
                onGoSearch = { navController.navigate("search") },
                onGoInput = { navController.navigate("input") },
                onDbImport = { importLauncher.launch(arrayOf("application/json")) },
                onDbExport = { exportLauncher.launch("fabric_db.json") },
                onGoList = { navController.navigate("list") }
            )
        }
        composable("input") {
            InputScreen(
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable("search") {
            SearchScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("list") {
            ListScreen(onBack = { navController.popBackStack() })
        }
    }
}
