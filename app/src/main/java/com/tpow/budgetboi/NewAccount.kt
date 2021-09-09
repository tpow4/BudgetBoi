package com.tpow.budgetboi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton

class NewAccount : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = NewAccount()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var editInstitutionView: EditText
    private lateinit var editAccountView: EditText
    private lateinit var submissionButton : MaterialButton
    private val startingBalance = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.new_account_fragment, container, false)

        editInstitutionView = fragmentView.findViewById(R.id.institutionEditText)
        editAccountView = fragmentView.findViewById(R.id.accountEditText)
        submissionButton = fragmentView.findViewById(R.id.new_account_submit)

        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository)).get(AccountViewModel::class.java)
        submissionButton.setOnClickListener { view ->
            var isError = false
            if (editInstitutionView.text.isBlank() || editAccountView.text.isBlank()) {
                isError = true
            }
            else
            {
                val institution = editInstitutionView.text.toString()
                val account = editAccountView.text.toString()
                viewModel.insert(Account(0, account, institution, startingBalance))
            }

            val action = NewAccountDirections.actionNewAccountToOverview(isError)
            view.findNavController().navigate(action)
        }
    }
}