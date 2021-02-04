package com.tpow.budgetboi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.button.MaterialButton

class NewAccount : Fragment() {

    companion object {
        fun newInstance() = NewAccount()
    }

    private lateinit var viewModel: NewAccountViewModel
    private lateinit var editInstitutionView: EditText
    private lateinit var editAccountView: EditText
    private val STARTING_BALANCE = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.new_account_fragment, container, false)
        editInstitutionView = fragmentView.findViewById(R.id.institutionEditText)
        editAccountView = fragmentView.findViewById(R.id.accountEditText)

        val button = fragmentView.findViewById<MaterialButton>(R.id.new_account_submit)
        button.setOnClickListener {
            //Todo: add fragment communication
//            if (TextUtils.isEmpty(editInstitutionView.text) || TextUtils.isEmpty(editAccountView.text)) {
//                setResult(Activity.RESULT_CANCELED, replyIntent)
//            } else {
//                val institution = editInstitutionView.text.toString()
//                val account = editAccountView.text.toString()
//                val accountList = arrayListOf<CharSequence>()
//                accountList.add(institution)
//                accountList.add(account)
//                accountList.add(STARTING_BALANCE.toString())
//                replyIntent.putCharSequenceArrayListExtra(EXTRA_NEW_ACCOUNT, accountList)
//                setResult(Activity.RESULT_OK, replyIntent)
//            }
        }
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(NewAccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}