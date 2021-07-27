package com.example.shopkeeperapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.ItemProduct
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class ItemDialog(code: Int, itemProduct: ItemProduct?) : DialogFragment() {

    val mCode = code
    val itemProduct = itemProduct


    var itemName = ""
    var itemPrice = 0.0
    var numberItem = 0.0
    var description = ""
    var totalPrice = 0.0

    lateinit var itemTotal: TextView
    lateinit var saveBt: Button
    lateinit var ignoreBt: Button

    lateinit var itemNameTl : TextInputLayout
    lateinit var itemPriceTl : TextInputLayout
    lateinit var itemNumberTl : TextInputLayout
    lateinit var itemDescriptionTl : TextInputLayout

    var backToOnlineCartDialogListener: BackToOnlineDialogListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.product_dialog, null)
            itemTotal = view.findViewById(R.id.items_total_price)
            saveBt = view.findViewById(R.id.save_item_bt)
            ignoreBt =view.findViewById(R.id.ignore_item_bt)
            itemNameTl = view.findViewById(R.id.item_name_til)
            itemPriceTl = view.findViewById(R.id.item_price)
            itemNumberTl = view.findViewById(R.id.number_item)
            itemDescriptionTl = view.findViewById(R.id.item_description)

            calculateTotal()
            differentCode()



            builder.setView(view)
                               .setNegativeButton("Cancel"
                               ) { dialog, id ->
                                   dismissDialog()
                               }
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getData(): Boolean{

        itemName = itemNameTl.editText?.text.toString()
        itemPrice = itemPriceTl.editText?.text.toString().toDouble()
        numberItem = itemNumberTl.editText?.text.toString().toDouble()
        description = itemDescriptionTl.editText?.text.toString()

        return !itemName.isEmpty() && numberItem>0



    }


    private fun updateData(){
        if(getData()){
            if (itemProduct != null) {
                itemProduct.name = itemName
                itemProduct.price = itemPrice
                itemProduct.totalPriceNum = totalPrice
                itemProduct.quantity = numberItem
                itemProduct.description = description

                if(mCode == 2){

                }else if(mCode == 3){
                    backToOnlineCartDialogListener?.updateItem(itemProduct)
                }
                dialog?.dismiss()


            }

        }

    }

    private fun setData(){
        itemNameTl.editText?.setText(itemProduct?.name)
        itemPriceTl.editText?.setText(itemProduct?.price.toString())
        itemDescriptionTl.editText?.setText(itemProduct?.description)
        itemNumberTl.editText?.setText(itemProduct?.quantity.toString())
        itemTotal.text = itemProduct?.totalPriceNum.toString()
    }

    private fun clearData(){
        itemTotal.text = ""
        itemNameTl.editText?.setText("")
        itemPriceTl.editText?.setText("")
        itemNumberTl.editText?.setText("")
        itemDescriptionTl.editText?.setText("")
    }

    private fun calculateTotal(){
        itemNumberTl.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()){
                    totalPrice = s?.toString().toDouble() * itemPriceTl.editText?.text.toString().toDouble()
                    itemTotal.text = totalPrice.toString()
                }
            }

        })
    }

    fun disableProductName(){
        itemNameTl.editText?.isEnabled = false
    }

    private fun dismissDialog(){
        getDialog()?.cancel()
    }

    private fun deleteData(){
        if(mCode == 2){

        }else if(mCode == 3){
            if (itemProduct != null) {
                backToOnlineCartDialogListener?.deleteItem(itemProduct)
            }
        }
        dialog?.dismiss()

    }

    private fun differentCode(){
        if(mCode ==1){
            saveBt.setOnClickListener {
               // saveData()

            }

            ignoreBt.setOnClickListener{
                clearData()
            }


        }else if(mCode == 2){

            saveBt.setOnClickListener {

                updateData()

            }

            ignoreBt.setOnClickListener{
                deleteData()
            }

        }else if(mCode == 3){
            setData()
            disableProductName()
            saveBt.text = "UPDATE"
            ignoreBt.text = "DELETE"

            saveBt.setOnClickListener {
                updateData()

            }

            ignoreBt.setOnClickListener{
                deleteData()
            }

        }

    }


    interface BackToOnlineDialogListener{
        fun updateItem(itemProduct: ItemProduct)
        fun deleteItem(itemProduct: ItemProduct)


    }

    fun setListener(listener: BackToOnlineDialogListener) {
        backToOnlineCartDialogListener = listener
    }


}