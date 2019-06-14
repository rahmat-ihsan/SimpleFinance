package com.nbd.keuangansederhana

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nbd.keuangansederhana.data.FinanceIn
import com.nbd.keuangansederhana.financeview.CashBalanceReportFragment
import com.nbd.keuangansederhana.financeview.FinanceInInputFragment
import com.nbd.keuangansederhana.financeview.FinanceInListFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.partial_finance_in_input.*

class MainActivity : AppCompatActivity(),
    FinanceInInputFragment.inputInterface,
    FinanceInListFragment.FinanceInListFragmentInterface {

    var financeInInputFragment = FinanceInInputFragment()
    var financeInListFragment = FinanceInListFragment()
    var cashBalanceReportFragment = CashBalanceReportFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        financeInListFragment = FinanceInListFragment.newInstance(this@MainActivity)
        financeInInputFragment = FinanceInInputFragment.newInstance(this@MainActivity)
        runOnUiThread {
            supportFragmentManager?.apply {
                beginTransaction().replace(R.id.frame, financeInListFragment, "frame").addToBackStack(null).commit()
            }
        }

        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            viewFlipper.displayedChild = 1

            financeInInputFragment = FinanceInInputFragment.newInstance(this@MainActivity)
            supportFragmentManager?.apply {
                beginTransaction().replace(R.id.frame, financeInInputFragment, "frame").commit()
            }
            txtTotal.visibility = View.GONE
            fab.hide()
        }
        refreshTotal()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun financeInInputBackPressed() {
        financeInListFragment = FinanceInListFragment.newInstance(this@MainActivity)
        supportFragmentManager?.apply {
            beginTransaction().replace(R.id.frame, financeInListFragment, "frame").commit()
        }
        fab.show()
        txtTotal.visibility = View.VISIBLE
    }

    override fun financeInSuccessSave() {
        financeInListFragment = FinanceInListFragment.newInstance(this@MainActivity)
        supportFragmentManager?.apply {
            beginTransaction().replace(R.id.frame, financeInListFragment, "frame").commit()
        }
        fab.show()
        txtTotal.visibility = View.VISIBLE
        refreshTotal()
    }

    override fun afterDeleteItem(financeIn: FinanceIn?, position: Int) {
        refreshTotal()
    }

    override fun onEditItem(financeIn: FinanceIn?, position: Int) {
        financeIn?.let {
            financeInInputFragment = FinanceInInputFragment.newInstance(this@MainActivity, it)
            supportFragmentManager?.apply {
                beginTransaction().replace(R.id.frame, financeInInputFragment, "frame").commit()
            }
            fab.show()
            txtTotal.visibility = View.GONE
        }
    }

    fun refreshTotal() {
        txtTotal.setText("Saldo : " + FinanceIn().getSaldo(this@MainActivity).toString())
    }
}
