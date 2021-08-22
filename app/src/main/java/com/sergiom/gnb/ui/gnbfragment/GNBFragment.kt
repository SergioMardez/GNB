package com.sergiom.gnb.ui.gnbfragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergiom.gnb.R
import com.sergiom.gnb.data.entities.GnbTransaction
import com.sergiom.gnb.databinding.FragmentGNBBinding
import com.sergiom.gnb.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import android.widget.AdapterView.OnItemClickListener


/**
 * A simple [Fragment] subclass.
 * Use the [GNBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class GNBFragment : Fragment(), TransactionAdapter.EventItemListener {

    private var binding: FragmentGNBBinding by autoCleared()
    private val viewModel: GNBViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter
    private lateinit var adapterSelectedSku: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.currency_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.currencyMenu -> {
                showAlertdialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertdialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_listview)

        val btndialog: Button = dialog.findViewById(R.id.btndialog) as Button
        btndialog.setOnClickListener { dialog.dismiss() }

        val currencyList = viewModel.getCurrencyForMenu()

        if (currencyList.isNotEmpty()) {
            val listView: ListView = dialog.findViewById(R.id.listview) as ListView
            val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireContext(),
                R.layout.list_item_menu, R.id.tvItem_menu, currencyList
            )
            listView.adapter = arrayAdapter

            dialog.show()
        } else {
            Toast.makeText(context, "Currency not set", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentGNBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupRecyclerViews()
    }

    private fun setListeners() {
        viewModel.mTransactionList.observe(viewLifecycleOwner, {
            binding.gnbImage.visibility = View.GONE
            adapter.setItems(it as ArrayList<GnbTransaction>)
        })

        viewModel.mError.observe(viewLifecycleOwner, {
            binding.gnbImage.visibility = View.GONE
            Toast.makeText(context, "ERROR: $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun setupRecyclerViews() {
        adapter = TransactionAdapter(this, true)
        binding.allTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.allTransactions.addItemDecoration(DividerItemDecoration(binding.allTransactions.context, DividerItemDecoration.VERTICAL))
        binding.allTransactions.adapter = adapter

        adapterSelectedSku = TransactionAdapter(this, false)
        binding.selectedTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.selectedTransaction.addItemDecoration(DividerItemDecoration(binding.allTransactions.context, DividerItemDecoration.VERTICAL))
        binding.selectedTransaction.adapter = adapterSelectedSku
    }

    override fun onClickedTransaction(transaction: GnbTransaction) {
        val allTransactions = viewModel.getAllTransactions(transaction) as ArrayList<GnbTransaction>
        adapterSelectedSku.setItems(allTransactions)
        binding.totalAmount.text = context?.getString(R.string.total_amount, viewModel.getAllEuros(allTransactions).toString())
    }

    companion object {
        /**
         * @return A new instance of fragment GNBFragment.
         */
        @JvmStatic
        fun newInstance() = GNBFragment()
    }
}