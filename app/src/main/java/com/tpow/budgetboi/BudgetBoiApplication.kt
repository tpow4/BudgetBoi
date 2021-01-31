package com.tpow.budgetboi

import android.app.Application

class BudgetBoiApplication : Application() {
    val database by lazy { (AppDatabase.getDatabase(this))}
    val repository by lazy { AccountRepository(database.accountDao) }
}