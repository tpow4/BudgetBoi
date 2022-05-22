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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Overview : Fragment() {
    private lateinit var viewModel: AccountViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var toolbar : MaterialToolbar
    private var actionMode : ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.overview_fragment, container, false)
        recyclerView = fragmentView.findViewById(R.id.recyclerView)
        toolbar = fragmentView.findViewById(R.id.toolbar)

        val addItemButton = fragmentView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addItemButton.setOnClickListener { view ->
            view.findNavController().navigate(OverviewDirections.actionOverviewToNewAccount())
        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.overview))
        toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    // Navigate to settings screen
                    Toast.makeText(activity, "Hit", Toast.LENGTH_SHORT)
                    findNavController().navigate(OverviewDirections.actionOverviewToSettings())
                    true
                }
                else -> false
            }
        }

        viewModel = ViewModelProvider(requireActivity(),
            AccountViewModelFactory((activity?.application as BudgetBoiApplication).repository))[AccountViewModel::class.java]

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

        viewModel.allAccounts.observe(viewLifecycleOwner) { accounts ->
            accounts?.let { adapter.submitList(it) }
        }

        val actionModeCallback = object : ActionMode.Callback {
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
                    R.id.delete_account -> {
                        //selection tracker needs to be accessed at this scope for some reason
                        val items = tracker.selection.map { adapter.currentList[it.toInt()] }
                        MaterialAlertDialogBuilder(requireActivity())
                            .setMessage(resources.getString(R.string.delete_account_modal_message))
                            .setNeutralButton(resources.getString(R.string.delete_account_modal_cancel)) { dialog, which ->
                                // Respond to neutral button press
                            }
                            .setPositiveButton(resources.getString(R.string.delete_account_modal_accept)) { dialog, which ->
                                // Respond to positive button press
                                viewModel.deleteAccounts(items)
                                mode.finish() // Action picked, so close the CAB
                            }.show()
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
                            actionMode?.title = items.first().accountName
                        }
                        else
                        {
                            actionMode?.title = "${items.size} accounts"
                        }
                    }
                    else
                    {
                        actionMode?.finish()
                    }
                }
            })
    }
}