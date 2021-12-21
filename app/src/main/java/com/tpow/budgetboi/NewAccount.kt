package com.tpow.budgetboi

import android.app.Dialog
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

class NewAccount : DialogFragment(), LifecycleObserver {

    companion object {
        fun newInstance() = NewAccount()
    }

    private val startingBalance = 0.0

    private lateinit var toolbar : MaterialToolbar
    private lateinit var viewModel: AccountViewModel
    private lateinit var editInstitutionView: EditText
    private lateinit var editAccountView: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.new_account_fragment, container, false)
        toolbar = fragmentView.findViewById(R.id.toolbar)
        editInstitutionView = fragmentView.findViewById(R.id.institutionEditText)
        editAccountView = fragmentView.findViewById(R.id.accountEditText)
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository))[AccountViewModel::class.java]
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
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
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
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
    }
}