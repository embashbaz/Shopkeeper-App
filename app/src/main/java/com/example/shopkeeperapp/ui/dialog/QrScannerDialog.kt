package com.example.shopkeeperapp.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.shopkeeperapp.R
import com.google.android.gms.location.LocationServices

class QrScannerDialog: DialogFragment(){

    private lateinit var codeScanner: CodeScanner
    internal lateinit var listener: QrCodeScannerDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.scanner_adapter, null)

            val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
            val activity = requireActivity()
            codeScanner = CodeScanner(activity, scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                   listener.onCodeGotten(it.text)
                    dialog?.dismiss()
                }
            }
            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }

            builder.setView(view)
                /** Add action buttons
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })**/
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setListener(listener: QrCodeScannerDialogListener) {
        this.listener = listener
    }

    interface QrCodeScannerDialogListener {
        fun onCodeGotten(codeQr: String)

    }
}