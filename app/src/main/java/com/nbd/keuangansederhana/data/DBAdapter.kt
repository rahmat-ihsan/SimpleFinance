package com.nbd.keuangansederhana.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

class DBAdapter constructor(context: Context) :
    OrmLiteSqliteOpenHelper(context, databaseName, null, dbversion) {
    companion object {
        val dbversion = 1
        val databaseName = "finance.db"
    }

    private var daortFinanceIn: RuntimeExceptionDao<FinanceIn, Int>? = null

    override fun onCreate(p0: SQLiteDatabase?, p1: ConnectionSource?) {
        try {
            TableUtils.createTable<FinanceIn>(connectionSource, FinanceIn::class.java)
        } catch (ex: Exception) {

        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: ConnectionSource?, p2: Int, p3: Int) {

    }

    fun getDaortFinanceIn(): RuntimeExceptionDao<FinanceIn, Int> {
        if (daortFinanceIn == null) {
            daortFinanceIn =
                getRuntimeExceptionDao<RuntimeExceptionDao<FinanceIn, Int>, FinanceIn>(FinanceIn::class.java)
        }
        return daortFinanceIn!!
    }
}