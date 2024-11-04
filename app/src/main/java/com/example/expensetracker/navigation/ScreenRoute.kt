package com.example.expensetracker.navigation

sealed class ScreenRoute(val route:String) {
    data object HomeScreenRoute: ScreenRoute("homescreen")
    data object AddExpenseScreenRoute: ScreenRoute("addexpensescreen")
    data object StatsScreenRoute: ScreenRoute("statsscreen")
    data object UpdateScreenRoute: ScreenRoute("updateexpensescreen")

}