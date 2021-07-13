package com.example.shopkeeperapp.ui.productList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.ShopProduct

class ProductListFragment : Fragment() {

    lateinit var productListAdapter: ProductListAdapter
    lateinit var productsRecyclerView: RecyclerView
    lateinit var addProductImg: ImageView
    lateinit var noDataTxt: TextView
    val uId : String by lazy {  ( activity?.application as ShopKeeperApp).uId }

    val productListViewModel: ProductListViewModel by lazy {
        ViewModelProvider(this).get(ProductListViewModel ::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_list, container, false)

        productsRecyclerView = view.findViewById(R.id.product_list_recycler)
        addProductImg = view.findViewById(R.id.add_product_img)
        noDataTxt = view.findViewById(R.id.no_data_txt)


        productListAdapter = ProductListAdapter { shopProduct -> moveToProductDetail(shopProduct) }

        productListViewModel.getListShopProduct(uId)
        productListViewModel.shopListProduct.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                productListAdapter.setData(it as ArrayList<ShopProduct>)
                productsRecyclerView.visibility = View.VISIBLE
                noDataTxt.visibility = View.INVISIBLE
            }else{
                productsRecyclerView.visibility = View.INVISIBLE
                noDataTxt.visibility = View.VISIBLE
            }
        })

        addProductImg.setOnClickListener{
            this.findNavController().navigate(R.id.action_productListFragment_to_productDetailFragment)
        }


        return view
    }

    private fun moveToProductDetail(shopProduct: ShopProduct) {

        val bundle = Bundle()
        bundle.putParcelable("product", shopProduct)

        this.findNavController().navigate(R.id.action_productListFragment_to_productDetailFragment, bundle)

    }


}