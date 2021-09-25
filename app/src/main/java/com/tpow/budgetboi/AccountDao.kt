package com.tpow.budgetboi

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * from Account")
    fun getAll(): Flow<List<Account>>

    @Query("SELECT * FROM Account WHERE uid IN (:accountIds)")
    fun loadAllByIds(accountIds: IntArray): Flow<List<Account>>

    @Query("SELECT * FROM Account WHERE institution LIKE :institution AND account_name LIKE :account LIMIT 1")
    fun findByName(institution: String, account: String) : Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg accounts: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account:Account)
}
