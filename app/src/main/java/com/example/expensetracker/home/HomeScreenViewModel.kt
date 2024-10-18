package com.example.expensetracker.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity

class HomeScreenViewModel(dao:ExpenseDao):ViewModel() {
    val expenses = dao.getAllExpenses()

    fun getBalance(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Income")
                total += it.amount
            else
                total -= it.amount
        }
        return "$total"
    }

    fun getTotalExpense(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Expense")
                total -= it.amount
        }
        return "$total"
    }

    fun getTotalIncome(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Income")
                total += it.amount
        }
        return "$total"
    }
    fun getItemIcon(item:ExpenseEntity): Int {
        if(item.category == "Salary"){
            return R.drawable.ic_paypal
        }
        else if(item.category == "Netflix"){
            return R.drawable.ic_netflix
        }
        else if(item.category == "Upwork"){
            return R.drawable.ic_upwork
        }
        else if(item.category == "Starbucks"){
            return R.drawable.ic_starbucks
        }
        else if(item.category == "Youtube"){
            return R.drawable.ic_youtube
        }
        else if(item.type == "Income")
            return R.drawable.ic_income
        return R.drawable.ic_expense
    }
}

class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
