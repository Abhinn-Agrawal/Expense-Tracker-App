package com.example.expensetracker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.data.ExpenseDao
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.ExpenseEntity

class UpdateExpenseViewModel(val dao: ExpenseDao): ViewModel() {
    suspend fun updateExpense(expenseEntity: ExpenseEntity):Boolean{
        return try{
            dao.updateExpense(expenseEntity)
            true
        } catch(ex:Throwable){
            false
        }
    }

    suspend fun deleteExpense(expenseEntity: ExpenseEntity):Boolean{
        return try{
            dao.deleteExpense(expenseEntity)
            true
        } catch(ex:Throwable){
            false
        }
    }
}

class UpdateExpenseViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UpdateExpenseViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return UpdateExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}