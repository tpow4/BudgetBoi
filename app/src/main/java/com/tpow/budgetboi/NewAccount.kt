package com.tpow.budgetboi

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout

class NewAccount : DialogFragment(), LifecycleObserver {

    companion object {
        fun newInstance() = NewAccount()
    }

    private val startingBalance = 0.0

    private lateinit var toolbar : MaterialToolbar
    private lateinit var viewModel: AccountViewModel
    private lateinit var editInstitutionLayout : TextInputLayout
    private lateinit var editInstitutionText: EditText
    private lateinit var editAccountLayout : TextInputLayout
    private lateinit var editAccountText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.new_account_fragment, container, false)
        toolbar = fragmentView.findViewById(R.id.toolbar)
        editInstitutionText = fragmentView.findViewById(R.id.institutionEditText)
        editAccountText = fragmentView.findViewById(R.id.accountEditText)
        editInstitutionLayout = fragmentView.findViewById(R.id.institutionEditLayout)
        editAccountLayout = fragmentView.findViewById(R.id.accountEditLayout)
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository))[AccountViewModel::class.java]
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
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    if (editInstitutionText.text.isBlank() || editAccountText.text.isBlank()) {
                        editInstitutionLayout.error = getString(R.string.new_account_error)
                        editAccountLayout.error = getString(R.string.new_account_error)
                    }
                    else
                    {
                        val institution = editInstitutionText.text.toString()
                        val account = editAccountText.text.toString()
                        viewModel.insert(Account(0, account, institution, startingBalance))
                        findNavController().navigateUp()
                    }
                    true
                }
                else -> false
            }
        }
    }
}