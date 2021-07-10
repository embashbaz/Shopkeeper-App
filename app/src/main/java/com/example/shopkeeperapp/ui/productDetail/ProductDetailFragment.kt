package com.example.shopkeeperapp.ui.productDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.shopkeeperapp.R
import com.google.android.material.textfield.TextInputLayout


class ProductDetailFragment : Fragment() {

    lateinit var productNameTl: TextInputLayout
    lateinit var productDescriptionTl: TextInputLayout
    lateinit var productPriceTl: TextInputLayout
    lateinit var productQrTl: TextInputLayout
    lateinit var getNumberItemTxt: TextView
    lateinit var productImage: ImageView
    lateinit var scanBarCodeBt : Button
    lateinit var saveProductBt : Button
    lateinit var ignoreProductBt : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)



        return  view

    }

    private fun bindViews(view: View){
       productNameTl = view.findViewById(R.id.product_name_tl)
        productDescriptionTl = view.findViewById(R.id.product_description_tl)
        productPriceTl = view.findViewById(R.id.product_code_tl)
        productQrTl = view.findViewById(R.id.product_price_tl)
        getNumberItemTxt = view.findViewById(R.id.set_number_item)
        productImage = view.findViewById(R.id.product_image)
        scanBarCodeBt = view.findViewById(R.id.scan_qr_bt)
        saveProductBt = view.findViewById(R.id.save_product_bt)
        ignoreProductBt = view.findViewById(R.id.ignore_bt)


    }

}