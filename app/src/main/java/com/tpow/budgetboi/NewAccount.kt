package com.tpow.budgetboi

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class NewAccount : DialogFragment(), LifecycleObserver {

    companion object {
        fun newInstance() = NewAccount()
    }

    private lateinit var toolbar : MaterialToolbar
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
        toolbar = fragmentView.findViewById(R.id.toolbar)
        editInstitutionView = fragmentView.findViewById(R.id.institutionEditText)
        editAccountView = fragmentView.findViewById(R.id.accountEditText)
        submissionButton = fragmentView.findViewById(R.id.new_account_submit)

        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository))[AccountViewModel::class.java]
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_MaterialComponents)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}