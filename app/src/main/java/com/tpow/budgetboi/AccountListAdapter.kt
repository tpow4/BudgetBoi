package com.tpow.budgetboi

import android.graphics.Rect
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class AccountListAdapter : ListAdapter<Account, AccountViewHolder>(AccountComparator()) {
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder : AccountViewHolder, position: Int) {
        val current = getItem(position)
        tracker?.let {
            holder.bind(current.institution, current.accountName, current.balance, it.isSelected(position.toLong()))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun clearSelection() {
        tracker?.clearSelection()
    }
}

class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    private val cardView : MaterialCardView = itemView.findViewById(R.id.account_card_view)
    private val institutionTextView : MaterialTextView = itemView.findViewById(R.id.institution_text)
    private val accountTextView : MaterialTextView = itemView.findViewById(R.id.account_text)
    private val balanceTextView : MaterialTextView = itemView.findViewById(R.id.balance_text)

    fun bind(institutionText: String?, accountText : String?, balance: Double?, isActivated: Boolean = false)
    {
        cardView.setOnClickListener{view ->
            view.findNavController().navigate(R.id.editAccount)
        }
        cardView.isActivated = isActivated
        cardView.isChecked = isActivated

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
                    balanceTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.material_on_surface_emphasis_high_type))
                }
            }

            val numberFormat = NumberFormat.getInstance(NumberFormat.CURRENCYSTYLE)
            numberFormat.minimumFractionDigits = 2
            numberFormat.maximumFractionDigits = 2
            numberFormat.currency = Currency.getInstance("USD")
            balanceTextView.text = numberFormat.format(balance)
        }
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
        override fun getPosition(): Int = bindingAdapterPosition
        override fun getSelectionKey(): Long = itemId
    }

    companion object {
        fun create(parent: ViewGroup) : AccountViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
            return AccountViewHolder(view)
        }
    }
}

class AccountComparator : DiffUtil.ItemCallback<Account>()
{
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.uid == newItem.uid
    }
}

class AccountDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>()
{
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null)
        {
            return (recyclerView.getChildViewHolder(view) as AccountViewHolder).getItemDetails()
        }
        return null
    }
}

class AccountItemDecoration(private var verticalSpaceHeight: Int) : RecyclerView.ItemDecoration()
{
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