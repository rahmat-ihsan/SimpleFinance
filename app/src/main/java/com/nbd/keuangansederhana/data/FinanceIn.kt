package com.nbd.keuangansederhana.data

import android.content.Context
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.text.SimpleDateFormat
import java.util.*


@DatabaseTable
public class FinanceIn {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    var TransactionID: Int = 0
    @DatabaseField
    var TransactionNumber = ""
    @DatabaseField
    var TransactionDate = ""
    @DatabaseField
    var TransactionTime = ""
    @DatabaseField
    var ReceivedFrom = ""
    @DatabaseField
    var Note = ""
    @DatabaseField
    var Amount: Double = 0.0


//    private fun validateBeforeSave(context: Context): String {
//        var pesanError = ""
//        if (Note.isEmpty()) {
//            pesanError = pesanError + context.resources.getString(R.string.empty_error_note) + "\r\n"
//        }
//        if (Amount == 0.0) {
//            pesanError = pesanError + context.resources.getString(R.string.empty_error_amount) + "\r\n"
//        }
//        if (ReceivedFrom.isEmpty()) {
//            pesanError = pesanError + context.resources.getString(R.string.empty_error_from) + "\r\n"
//        }
//        if (Amount < 0)
//            pesanError =
//                pesanError + context.resources.getString(R.string.amount) + context.resources.getString(R.string.minus_error) + "\r\n"
//        return pesanError
//    }

    fun insert(context: Context): String {
        var pesanError = ""
//        if (!IsAuto) {
//            pesanError = validateBeforeSave(context)
//            if (!pesanError.isEmpty())
//                return pesanError
//        }
        val date = Calendar.getInstance().time
        val formatLong = SimpleDateFormat("yyyy-MM-dd")
        val formatSort = SimpleDateFormat("yyMMdd")
        val formatTime = SimpleDateFormat("HH:mm")
        val formattedDateLong = formatLong.format(date)
        val formattedDateSort = formatSort.format(date)
        TransactionDate = formattedDateLong
        TransactionTime = formatTime.format(date)
        TransactionNumber = "UM/" + formattedDateSort + "/"
        if (!pesanError.isEmpty())
            return pesanError
        try {
            val dao = DBAdapter(context).getDaortFinanceIn()
            var LastNumber = 0

            val cursor = DBAdapter(context).getReadableDatabase().rawQuery(
                "\tSELECT COALESCE(MAX(CAST(REPLACE(TransactionNumber,'" + TransactionNumber + "','') AS INT)),0) LastNo \n" +
                        "\tFROM FinanceIn s \n" +
                        "\tWHERE s.TransactionDate='" + TransactionDate + "'\n AND s.TransactionNumber LIKE '" + TransactionNumber + "%'\n",
                null
            )
            if (cursor.moveToFirst()) {
                do {
                    LastNumber = cursor.getInt(0)
                } while (cursor.moveToNext())
            }
            //TransactionNumber = TransactionNumber + TransactionID;
            TransactionNumber = TransactionNumber + Integer.toString(LastNumber + 1)
            dao.create(this)
        } catch (ex: Exception) {
            pesanError = pesanError + ex.message + "\r\n"
        }

        return pesanError
    }

    fun update(context: Context): String {
        var pesanError = ""
//        if (!IsAuto) {
//            pesanError = validateBeforeSave(context)
//            if (!pesanError.isEmpty())
//                return pesanError
//        }
        try {
            val dao = DBAdapter(context).getDaortFinanceIn()
            dao.update(this)
        } catch (ex: Exception) {
            pesanError = pesanError + ex.message + "\r\n"
        }

        return pesanError
    }

    fun delete(context: Context): String {
        var pesanError = ""
//        if (!IsAuto) {
//            pesanError = validateBeforeDelete(context)
//            if (!pesanError.isEmpty())
//                return pesanError
//        }
        try {
            val dao = DBAdapter(context).getDaortFinanceIn()
            dao.delete(this)
        } catch (ex: Exception) {
            pesanError = pesanError + ex.message + "\r\n"
        }

        return pesanError
    }

    fun getList(context: Context): MutableList<FinanceIn> {
        val dao = DBAdapter(context).getDaortFinanceIn()
        return dao.queryForAll()
    }

    fun getSaldo(context: Context): Double {
        val dao = DBAdapter(context).getDaortFinanceIn()
        var saldo = 0.0
        for (fi in dao.queryForAll()) {
            saldo += fi.Amount
        }
        return saldo
    }
}