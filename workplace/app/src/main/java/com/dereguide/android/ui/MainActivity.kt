package com.dereguide.android.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dereguide.android.R
import com.dereguide.android.ui.screens.CardListScreen
import com.dereguide.android.ui.screens.CardDetailScreen
import com.dereguide.android.ui.theme.DereGuideTheme
import com.dereguide.android.debug.NetworkTestUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity 启动")
        
        // 启动网络测试
        NetworkTestUtil.startNetworkTest(this)
        
        setContent {
            DereGuideTheme {
                DereGuideApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DereGuideApp() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "cards",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("cards") { 
                CardListScreen(navController = navController)
            }
            composable("card_detail/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId")?.toIntOrNull() ?: 0
                CardDetailScreen(
                    navController = navController,
                    cardId = cardId
                )
            }
        }
    }
}

