package com.nbd.keuangansederhana.financeview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbd.keuangansederhana.R
import com.nbd.keuangansederhana.data.FinanceIn
import kotlinx.android.synthetic.main.partial_finance_in_input.*

class FinanceInInputFragment  : Fragment() {

    companion object {
        fun newInstance(listener: inputInterface): FinanceInInputFragment {
            val f = FinanceInInputFragment()
            f.mInterface = listener
            return f
        }
        fun newInstance(listener: inputInterface, financeIn: FinanceIn): FinanceInInputFragment {
            val f = FinanceInInputFragment()
            f.mInterface = listener
            f.currentFinanceIn = financeIn
            f.isEdit = true
            return f
        }
    }

    var currentFinanceIn: FinanceIn = FinanceIn()
    var isEdit = false
    var mInterface: inputInterface? = null
    interface inputInterface {
        fun financeInInputBackPressed()
        fun financeInSuccessSave()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        var layout = inflater.inflate(R.layout.partial_finance_in_input, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isEdit) {
            actvReceivedFrom.setText(currentFinanceIn.ReceivedFrom)
            actvValue.setText(currentFinanceIn.Amount.toString())
            actvNote.setText(currentFinanceIn.Note)
        }
        ivBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mInterface?.apply {
                    financeInInputBackPressed()
                }
            }
        })
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                currentFinanceIn.ReceivedFrom = actvReceivedFrom.text.toString()
                currentFinanceIn.Amount = actvValue.text.toString().toDouble()
                currentFinanceIn.Note = actvNote.text.toString()
                var pe = ""
                context?.let {
                    if (isEdit) {
                        pe = currentFinanceIn.update(it)
                    } else {
                        pe = currentFinanceIn.insert(it)
                    }
                }
                if (pe.isEmpty()) {
                    mInterface?.apply {
                        financeInSuccessSave()
                    }
                }
            }
        })
    }
}