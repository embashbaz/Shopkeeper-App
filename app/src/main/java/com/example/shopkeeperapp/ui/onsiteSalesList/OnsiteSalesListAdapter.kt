package com.example.shopkeeperapp.ui.onsiteSalesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.Cart

class OnsiteSalesListAdapter (onClick: (Cart) -> Unit): RecyclerView.Adapter<OnsiteSalesListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Cart>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = allItems.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount()= allItems.size

    fun setData(items: ArrayList<Cart>){
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (Cart) -> Unit) : RecyclerView.ViewHolder(itemView){

        lateinit var mItem: Cart
        init{
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }
        fun bind(item: Cart){
            mItem = item

            val cardDateCreated = itemView.findViewById<TextView>(R.id.date_created_txt)
            val cartStatus = itemView.findViewById<TextView>(R.id.status_expense_list)
            val cartTotalprice = itemView.findViewById<TextView>(R.id.price_expense_list)

            cardDateCreated.setText(item.dateCreated)
            cartStatus.setText(item.status.toString())
            cartTotalprice.setText(item.totalPrice.toString())



        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Cart) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.order_item, parent, false)

                return ViewHolder(view, onClick)
            }

        }

    }
}