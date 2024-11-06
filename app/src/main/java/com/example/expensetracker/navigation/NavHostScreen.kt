package com.example.expensetracker.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.SharedViewModel
import com.example.expensetracker.update.UpdateScreen
import com.example.expensetracker.addExpense.AddExpenseScreen
import com.example.expensetracker.home.HomeScreen
import com.example.expensetracker.stats.StatsScreen
import com.example.expensetracker.ui.theme.Zinc

@Composable
fun NavHostScreen() {
    val sharedViewModel: SharedViewModel = viewModel()
    val navController = rememberNavController()
    var bottomBarVisibility by remember { mutableStateOf(true) }

    Scaffold(bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = ScreenRoute.HomeScreenRoute.route, icon = R.drawable.ic_home),
                    NavItem(route = ScreenRoute.StatsScreenRoute.route, icon = R.drawable.ic_stats)
                )
            )
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.HomeScreenRoute.route,
            modifier = Modifier.padding(it)
        ) {
            composable(ScreenRoute.HomeScreenRoute.route) {
                bottomBarVisibility = true
                HomeScreen(navController,sharedViewModel)
            }
            composable(ScreenRoute.AddExpenseScreenRoute.route) {
                bottomBarVisibility = false
                AddExpenseScreen(navController)
            }
            composable(ScreenRoute.StatsScreenRoute.route) {
                bottomBarVisibility = true
                StatsScreen(navController)
            }
            composable(ScreenRoute.UpdateScreenRoute.route) {
                bottomBarVisibility = false
                val expense = sharedViewModel.selectedExpense.value
                expense?.let {
                    UpdateScreen(navController, it)
                } ?: run {
                    navController.navigate(ScreenRoute.HomeScreenRoute.route)
                }
            }
        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    // Bottom Navigation Bar
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}