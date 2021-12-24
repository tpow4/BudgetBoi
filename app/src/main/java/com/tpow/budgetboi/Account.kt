package com.tpow.budgetboi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "institution") val institution: String?,
    @ColumnInfo(name = "account_name") val accountName: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "balance") val balance: Double?
)
