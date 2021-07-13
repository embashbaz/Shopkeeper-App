package com.example.shopkeeperapp.ui.productList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.data.ShopProduct

class ProductListAdapter (onClick: (ShopProduct) -> Unit): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<ShopProduct>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = allItems.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = allItems.size

    fun setData(items: ArrayList<ShopProduct>) {
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (ShopProduct) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var mItem: ShopProduct

        init {
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }

        fun bind(item: ShopProduct) {
            mItem = item
            val productNameTxt = itemView.findViewById<TextView>(R.id.name_product_list)
            val qrCodeTxt = itemView.findViewById<TextView>(R.id.qr_code_product_list)
            val numberOfProductTxt = itemView.findViewById<TextView>(R.id.number_product_list)
            val priceTxt = itemView.findViewById<TextView>(R.id.price_product_list)
            val productImage = itemView.findViewById<ImageView>(R.id.product_image_product_list)

            productNameTxt.text = item.productName
            qrCodeTxt.text = item.productQrCode.toString()
            numberOfProductTxt.text = item.productQrCode.toString()
            priceTxt.text = item.price.toString()

            Glide.with(itemView).load(item.imageUrl).apply(RequestOptions.circleCropTransform()).into(productImage)
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (ShopProduct) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.product_item, parent, false)

                return ViewHolder(view, onClick)
            }

        }

    }

}