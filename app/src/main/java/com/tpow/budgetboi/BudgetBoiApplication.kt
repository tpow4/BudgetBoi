package com.tpow.budgetboi

import android.app.Application
import com.google.android.material.color.DynamicColors

class BudgetBoiApplication : Application() {
    val database by lazy { (AppDatabase.getDatabase(this))}
    val repository by lazy { AccountRepository(database.accountDao) }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}