package com.example.shopkeeperapp.ui.productDetail

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.ShopProduct
import com.example.shopkeeperapp.ui.dialog.NoticeDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator

class ProductDetailFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener,
    ItemsQuantityDialog.ItemsQuantityDialogListener {

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

    var unitPrice = 0.0
    var totalPrice = 0.0
    var numberItems = 0.0

    val REQUEST_IMAGE_CAPTURE = 1
    val CUSTOMIZED_REQUEST_CODE = 0x0000ffff

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
           // getContent.launch(photoURI)
            dispatchTakePictureIntent()
        }

        saveProductBt.setOnClickListener{
            saveNewProduct()
        }

        scanBarCodeBt.setOnClickListener{
            scanCode()
        }

        getNumberItemTxt.setOnClickListener{
            openItemsQuantity()

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

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)

        productImage.setImageBitmap(imageBitmap)
        confirmSavingPicture(imageBitmap!!)
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            productImage.setImageBitmap(imageBitmap)
           confirmSavingPicture(imageBitmap!!)

        }else if(requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(activity, "cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("MainActivity", "Scanned")
                    Toast.makeText(activity, "Scanned -> " + result.contents, Toast.LENGTH_SHORT)
                        .show()
                    productQrTl.editText?.setText(result.toString())
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }



    fun confirmSavingPicture(bitmap: Bitmap){

        val messageText = "Are you sure you want to save this picture?"
        val dialog = NoticeDialogFragment(messageText, "Save")
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Confirm you want to save picture")

    }

    fun openItemsQuantity(){

        val dialog = ItemsQuantityDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "give product quantity")

    }

    fun savePicture(){

        var picName: String
        if(!productQrTl.editText?.text.isNullOrEmpty())
            picName = productQrTl.editText?.text.toString()
        else
            picName = productNameTl.editText?.text.toString()

        imageBitmap?.let { productDetailsViewModel.uploadImage(it, uId, picName) }
        productDetailsViewModel.pictureUploadOutput.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it["status"], Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        savePicture()
    }

    override fun onDialogPositiveClick(itemNumber: Double, unitPrices: Double, totalPrices: Double) {
        unitPrice = unitPrices
        totalPrice = totalPrices
        numberItems = itemNumber
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
                    numberItems,
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

    private fun scanCode(){

        val intentIntegrator = IntentIntegrator(activity)

        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.setCameraId(0)
        intentIntegrator.setPrompt("SCAN")
        intentIntegrator.setBarcodeImageEnabled(false)
        intentIntegrator.initiateScan()

    }


}