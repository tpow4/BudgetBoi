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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAccounts(accounts: List<Account>){
        accountDao.deleteAll(accounts)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAccountsById(ids: IntArray): Flow<List<Account>> {
        return accountDao.loadAllByIds(ids)
    }
}