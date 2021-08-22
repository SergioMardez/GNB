package com.sergiom.gnb.ui.gnbfragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sergiom.gnb.data.entities.GnbTransaction
import com.sergiom.gnb.databinding.RecyclerLayoutBinding

class TransactionAdapter(private val listener: EventItemListener, private val listenerEnabled: Boolean) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var rowIndex = -1

    interface EventItemListener {
        fun onClickedTransaction(transaction: GnbTransaction)
    }

    private val items = ArrayList<GnbTransaction>()

    fun setItems(items: ArrayList<GnbTransaction>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding: RecyclerLayoutBinding = RecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            if (listenerEnabled) {
                listener.onClickedTransaction(items[position])
                rowIndex = holder.adapterPosition
                notifyDataSetChanged()
            }
        }

        if (rowIndex == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e61b17"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }

    class TransactionViewHolder(private val itemBinding: RecyclerLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: GnbTransaction) {
            itemBinding.skuText.text = item.sku
            itemBinding.amountText.text = item.amount.toString()
            itemBinding.currencyText.text = item.currency
        }
    }
}