package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensetracker.data.ExpenseEntity

class SharedViewModel : ViewModel() {
    private val _selectedExpense = MutableLiveData<ExpenseEntity?>()
    val selectedExpense: LiveData<ExpenseEntity?> get() = _selectedExpense

    fun selectedExpense(expense: ExpenseEntity) {
        _selectedExpense.value = expense
    }

    fun clearSelectedExpense() {
        _selectedExpense.value = null
    }
}
