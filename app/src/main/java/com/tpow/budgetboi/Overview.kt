package com.tpow.budgetboi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Overview : Fragment() {

    companion object {
        fun newInstance() = Overview()
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.overview_fragment, container, false)
        val button = fragmentView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener { view ->
            view.findNavController().navigate(R.id.newAccount)
        }
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this, AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository)).get(AccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}