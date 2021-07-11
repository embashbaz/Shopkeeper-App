package com.example.shopkeeperapp.ui.productDetail

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.ShopProduct
import com.example.shopkeeperapp.ui.dialog.NoticeDialogFragment
import com.google.android.material.textfield.TextInputLayout


class ProductDetailFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener  {

    lateinit var productNameTl: TextInputLayout
    lateinit var productDescriptionTl: TextInputLayout
    lateinit var productPriceTl: TextInputLayout
    lateinit var productQrTl: TextInputLayout
    lateinit var getNumberItemTxt: TextView
    lateinit var productImage: ImageView
    lateinit var scanBarCodeBt : Button
    lateinit var saveProductBt : Button
    lateinit var ignoreProductBt : Button
    var imageBitmap: Bitmap? = null
    var uploadingImage = false

    val REQUEST_IMAGE_CAPTURE = 1
    var imageUrl = ""

    val productDetailsViewModel: ProductDetailsViewModel by lazy {
        ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
    }

    val uId : String by lazy {  ( activity?.application as ShopKeeperApp).uId }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)
        bindViews(view)

        productImage.setOnClickListener{
            dispatchTakePictureIntent()
        }

        checkImageUploadStatus()

        return  view

    }

    private fun checkImageUploadStatus() {
            productDetailsViewModel.pictureUploadOutput.observe(viewLifecycleOwner,{
                Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()
                if(it["status"].equals("Operation finished")){
                    imageUrl = it["value"].toString()
                    uploadingImage = false
                }else if(it["status"].equals("Operation in progress")){
                    uploadingImage = true
                }

            })
    }

    private fun bindViews(view: View){
       productNameTl = view.findViewById(R.id.product_name_tl)
        productDescriptionTl = view.findViewById(R.id.product_description_tl)
        productPriceTl = view.findViewById(R.id.product_price_tl)
        productQrTl = view.findViewById(R.id.product_code_tl)
        getNumberItemTxt = view.findViewById(R.id.set_number_item)
        productImage = view.findViewById(R.id.product_image)
        scanBarCodeBt = view.findViewById(R.id.scan_qr_bt)
        saveProductBt = view.findViewById(R.id.save_product_bt)
        ignoreProductBt = view.findViewById(R.id.ignore_bt)


    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            activity?.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            productImage.setImageBitmap(imageBitmap)
            confirmSavingPicture(imageBitmap!!)

        }
    }

    fun confirmSavingPicture(bitmap: Bitmap){

        val messageText = "Are you sure you want to save this picture?"
        val dialog = NoticeDialogFragment(messageText, "Save")
        dialog.show(parentFragmentManager, "Confirm you want to save picture")

    }

    fun savePicture(){

        var picName: String
        if(!productQrTl.editText?.text.isNullOrEmpty())
            picName = productQrTl.editText?.text.toString()
        else
            picName = productNameTl.editText?.text.toString()

        imageBitmap?.let { productDetailsViewModel.uploadImage(it, uId, picName) }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        savePicture()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

    private fun saveNewProduct(){
        if (checkMandatoryFields()) {
            if (!uploadingImage) {
                val shopProduct = ShopProduct(
                    "",
                    productNameTl.editText?.text.toString(),
                    productQrTl.editText?.text.toString().toLong(),
                    productPriceTl.editText?.text.toString().toDouble(),
                    10.0,
                    imageUrl,
                    productDescriptionTl.editText?.text.toString()
                )
                productDetailsViewModel.saveNewProduct(shopProduct, uId)

            }else{
                Toast.makeText(activity, "Wait for image upload to finish", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(activity, "Make sure that the you have given " +
                    "a product name price and quantity in store", Toast.LENGTH_LONG).show()
        }

    }

    private fun checkMandatoryFields(): Boolean{
        return !productNameTl.editText?.text.isNullOrEmpty() && !productPriceTl.editText?.text.isNullOrEmpty()
    }


}