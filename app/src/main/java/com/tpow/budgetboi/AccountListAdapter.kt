package com.tpow.budgetboi

import android.graphics.Rect
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import java.math.RoundingMode

class AccountListAdapter : ListAdapter<Account, AccountViewHolder>(AccountComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder : AccountViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.institution, current.accountName, current.balance)
    }
}

class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val institutionTextView : MaterialTextView = itemView.findViewById(R.id.institution_text)
    private val accountTextView : MaterialTextView = itemView.findViewById(R.id.account_text)
    private val balanceTextView : MaterialTextView = itemView.findViewById(R.id.balance_text)

    fun bind(institutionText: String?, accountText : String?, balance: Double?)
    {
        institutionTextView.text = institutionText
        accountTextView.text = accountText
        if (balance == null)
        {
            balanceTextView.text = "N/A"
        }
        else
        {
            when {
                balance > 0 -> {
                    balanceTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.positiveGreen))
                }
                balance < 0 -> {
                    balanceTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.negativeRed))
                }
                else -> {
                    balanceTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.design_default_color_on_primary))
                }
            }

            val numberFormat = NumberFormat.getInstance(NumberFormat.CURRENCYSTYLE)
            numberFormat.minimumFractionDigits = 2
            numberFormat.maximumFractionDigits = 2
            numberFormat.currency = Currency.getInstance("USD")
            balanceTextView.text = numberFormat.format(balance)
        }
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

class AccountItemDecoration(private var verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildLayoutPosition(view) == 0)
        {
            outRect.top = verticalSpaceHeight
        }
        outRect.bottom = verticalSpaceHeight
    }
}