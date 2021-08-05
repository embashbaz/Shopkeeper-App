package com.example.shopkeeperapp.ui.ordersList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.showStatusValue

class OrderListAdapter (onClick: (Order) -> Unit): RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Order>()



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

    fun setData(items: ArrayList<Order>){
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (Order) -> Unit) : RecyclerView.ViewHolder(itemView) {

        lateinit var mItem: Order

        init {
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }

        fun bind(item: Order) {
            mItem = item

            val cartPersonName = itemView.findViewById<TextView>(R.id.person_txt)
            val cardDateCreated = itemView.findViewById<TextView>(R.id.date_created_txt)
            val cartStatus = itemView.findViewById<TextView>(R.id.status_expense_list)
            val cartTotalprice = itemView.findViewById<TextView>(R.id.price_expense_list)

            cartPersonName.setText(item.userName)
            cartStatus.setText(item.cart?.status?.let { showStatusValue(it) })
            cartTotalprice.setText(item.cart?.totalPrice.toString())
            cardDateCreated.setText(item.cart?.dateCreated)


        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Order) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.order_item, parent, false)

                return ViewHolder(view, onClick)
            }

        }
    }

    }