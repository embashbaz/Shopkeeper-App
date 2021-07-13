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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_land_in, container, false)

        productListCard = view.findViewById(R.id.go_product_list_card)
        productListCard.setOnClickListener{
            this.findNavController().navigate(R.id.action_landInFragment_to_productListFragment)
        }



        return  view

    }

}