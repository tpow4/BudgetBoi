package com.tpow.budgetboi

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
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

    private lateinit var viewModel: AccountViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var toolbar : MaterialToolbar
    private var actionMode : ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.selected_account_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_settings -> {
                    //Todo: button handling
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            (recyclerView.adapter as AccountListAdapter).clearSelection()
            actionMode = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.overview_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.recyclerView)
        toolbar = fragmentView.findViewById(R.id.toolbar)

        val addItemButton = fragmentView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addItemButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.newAccount)
//            NewAccount().show(childFragmentManager, "NewAccountFragment")
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.inflateMenu(R.menu.selected_account_menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(),
            AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository)).get(AccountViewModel::class.java)

        val adapter = AccountListAdapter()
        recyclerView.adapter = adapter

        val tracker = SelectionTracker.Builder(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            AccountDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
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

                        if(actionMode == null)
                        {
                            actionMode = activity!!.startActionMode(actionModeCallback)
                        }

                        if(items.size == 1)
                        {
                            actionMode?.title = items[0].accountName
                        }
                        else
                        {
                            actionMode?.title = items.size.toString() + " accounts"
                        }
                    }
                    else
                    {
                        actionMode?.finish()
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()

        //clears args after displaying warning. Otherwise, it will display after each fragment backs into overview
        if (OverviewArgs.fromBundle(requireArguments()).entryArg) {
            Toast.makeText(context, "No text was entered for account creation", Toast.LENGTH_LONG). show()
        }
        requireArguments().clear()
    }
}