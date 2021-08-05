package com.example.shopkeeperapp.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.ShopKeeper
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.GeoPoint


class RegisterFragment : Fragment(), CoordinateDialog.CoordinateDialogListener {

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
    lateinit var  latLng: LatLng

    val registrationViewModel : RegistrationViewModel by lazy {
        ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }

    val uId : String by lazy {  ( activity?.application as ShopKeeperApp).uId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        setViews(view)

        if (arguments != null){
            disableViews()
            setData()
        }

        registerBt.setOnClickListener{
            saveNewUser()
        }

        getCordinateTxt.setOnClickListener{
            getCoordinate()
        }


        return view
    }

    private fun setData() {

        registrationViewModel.getUserData(uId).observe(viewLifecycleOwner, Observer {
            if(it != null){
                nameTl.editText?.setText(it.name)
                emailTl.editText?.setText(it.email)
                phoneNumberTl.editText?.setText(it.phoneNumber.toString())
                passwordTl.editText?.setText("N/A")
                descriptionTl.editText?.setText(it.more)
                businessAreaTl.editText?.setText(it.buisinessArea)
                cityTl.editText?.setText(it.county)
            }

        })

    }

    private fun disableViews() {
        nameTl.isEnabled = false
        emailTl.isEnabled = false
        phoneNumberTl.isEnabled = false
        passwordTl.isEnabled = false
        confirmPasswordTl.visibility = View.INVISIBLE
        descriptionTl.isEnabled = false
        businessAreaTl.isEnabled = false
        cityTl.isEnabled = false
        getCordinateTxt.isEnabled = false
        registerBt.visibility = View.INVISIBLE
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
        val dialog = CoordinateDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Give Coordinate")
    }

    private fun saveNewUser() {

        if(!nameTl.editText?.text.isNullOrEmpty() || !emailTl.editText?.text.isNullOrEmpty() || !passwordTl.editText?.text.isNullOrEmpty() ||
            !businessAreaTl.editText?.text.isNullOrEmpty()
               || !cityTl.editText?.text.isNullOrEmpty()){
            if(passwordTl.editText?.text.toString() == confirmPasswordTl.editText?.text.toString()){
                    if(!latLng.equals(0.0)){
                        val geo= GeoPoint(latLng.latitude, latLng.longitude)
                        val shopKeeper = ShopKeeper("",emailTl.editText?.text.toString(),nameTl.editText?.text.toString(),
                            geo,phoneNumberTl.editText?.text.toString().toLong(),
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

    override fun onDialogPositiveClick(latLng: com.google.android.gms.maps.model.LatLng) {
        this.latLng = latLng
    }


}