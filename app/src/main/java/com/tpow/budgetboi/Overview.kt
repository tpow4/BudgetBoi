package com.tpow.budgetboi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Overview : Fragment() {

    companion object {
        fun newInstance() = Overview()
    }

    val args: OverviewArgs by navArgs()

    private lateinit var viewModel: AccountViewModel
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.overview_fragment, container, false)
         recyclerView = fragmentView.findViewById(R.id.recyclerView)


//        accountViewModel.allAccounts.observe(this, Observer { accounts ->
//            accounts?.let { adapter.submitList(it) }
//        })

        val addItemButton = fragmentView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addItemButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.newAccount)
        }

        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository)).get(AccountViewModel::class.java)
        val adapter = AccountListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(AccountItemDecoration(resources.getDimensionPixelSize(R.dimen.account_vertical_margin)))
        viewModel.allAccounts.observe(viewLifecycleOwner, { accounts ->
            accounts?.let { adapter.submitList(it) }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.EntryError)
        {
            Toast.makeText(context, "Bruh, no text was entered", Toast.LENGTH_LONG). show()
        }
    }

}