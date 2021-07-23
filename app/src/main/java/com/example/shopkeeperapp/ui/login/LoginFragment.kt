package com.example.shopkeeperapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout


class LoginFragment : Fragment() {

    lateinit var emailTl: TextInputLayout
    lateinit var passwordTl: TextInputLayout
    lateinit var forgotPwdBt : Button
    lateinit var registerBt : Button
    lateinit var loginBt : Button



    val loginViewModel : LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
       override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =   inflater.inflate(R.layout.fragment_login, container, false)

          goToLandInScreen()

           initiateView(view)
           loginBt.setOnClickListener{
               loginMethod()
           }

        registerBt.setOnClickListener{
           goToRegistrationFgmt()
        }


        return view
    }

    fun goToLandInScreen(){
        if( !((activity?.application as ShopKeeperApp).uId).isNullOrEmpty() ) {

            this.findNavController().navigate(R.id.action_loginFragment_to_landInFragment)

        }
    }

    fun initiateView(view: View){
        emailTl = view.findViewById(R.id.email_login)
        passwordTl = view.findViewById(R.id.password_login)
        forgotPwdBt = view.findViewById(R.id.forgot_password)
        registerBt = view.findViewById(R.id.register_login)
        loginBt = view.findViewById(R.id.login_button)


    }

    fun loginMethod(){
        if (!emailTl.editText?.text.isNullOrEmpty() && !passwordTl.editText?.text.isNullOrEmpty() ) {
            loginViewModel.signUp(
                emailTl.editText?.text.toString(),
                passwordTl.editText?.text.toString()
            )

            loginViewModel.loginOutput.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it["status"] + ": " + it["value"], Toast.LENGTH_LONG)
                    .show()
                if (it["status"] == "success") {
                    (activity?.application as ShopKeeperApp).uId = it["value"].toString()
                    this.findNavController().navigate(R.id.action_loginFragment_to_landInFragment)
                }
            })
        }

    }

    fun goToRegistrationFgmt(){
        this.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

    }


}