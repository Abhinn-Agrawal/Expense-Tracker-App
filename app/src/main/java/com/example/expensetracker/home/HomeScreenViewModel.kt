package com.example.expensetracker.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.ExpenseRepository
import com.example.expensetracker.data.ExpenseDao
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseEntity
import com.example.expensetracker.widget.Utils
import kotlinx.coroutines.launch

class HomeScreenViewModel(val dao: ExpenseDao):ViewModel() {
    private val repository: ExpenseRepository = ExpenseRepository(dao)
    val expenses: LiveData<List<ExpenseEntity>> = repository.getAllExpenses().asLiveData()

    fun getBalance(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Income")
                total += it.amount
            else
                total -= it.amount
        }
        return Utils.formatToDecimalValue(total)
    }

    fun getTotalExpense(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Expense")
                total -= it.amount
        }
        return Utils.formatToDecimalValue(total)
    }

    fun getTotalIncome(list:List<ExpenseEntity>):String{
        var total = 0.00
        list.forEach{
            if(it.type == "Income")
                total += it.amount
        }
        return Utils.formatToDecimalValue(total)
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
