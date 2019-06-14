package com.nbd.keuangansederhana.extension

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface


fun Context.buildAlertDialog(
        title: String = "",
        message: String = "",
        yesButton: String = "",
        noButton: String = "",
        positiveAction: (DialogInterface) -> Unit = {},
        negativeAction: (DialogInterface) -> Unit = {}
): AlertDialog {
    val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)

    if (yesButton.isNotEmpty()) {
        builder.setPositiveButton(yesButton) { dialog, _ ->
            positiveAction.invoke(dialog)
            dialog.dismiss()
        }
    }

    if (noButton.isNotEmpty()) {
        builder.setNegativeButton(noButton) { dialog, _ ->
            negativeAction.invoke(dialog)
            dialog.dismiss()
        }
    }

    return builder.create()
}