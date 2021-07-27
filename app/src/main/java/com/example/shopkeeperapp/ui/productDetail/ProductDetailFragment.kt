package com.example.shopkeeperapp.ui.productDetail

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ShopKeeperApp
import com.example.shopkeeperapp.data.ShopProduct
import com.example.shopkeeperapp.ui.dialog.NoticeDialogFragment
import com.example.shopkeeperapp.ui.dialog.QrScannerDialog
import com.google.android.material.textfield.TextInputLayout
import java.lang.System.load


class ProductDetailFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener,
    ItemsQuantityDialog.ItemsQuantityDialogListener, QrScannerDialog.QrCodeScannerDialogListener {

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
    var imageUrl = ""

    var passedProduct: ShopProduct? = null

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
        checkDataPassed()


        checkImageUploadStatus()

        return  view

    }

    private fun checkDataPassed(){
        if(passedProduct == null){
            onViewClickedNewProduct()
        }
        else{
            changeViewsBehaviour()
            setDataToView()
            onViewClickedUpdateProduct()

        }

    }

    private fun onViewClickedNewProduct(){
        productImage.setOnClickListener{
            // getContent.launch(photoURI)
            uploadingImage = false
            dispatchTakePictureIntent()
        }

        saveProductBt.setOnClickListener{
            saveNewProduct()
        }

        scanBarCodeBt.setOnClickListener{
            scanCode()
        }

        ignoreProductBt.setOnClickListener{
            setViewsToEmpty()
        }

        getNumberItemTxt.setOnClickListener{
            openItemsQuantity()

        }
    }

    private fun onViewClickedUpdateProduct() {
        productImage.setOnClickListener{
            // getContent.launch(photoURI)
            dispatchTakePictureIntent()
        }

        saveProductBt.setOnClickListener{
            updateProduct()
        }

        ignoreProductBt.setOnClickListener{
            deleteDocument()
        }

        getNumberItemTxt.setOnClickListener{
            openItemsQuantity()

        }
    }

    private fun deleteDocument() {
        productDetailsViewModel.deleteProduct(uId, passedProduct!!.docId)
        productDetailsViewModel.deletingProductOutput.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()
            if (it["status"] == "success" ){
                setViewsToEmpty()
                passedProduct = null
                //onCreate(null)
            }
        })
    }

    private fun updateProduct() {
        var newNumItem = numberItems + passedProduct?.itemQuantity!!
        var newDescr= ""
        var newPrice = 0.0
        var newUrl = ""

        if(!productPriceTl.editText?.text.toString().isNullOrEmpty()){
            newPrice = productPriceTl.editText?.text.toString().toDouble()
        }else{
            newPrice = passedProduct!!.price
        }

        if(!productDescriptionTl.editText?.text.toString().isNullOrEmpty()){
            newDescr = productDescriptionTl.editText?.text.toString()

        }else{
            newDescr = passedProduct!!.description
        }

        if(!imageUrl.isNullOrEmpty()){
            uploadingImage = true
            newUrl = imageUrl
        }else{
            newUrl = passedProduct!!.imageUrl
        }

        passedProduct!!.itemQuantity = newNumItem
        passedProduct!!.description = newDescr
        passedProduct!!.price = newPrice
        passedProduct!!.imageUrl = newUrl

        productDetailsViewModel.updateProduct(uId, passedProduct!!.docId, passedProduct!!)

        productDetailsViewModel.updatingProductOutput.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()
        })


    }

    private fun setDataToView() {
        productNameTl.editText?.setText(passedProduct?.productName)
        productDescriptionTl.editText?.setText(passedProduct?.description)
        productPriceTl.editText?.setText(passedProduct?.price.toString())
        productQrTl.editText?.setText(passedProduct?.productQrCode.toString())
        if(!passedProduct?.imageUrl.isNullOrEmpty())
            view?.let { Glide.with(it).load(passedProduct?.imageUrl).into(productImage) }
    }

    private fun changeViewsBehaviour() {
        productNameTl.editText?.isEnabled = false
        productQrTl.editText?.isEnabled = false
        getNumberItemTxt.setText("Number of items added")
        scanBarCodeBt.isEnabled = false
        saveProductBt.setText("Update")
        ignoreProductBt.setText("Delete")

    }

    private fun checkImageUploadStatus() {
            productDetailsViewModel.pictureUploadOutput.observe(viewLifecycleOwner,{
                Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()
                if(it["status"].equals("Operation finished")){
                    imageUrl = it["value"].toString()
                    uploadingImage = true
                }else if(it["status"].equals("Operation in progress")){
                    uploadingImage = false
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            productImage.setImageBitmap(imageBitmap)
           confirmSavingPicture(imageBitmap!!)

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
       checkImageUploadStatus()
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
            if (uploadingImage) {
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
                productDetailsViewModel.addingProductOutput.observe(viewLifecycleOwner, {

                    Toast.makeText(activity, it["status"] +" with value "+ it["value"], Toast.LENGTH_SHORT).show()
                    if (it["status"] == "success" ){
                        setViewsToEmpty()
                    }

                })

            }else{
                Toast.makeText(activity, "Wait for image upload to finish", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(activity, "Make sure that the you have given " +
                    "a product name price and quantity in store", Toast.LENGTH_LONG).show()
        }

    }

    private fun setViewsToEmpty() {
        productNameTl.editText?.setText("")
        productDescriptionTl.editText?.setText("")
        productPriceTl.editText?.setText("")
        productQrTl.editText?.setText("")
        productImage.setImageDrawable()

    }

    private fun checkMandatoryFields(): Boolean{
        return !productNameTl.editText?.text.isNullOrEmpty() && !productPriceTl.editText?.text.isNullOrEmpty()
    }

    private fun scanCode(){

        val dialog = QrScannerDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Scan code")

    }

    override fun onCodeGotten(codeQr: String) {
        productQrTl.editText?.setText(codeQr)
    }


}