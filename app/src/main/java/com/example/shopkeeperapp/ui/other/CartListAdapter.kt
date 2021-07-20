package com.example.shopkeeperapp.ui.other

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.ItemProduct

class CartListAdapter (onClick: (ItemProduct) -> Unit): RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<ItemProduct>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount()= allItems.size

    fun setData(items: ArrayList<ItemProduct>){
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (ItemProduct) -> Unit) : RecyclerView.ViewHolder(itemView){

        lateinit var mItem: ItemProduct
        init{
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }
        fun bind(item: ItemProduct){
            mItem = item


            val itemName = itemView.findViewById<TextView>(R.id.item_name_list)
            val unitPrice = itemView.findViewById<TextView>(R.id.unit_price_list)
            val totalPrice = itemView.findViewById<TextView>(R.id.total_items_list)


            itemName.text = mItem.name
            unitPrice.text = mItem.price.toString() + "X" + mItem.quantity
            totalPrice.text = "Ksh"+mItem.totalPriceNum.toString()


        }

        companion object {
            fun from(parent: ViewGroup, onClick: (ItemProduct) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.cart_item, parent, false)

                return ViewHolder(view, onClick)
            }

        }

    }
}