package com.tpow.budgetboi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Overview : Fragment() {

    companion object {
        fun newInstance() = Overview()
    }

    val args: OverviewArgs by navArgs()

    private lateinit var viewModel: AccountViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var toolbar : MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.overview_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.recyclerView)
        toolbar = fragmentView.findViewById(R.id.materialToolbar)

        val addItemButton = fragmentView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addItemButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.newAccount)
        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.EntryError)
        {
            Toast.makeText(context, "Bruh, no text was entered", Toast.LENGTH_LONG). show()
        }


        toolbar.inflateMenu(R.menu.selected_account_menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository)).get(AccountViewModel::class.java)

        val adapter = AccountListAdapter()
        recyclerView.adapter = adapter

        val tracker = SelectionTracker.Builder(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            AccountDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectSingleAnything()
        ).build()

        adapter.tracker = tracker

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(AccountItemDecoration(resources.getDimensionPixelSize(R.dimen.account_vertical_margin)))

        viewModel.allAccounts.observe(viewLifecycleOwner, { accounts ->
            accounts?.let { adapter.submitList(it) }
        })

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (tracker.hasSelection())
                    {
                        val items = tracker.selection.map { adapter.currentList[it.toInt()] }
                        toolbar.isVisible = items.isNotEmpty()

                        //Todo: error catching needed
                        toolbar.title = items[0].accountName
                    }
                    else
                    {
                        toolbar.isVisible = false
                    }
                }
            })
    }


}