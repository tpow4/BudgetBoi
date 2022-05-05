package com.tpow.budgetboi

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

class ViewAccount : Fragment() {

    private val args: ViewAccountArgs by navArgs()
    private lateinit var viewModel: AccountViewModel
    private lateinit var toolbar : MaterialToolbar
    private lateinit var textView: TextView

    companion object {
        fun newInstance() = ViewAccount()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.view_account_fragment, container, false)
        toolbar = fragmentView.findViewById(R.id.toolbar)
        textView = fragmentView.findViewById(R.id.textView3)

        viewModel = ViewModelProvider(requireActivity(),
            AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository))[AccountViewModel::class.java]

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        val numberFormat = NumberFormat.getInstance(NumberFormat.CURRENCYSTYLE)
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.currency = Currency.getInstance("USD")

        viewModel.getAccountById(args.accountId).observe(viewLifecycleOwner) { account ->
            toolbar.title = account.institution
            toolbar.subtitle = account.accountName
            textView.text = numberFormat.format(account.balance)
        }
    }
}