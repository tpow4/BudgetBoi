package com.tpow.budgetboi.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tpow.budgetboi.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val newAccountActivityRequestCode = 1
    private val accountViewModel: AccountViewModel by viewModels {
        AccountViewModelFactory((application as BudgetBoiApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = AccountListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(AccountItemDecoration(resources.getDimensionPixelSize(R.dimen.account_vertical_margin)))

        accountViewModel.allAccounts.observe(this, Observer { accounts ->
            accounts?.let { adapter.submitList(it) }
        })

        val addItemButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addItemButton.setOnClickListener {
            val intent = Intent(this@MainActivity, NewAccountActivity::class.java)
            startActivityForResult(intent, newAccountActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newAccountActivityRequestCode && resultCode == Activity.RESULT_OK)
        {
            data?.getStringArrayListExtra(NewAccountActivity.EXTRA_NEW_ACCOUNT)?.let {
                //Todo: throw exception for parse error
                val account = Account(0, it[0], it[1], it[2].toDoubleOrNull())
                accountViewModel.insert(account)
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {

        }
        else
        {
            Toast.makeText(applicationContext, "Bruh, no text was entered", Toast.LENGTH_LONG). show()
        }
    }
}