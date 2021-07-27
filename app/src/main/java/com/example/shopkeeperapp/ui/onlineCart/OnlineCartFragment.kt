package com.example.shopkeeperapp.ui.onlineCart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.Cart
import com.example.shopkeeperapp.data.ItemProduct
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.ui.other.CartListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class OnlineCartFragment : Fragment() {

    lateinit var typeCart: TextView
    lateinit var dateCreated: TextView
    lateinit var statusCart: TextView
    lateinit var totalCart: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var addItem: FloatingActionButton
    lateinit var noData: TextView

    lateinit var cartAdapter: CartListAdapter
    lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_online_cart, container, false)
        initView(view)
        if(arguments?.getParcelable<Order>("order") != null)
            order = arguments?.getParcelable<Order>("order")!!

        setData()



        return view
    }

    private fun setData() {
        if(!order.itemList.isEmpty()){
            cartAdapter.setData(order.itemList as ArrayList<ItemProduct>)
            calculateToatal(order.itemList)
            recyclerCart.visibility = View.VISIBLE
            noData.visibility = View.INVISIBLE
        }else{
            recyclerCart.visibility = View.INVISIBLE
            noData.visibility = View.VISIBLE
        }


    dateCreated.text = order.cart.dateCreated
    statusCart.text = order.cart.status.toString()
    recyclerCart.layoutManager = LinearLayoutManager(activity)
    recyclerCart.adapter = cartAdapter
    }

    private fun initView(view: View){

        dateCreated = view.findViewById(R.id.online_date_created)
        statusCart = view.findViewById(R.id.online_cart_status)
        totalCart = view.findViewById(R.id.online_cart_total)
        recyclerCart = view.findViewById(R.id.online_cart_recyclerView)
        noData = view.findViewById(R.id.online_no_data_cart)

        cartAdapter = CartListAdapter { itemProduct -> itemDetail(itemProduct) }

    }

    private fun itemDetail(itemProduct: ItemProduct) {

    }

    private fun calculateToatal(it: List<ItemProduct>) {
        var sum = 0.0
        for(item in it ){
            sum = sum + item.totalPriceNum

        }

        totalCart.text = "ksh"+sum
        order.cart.totalPrice = sum
    }

}