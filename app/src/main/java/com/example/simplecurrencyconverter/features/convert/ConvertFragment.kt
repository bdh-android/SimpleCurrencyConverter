package com.example.simplecurrencyconverter.features.convert

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.simplecurrencyconverter.R
import com.example.simplecurrencyconverter.databinding.FragmentConvertBinding
import com.example.simplecurrencyconverter.features.common.SpinnerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConvertFragment : Fragment() ,AdapterView.OnItemSelectedListener,SwipeRefreshLayout.OnRefreshListener{

    private var _binding: FragmentConvertBinding? = null
    private var spinnerItemSelectedCounter=0

    private val binding get() = _binding!!
    private val viewModel:ConversionViewModel by viewModels ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConvertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currenciesList= resources.getStringArray(R.array.symbolss)
        val indicator = binding.progressIndicator
        val spinnerFrom = binding.spinnerFrom
        val spinnerTo  = binding.spinnerTo
        val editTextFrom= binding.edittextFrom
        val editTextTo= binding.edittextTo
        val date = binding.updateDate
        val fromSpinnerAdapter = SpinnerAdapter(requireActivity(), currenciesList.toList()) //ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,currenciesList)
        val toSpinnerAdapter = SpinnerAdapter(requireActivity(),currenciesList.toList()) //ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,currenciesList)
        val swipeToRefresh   = binding.swipetorefresh
        swipeToRefresh?.let {
            it.setColorSchemeResources(R.color.blue,
                R.color.red,
                R.color.green,
                R.color.orange)
            it.setOnRefreshListener (this)
        }

        with(spinnerFrom){
            onItemSelectedListener=this@ConvertFragment
            adapter=fromSpinnerAdapter

        }
        with(spinnerTo){
            onItemSelectedListener=this@ConvertFragment
            adapter= toSpinnerAdapter
        }

        val txtwatcherfrom= object : TextWatcher{
            var text=""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text=s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null  && s!=text) {
                    viewModel.Event(ConvertUiEvents.fromEditTextChanged(s.toString()))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        val txtwatcherto= object : TextWatcher{
            var text=""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                text=s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 if (s != null && s!=text) {
                     viewModel.Event(ConvertUiEvents.toEditTextChanged(s.toString()))
                    }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        editTextFrom.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus){
                viewModel.Event(ConvertUiEvents.fromEditTextChanged("1"))
                editTextFrom.addTextChangedListener(txtwatcherfrom)

            }else{
                editTextFrom.removeTextChangedListener(txtwatcherfrom)
            }
        }
        editTextTo.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus){
                viewModel.Event(ConvertUiEvents.toEditTextChanged("1"))
                editTextTo.addTextChangedListener(txtwatcherto)

            }else{
                editTextTo.removeTextChangedListener(txtwatcherto)
            }
        }
        val handler= CoroutineExceptionHandler(){_,e->

           Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_LONG).show()
        }

        viewLifecycleOwner.lifecycleScope.launch(handler) {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{

                    spinnerFrom.setSelection(currenciesList.indexOf(it?.fromSymbol))
                    spinnerTo.setSelection(currenciesList.indexOf(it?.toSymbol ))
                    val editTextFromCursor = editTextFrom.selectionStart

                    editTextFrom.setText(it?.fromText)
                    if(editTextFromCursor> -1 && editTextFromCursor <= editTextFrom.length())
                    editTextFrom.setSelection(editTextFromCursor)
                     val editTextToCursor = editTextTo.selectionStart

                    editTextTo.setText(it?.toText)
                    if(editTextToCursor> -1 && editTextToCursor <= editTextTo.length())
                    editTextTo.setSelection(editTextToCursor)

                    date.text = getString(R.string.date , it?.date)
                    if(it?.isLoading == true) {
                        swipeToRefresh?.isRefreshing = true
                        indicator.visibility = View.VISIBLE
                    }
                    else {
                        swipeToRefresh?.isRefreshing = false
                        indicator.visibility = View.INVISIBLE
                    }
                    if (it?.errorMsg != null) {
                        Snackbar.make(requireView(),it.errorMsg,Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        spinnerItemSelectedCounter = 0
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerItemSelectedCounter++
        if (spinnerItemSelectedCounter<=2) return
        val selected = parent?.selectedItem
        if (selected == null) {
            return
        }

        if (parent?.id == R.id.spinner_from ){
            viewModel.Event(ConvertUiEvents.fromSpinnerChanged(selected.toString()))
        }else if (parent?.id == R.id.spinner_to){
            viewModel.Event(ConvertUiEvents.toSpinnerChanged(selected.toString()))
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

}