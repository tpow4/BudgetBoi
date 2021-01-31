package com.tpow.budgetboi

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountDao: AccountDao) {
    val accounts: Flow<List<Account>> = accountDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(account: Account){
        accountDao.insert(account)
    }
}