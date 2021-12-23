package com.tpow.budgetboi

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {
    val allAccounts: LiveData<List<Account>> = repository.accounts.asLiveData()

    fun insert(account: Account) = viewModelScope.launch {
        repository.insert(account)
    }

    fun deleteAccounts(accounts: List<Account>) = viewModelScope.launch {
        repository.deleteAccounts(accounts)
    }
}

class AccountViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}