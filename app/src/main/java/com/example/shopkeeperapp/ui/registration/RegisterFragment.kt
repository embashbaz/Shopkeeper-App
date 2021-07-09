package com.example.shopkeeperapp.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.ShopKeeper
import com.google.android.material.textfield.TextInputLayout
import com.google.type.LatLng


abstract class RegisterFragment : Fragment() {

    lateinit var nameTl: TextInputLayout
    lateinit var emailTl: TextInputLayout
    lateinit var phoneNumberTl: TextInputLayout
    lateinit var passwordTl: TextInputLayout
    lateinit var confirmPasswordTl: TextInputLayout
    lateinit var descriptionTl: TextInputLayout
    lateinit var businessAreaTl: TextInputLayout
    lateinit var cityTl: TextInputLayout
    lateinit var getCordinateTxt: TextView
    lateinit var registerBt: Button

    val registrationViewModel : RegistrationViewModel by lazy {
        ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        setViews(view)

        registerBt.setOnClickListener{
            saveNewUser()
        }

        getCordinateTxt.setOnClickListener{
            getCoordinate()
        }


        return view
    }



    fun setViews(view: View){
        nameTl = view.findViewById(R.id.name_register)
        emailTl = view.findViewById(R.id.email_register)
        phoneNumberTl = view.findViewById(R.id.phone_register)
        passwordTl = view.findViewById(R.id.password_register)
        confirmPasswordTl = view.findViewById(R.id.confirm_password)
        descriptionTl = view.findViewById(R.id.description_tl)
        businessAreaTl = view.findViewById(R.id.area_business)
        cityTl = view.findViewById(R.id.city_adress)
        getCordinateTxt = view.findViewById(R.id.get_coordinate_txt)
        registerBt = view.findViewById(R.id.register_button)
    }

    private fun getCoordinate(){
        val dialog = CoordinateDialog(registrationViewModel)
        dialog.show(parentFragmentManager, "Give Coordinate")
    }

    private fun saveNewUser() {

        if(!nameTl.editText?.text.isNullOrEmpty() || !emailTl.editText?.text.isNullOrEmpty() || !passwordTl.editText?.text.isNullOrEmpty() ||
            !businessAreaTl.editText?.text.isNullOrEmpty()
               || !cityTl.editText?.text.isNullOrEmpty()){
            if(passwordTl.editText?.text.toString() == confirmPasswordTl.editText?.text.toString()){
                    if(!registrationViewModel.latLng.equals(0.0)){
                        val shopKeeper = ShopKeeper("",emailTl.editText?.text.toString(),nameTl.editText?.text.toString(),
                            registrationViewModel.latLng,phoneNumberTl.editText?.text.toString().toLong(),
                          businessAreaTl.editText?.text.toString(), cityTl.editText?.text.toString(), descriptionTl.editText?.text.toString() )

                        registrationViewModel.signUp(shopKeeper, passwordTl.editText?.text.toString())

                        registrationViewModel.registrationOutput.observe(viewLifecycleOwner,{

                            Toast.makeText(activity, it["status"]+": "+it["value"], Toast.LENGTH_LONG).show()                        })


                    }else{

                        Toast.makeText(activity, "The coordinate of your location were not set", Toast.LENGTH_LONG).show()
                    }


            }else{
                Toast.makeText(activity, "Please make sure the two password are the same", Toast.LENGTH_LONG).show()
            }

        }else {
            Toast.makeText(activity, "Please make sure you fill all the field, phone number and description are not mandatory", Toast.LENGTH_LONG).show()
        }

    }




}