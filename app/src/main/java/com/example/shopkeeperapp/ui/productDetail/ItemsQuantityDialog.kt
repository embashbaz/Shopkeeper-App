package com.example.shopkeeperapp.ui.productDetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import com.example.shopkeeperapp.R
import com.google.android.material.textfield.TextInputLayout


class ItemsQuantityDialog: DialogFragment() {
    internal lateinit var listener: ItemsQuantityDialogListener

    lateinit var itemNumberTl: TextInputLayout
    lateinit var unitPriceTl: TextInputLayout
    lateinit var totalPriceTl: TextInputLayout
    var unitPrice = 0.0
    var totalPrice = 0.0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.items_quantity, null)

            unitPriceTl = view.findViewById(R.id.unit_price_items_dialog)
            itemNumberTl = view.findViewById(R.id.number_items_items_dialog)
            totalPriceTl = view.findViewById(R.id.total_spent_items_dialog)


            builder.setView(view)
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->

                        listener.onDialogPositiveClick(itemNumberTl.editText?.text.toString().toDouble(), unitPrice, totalPrice)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(this)
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")


            }



    public interface ItemsQuantityDialogListener {
        fun onDialogPositiveClick(itemNumber: Double, unitPrice: Double, totalPrice: Double)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener

    fun setListener(listener: ItemsQuantityDialogListener) {
        this.listener = listener
    }




    //override fun onAttach(context: Context) {
    //    super.onAttach(context)
        // Verify that the host activity implements the callback interface
   //     try {
            // Instantiate the NoticeDialogListener so we can send events to the host
    //    listener = parentFragmentManager as ItemsQuantityDialogListener
    //    } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
    //        throw ClassCastException((context.toString() +
       //             " must implement NoticeDialogListener"))

     //   }
   // }

    fun calculateTotal(){
        unitPriceTl.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()){
                    totalPrice = s?.toString().toDouble() * itemNumberTl.editText?.text.toString().toDouble()
                    totalPriceTl.editText?.setText(totalPrice.toString())
                }
            }


        })



    }
}