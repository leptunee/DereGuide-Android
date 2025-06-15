package com.dereguide.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dereguide.android.R
import com.dereguide.android.ui.screens.*
import com.dereguide.android.ui.theme.DereGuideTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(item.title)) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "cards",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("cards") { 
                CardListScreen(navController = navController)
            }
            composable("songs") { 
                SongListScreen(navController = navController)
            }
            composable("characters") { 
                CharacterListScreen(navController = navController)
            }
            composable("team") { 
                TeamEditorScreen(navController = navController)
            }
            composable("more") { 
                MoreScreen(navController = navController)
            }
        }
    }
}

data class BottomNavItem(
    val title: Int,
    val icon: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = R.string.nav_cards,
        icon = R.drawable.ic_cards,
        route = "cards"
    ),
    BottomNavItem(
        title = R.string.nav_songs,
        icon = R.drawable.ic_music,
        route = "songs"
    ),
    BottomNavItem(
        title = R.string.nav_characters,
        icon = R.drawable.ic_person,
        route = "characters"
    ),
    BottomNavItem(
        title = R.string.nav_team,
        icon = R.drawable.ic_group,
        route = "team"
    ),
    BottomNavItem(
        title = R.string.nav_more,
        icon = R.drawable.ic_more,
        route = "more"
    )
)
