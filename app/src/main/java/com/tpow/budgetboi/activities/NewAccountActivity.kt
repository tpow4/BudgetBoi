package com.tpow.budgetboi.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.tpow.budgetboi.R

class NewAccountActivity : AppCompatActivity() {

    private lateinit var editInstitutionView: EditText
    private lateinit var editAccountView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)
        editInstitutionView = findViewById(R.id.institutionEditText)
        editAccountView = findViewById(R.id.accountEditText)

        val button = findViewById<MaterialButton>(R.id.button)
        button.setOnClickListener {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(editInstitutionView.text) || TextUtils.isEmpty(editAccountView.text))
            {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            else
            {
                val institution = editInstitutionView.text.toString()
                val account = editAccountView.text.toString()
                val accountList = arrayListOf<CharSequence>()
                accountList.add(institution)
                accountList.add(account)
                replyIntent.putCharSequenceArrayListExtra(EXTRA_NEW_ACCOUNT, accountList)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_NEW_ACCOUNT = "com.tpow.budgetboi.ACCOUNT"
    }
}