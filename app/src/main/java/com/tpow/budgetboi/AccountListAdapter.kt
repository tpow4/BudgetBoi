package com.tpow.budgetboi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class AccountListAdapter : ListAdapter<Account, AccountViewHolder>(AccountComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder : AccountViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.institution, current.accountName)
    }
}

class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val InstitutionTextView : MaterialTextView = itemView.findViewById(R.id.institution_text)
    private val AccountTextView : MaterialTextView = itemView.findViewById(R.id.account_text)

    fun bind(institutionText: String?, accountText : String?)
    {
        InstitutionTextView.text = institutionText
        AccountTextView.text = accountText
    }

    companion object {
        fun create(parent: ViewGroup) : AccountViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
            return AccountViewHolder(view)
        }
    }
}

class AccountComparator : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.institution === newItem.institution && oldItem.accountName === newItem.accountName
    }
}