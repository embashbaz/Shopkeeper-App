package com.example.shopkeeperapp.ui.landin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.Repository
import com.google.firebase.auth.FirebaseAuth

class LandInFragment : Fragment() {

    lateinit var productListCard: CardView
    lateinit var orderListCart: CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true)

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

        view.findViewById<CardView>(R.id.go_to_performance).setOnClickListener{

            this.findNavController().navigate(R.id.action_landInFragment_to_performanceFragment)
        }



        return  view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.landin_menu, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout_menu){
            val repository = Repository()
            repository.logOut()
            (activity?.application as ShopKeeperApp).uId = ""
            this.findNavController().navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }

}