package com.example.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun expenseDao():ExpenseDao

    companion object{
        const val DATABASE_NAME = "expense_table"

        @JvmStatic
        fun getDatabase(context: Context):ExpenseDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initBasicData(context)
                }
            }).build()
        }
        fun initBasicData(context:Context){
            CoroutineScope(Dispatchers.IO).launch {
                val dao = getDatabase(context).expenseDao()
                dao.insertExpense(ExpenseEntity(id=1, title = "Salary", amount = 12000.00, date = System.currentTimeMillis(),"Salary","Income"))
                dao.insertExpense(ExpenseEntity(id=2, title = "Netflix", amount = 800.00, date = System.currentTimeMillis(),"Netflix","Expense"))
                dao.insertExpense(ExpenseEntity(id=3, title = "Starbucks", amount = 500.00, date = System.currentTimeMillis(),"Starbucks","Expense"))
                dao.insertExpense(ExpenseEntity(id=4, title = "Upwork", amount = 1200.00, date = System.currentTimeMillis(),"Upwork","Income"))
            }
        }
    }
}
