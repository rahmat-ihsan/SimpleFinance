package com.nbd.keuangansederhana.financeview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ArrayAdapter
import com.nbd.keuangansederhana.R
import com.nbd.keuangansederhana.data.FinanceIn
import com.nbd.keuangansederhana.extension.buildAlertDialog
import kotlinx.android.synthetic.main.item_finance_in.view.*
import kotlinx.android.synthetic.main.partial_finance_in_list.*


class FinanceInListFragment : Fragment() {

    companion object {
        fun newInstance(listener: FinanceInListFragmentInterface): FinanceInListFragment {
            val f = FinanceInListFragment()
            f.financeInInterface = listener
            return f
        }
    }

    var adapterListFinanceIn: AdapterListFinanceIn? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var financeInSelected: FinanceIn = FinanceIn()
    var financeInInterface: FinanceInListFragmentInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        var layout = inflater.inflate(R.layout.partial_finance_in_list, container, false)

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapterListFinanceIn = AdapterListFinanceIn(context)
        recyclerViewFinanceIn.adapter = adapterListFinanceIn
        recyclerViewFinanceIn.layoutManager = linearLayoutManager
        registerForContextMenu(recyclerViewFinanceIn)
        adapterListFinanceIn?.setInterface(object : AdapterListFinanceInInterface {
            override fun onClickItem(financeIn: FinanceIn, view: View, position: Int) {
                financeInSelected = financeIn
                view.showContextMenu()
            }
        })

        adapterListFinanceIn?.apply {
            context?.let {
                addAll(FinanceIn().getList(it))
                if (listFinanceIn.size > 0) {
                    recyclerViewFinanceIn.visibility = View.VISIBLE
                    llEmptyList.visibility = View.GONE
                } else {
                    recyclerViewFinanceIn.visibility = View.GONE
                    llEmptyList.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v!!.getId() == R.id.recyclerViewFinanceIn) {
            menu!!.add(0, 1, 0, "Edit")
            menu!!.add(0, 2, 0, "Hapus")
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
//        val info = item.getMenuInfo() as AdapterView.AdapterContextMenuInfo
//        mListener.onSelectedItem(item.getItemId(), itemList.get(info.position))
        if (item!!.itemId == 1) {
            financeInInterface?.apply {
                onEditItem(financeInSelected, adapterListFinanceIn?.listFinanceIn?.indexOf(financeInSelected) ?: -1)
            }
        } else if (item!!.itemId == 2) {
            context?.let {ctx ->
                ctx.buildAlertDialog(
                    title = "Peringatan",
                    yesButton = "Ya",
                    noButton = "Tidak",
                    message = "Apakah anda yakin ingin menghapus?",
                    positiveAction = {
                        val pesanError = financeInSelected.delete(ctx)
                        if (pesanError.isEmpty()) {
                            adapterListFinanceIn?.apply {
                                var pos = listFinanceIn.indexOf(financeInSelected)
                                listFinanceIn.remove(financeInSelected)
                                notifyItemRemoved(pos)
                                financeInInterface?.apply {
                                    afterDeleteItem(financeInSelected, pos)
                                }
//                                context?.let {
//                                    addAll(FinanceIn().getList(it))
//                                }
                            }
                        }
                    })?.show()
            }
        }
        return true
    }

    interface FinanceInListFragmentInterface {
        fun onEditItem(financeIn: FinanceIn?, position: Int)
        fun afterDeleteItem(financeIn: FinanceIn?, position: Int)
    }

    interface AdapterListFinanceInInterface {
        fun onClickItem(financeIn: FinanceIn, view: View, position: Int)
    }

    class ViewHolderFinanceIn(itemView: View) : RecyclerView.ViewHolder(itemView)

    class AdapterListFinanceIn(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var listFinanceIn = ArrayList<FinanceIn>()
        var dictFirstTransactionDate: HashMap<String, Int> = HashMap<String, Int>()
        private lateinit var context: Context
        private var adapterListFinanceInInterface: AdapterListFinanceInInterface? = null
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            context = parent.context
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_finance_in, parent, false)
            return ViewHolderFinanceIn(view)
        }

        override fun getItemCount(): Int {
            return listFinanceIn.size
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            val financeIn = listFinanceIn?.get(position)
            financeIn?.let {
                viewHolder.itemView.txtTransactionDate.text = it.TransactionDate//util.formatStrDateToIndonesia(it.TransactionDate)
                if (dictFirstTransactionDate.get(it.TransactionDate) != it.TransactionID) {
                    viewHolder.itemView.txtTransactionDate.visibility = View.GONE
//                    viewHolder.itemView.vTopLine.visibility = View.VISIBLE
                } else {
                    viewHolder.itemView.txtTransactionDate.visibility = View.VISIBLE
//                    viewHolder.itemView.vTopLine.visibility = View.GONE
                }
                viewHolder.itemView.txtHour.text = it.TransactionTime
//                viewHolder.itemView.txtTo.text = it.CashBankAccountName
                viewHolder.itemView.txtFrom.text = it.ReceivedFrom
                viewHolder.itemView.txtAmount.text = it.Amount.toString()
                viewHolder.itemView.txtNote.text = it.Note
                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        adapterListFinanceInInterface?.onClickItem(it, viewHolder.itemView, position)
                    }
                })
            }
        }

        fun addAll(_listFinanceIn: List<FinanceIn>) {
            listFinanceIn?.let {
                it.clear()
                it.addAll(_listFinanceIn)
                notifyDataSetChanged()
            }
            dictFirstTransactionDate = HashMap()
            for (cb in _listFinanceIn) {
                if (!dictFirstTransactionDate.containsKey(cb.TransactionDate)) {
                    dictFirstTransactionDate.put(cb.TransactionDate, cb.TransactionID)
                }
            }
        }

        fun setInterface(adapterInterface: AdapterListFinanceInInterface) {
            this.adapterListFinanceInInterface = adapterInterface
        }
    }
}