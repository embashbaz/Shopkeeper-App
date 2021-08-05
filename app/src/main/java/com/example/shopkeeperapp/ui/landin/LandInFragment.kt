package com.example.shopkeeperapp.ui.landin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.shopkeeperapp.R

class LandInFragment : Fragment() {

    lateinit var productListCard: CardView
    lateinit var orderListCart: CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_land_in, container, false)

        productListCard = view.findViewById(R.id.go_product_list_card)
        orderListCart = view.findViewById(R.id.go_to_order_list)

        productListCard.setOnClickListener{
            this.findNavController().navigate(R.id.action_landInFragment_to_productListFragment)
        }

        orderListCart.setOnClickListener{
            this.findNavController().navigate(R.id.action_landInFragment_to_orderListFragment)

        }

        view.findViewById<CardView>(R.id.go_to_profile).setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("code", 2)
            this.findNavController().navigate(R.id.action_landInFragment_to_registerFragment, bundle)
        }



        return  view

    }

}