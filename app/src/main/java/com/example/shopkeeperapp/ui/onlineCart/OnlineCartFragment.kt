package com.example.shopkeeperapp.ui.onlineCart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.Cart
import com.example.shopkeeperapp.data.ItemProduct
import com.example.shopkeeperapp.data.Order
import com.example.shopkeeperapp.ui.dialog.ItemDialog
import com.example.shopkeeperapp.ui.ordersList.OrderListViewModel
import com.example.shopkeeperapp.ui.other.CartListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class OnlineCartFragment : Fragment(), ItemDialog.BackToOnlineDialogListener {

    lateinit var dateCreated: TextView
    lateinit var statusCart: TextView
    lateinit var totalCart: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var noData: TextView
    lateinit var checkoutBtOnline: Button

    lateinit var cartAdapter: CartListAdapter
    lateinit var order: Order

    val onlineCartViewModel: OnlineCartViewModel by lazy {
        ViewModelProvider(this).get(OnlineCartViewModel ::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_online_cart, container, false)
        initView(view)
        if(arguments?.getParcelable<Order>("order") != null)
            order = arguments?.getParcelable("order")!!
        setCheckoutButtonBehaviour()

        setData()





        return view
    }

    fun setCheckoutButtonBehaviour(){
        if(order.cart.status == 4){
            checkoutBtOnline.text = "UPDATE"
            checkoutBtOnline.setOnClickListener{
                onlineCartViewModel.updateOrder(order)
            }
        }


        onlineCartViewModel.updatingOrderOutput.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()

        })



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
        checkoutBtOnline = view.findViewById(R.id.online_checkout)

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

    override fun updateItem(mItemProduct: ItemProduct) {
        var items: MutableList<ItemProduct> = order.itemList as MutableList<ItemProduct>
        items[getItemImpl(order.itemList, mItemProduct)] = mItemProduct
        order.itemList = items
        order.cart.status = 4
    }

    override fun deleteItem(itemProduct: ItemProduct) {
        order.itemList.drop(getItemImpl(order.itemList, itemProduct))
        order.cart.status = 4
    }

    private fun getItemImpl(list: List<ItemProduct>, item: ItemProduct): Int {
        list.forEachIndexed { index, it ->
            if (it == item)
                return index
        }
        return -1
    }

}