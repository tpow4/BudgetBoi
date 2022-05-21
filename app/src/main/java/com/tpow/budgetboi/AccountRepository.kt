package com.tpow.budgetboi

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class AccountRepository(private val accountDao: AccountDao) {
    val accounts: Flow<List<Account>> = accountDao.getAll()

    fun getAccountById(id: Int): Flow<Account> = accountDao.loadUserById(id).distinctUntilChanged()

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
}