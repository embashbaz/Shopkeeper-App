package com.example.shopkeeperapp.ui.registration

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.shopkeeperapp.R
import com.example.shopkeeperapp.ui.dialog.NoticeDialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputLayout
import com.google.android.gms.maps.model.LatLng

class CoordinateDialog() : DialogFragment(){

    internal lateinit var listener: CoordinateDialogListener

    lateinit var lantitudeTl: TextInputLayout
    lateinit var longitudeTl: TextInputLayout
    lateinit var fusedLocation: FusedLocationProviderClient
    lateinit var  latLng: LatLng

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.coordinate_dialog, null)
            fusedLocation = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!

            lantitudeTl = view.findViewById(R.id.latitude_tl)
            longitudeTl = view.findViewById(R.id.longitude_tl)
            getCoordinate()

            builder.setView(view)
                // Add action buttons
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(setDataTo())
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getCoordinate(){

        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocation.lastLocation.addOnSuccessListener {
            lantitudeTl.editText?.setText(it.latitude.toString())
            longitudeTl.editText?.setText(it.longitude.toString())

        }
    }

    private fun setDataTo() : LatLng{
        if (!lantitudeTl.editText?.text.isNullOrEmpty() && !longitudeTl.editText?.text.isNullOrEmpty() ){

              return  LatLng(lantitudeTl.editText?.text.toString().toDouble(), longitudeTl.editText?.text.toString().toDouble())
            //registrationViewModel.latLng = latLng

        }else{
            return LatLng(0.0, 0.0)
        }

    }

    fun setListener(listener:  CoordinateDialogListener ) {
        this.listener = listener
    }

    interface CoordinateDialogListener {
        fun onDialogPositiveClick(latLng: LatLng)

    }




}