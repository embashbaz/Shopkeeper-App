package com.example.shopkeeperapp.ui.ordersList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.data.ShopProduct
import com.example.shopkeeperapp.ui.productList.ProductListAdapter
import com.example.shopkeeperapp.ui.productList.ProductListViewModel

class OrderListFragment : Fragment() {

    lateinit var orderListRecyclerView: RecyclerView
    lateinit var noDataTxt: TextView
    val uId : String by lazy {  ( activity?.application as ShopKeeperApp).uId }
    lateinit var orderListAdapter: OrderListAdapter

    val orderListViewModel: OrderListViewModel by lazy {
        ViewModelProvider(this).get(OrderListViewModel ::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_order_list, container, false)

        orderListRecyclerView = view.findViewById(R.id.order_list_recycler)
        noDataTxt = view.findViewById(R.id.no_data_order_list)

        noDataTxt = view.findViewById(R.id.no_data_order_list)


        orderListAdapter = OrderListAdapter { order -> seeCart(order) }

        orderListViewModel.getOrderShopProduct(uId)
        orderListViewModel.orderListProduct.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){

                val orders = it as ArrayList
                orders.sortWith{ lhs, rhs ->
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if (lhs.cart?.status!! < rhs.cart!!.status) -1 else if (lhs.cart!!.status > rhs.cart!!.status) 1 else 0
                }
                orderListAdapter.setData(orders)
                orderListAdapter.notifyDataSetChanged()
                orderListRecyclerView.visibility = View.VISIBLE
                noDataTxt.visibility = View.INVISIBLE
            }else{
                orderListRecyclerView.visibility = View.INVISIBLE
                noDataTxt.visibility = View.VISIBLE
            }
        })

        orderListRecyclerView.layoutManager = LinearLayoutManager(activity)
        orderListRecyclerView.adapter = orderListAdapter




        return view
    }

    private fun seeCart(order: Order) {
        val bundle = Bundle()
        bundle.putInt("order_code", 3)
        bundle.putParcelable("order", order)

        this.findNavController().navigate(R.id.action_orderListFragment_to_onlineCartFragment, bundle)

    }


}