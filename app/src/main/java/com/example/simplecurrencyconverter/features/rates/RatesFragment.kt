package com.example.simplecurrencyconverter.features.rates



import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.palette.graphics.Palette
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.simplecurrencyconverter.R
import com.example.simplecurrencyconverter.databinding.FragmentRatesBinding
import com.example.simplecurrencyconverter.features.common.SpinnerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RatesFragment : Fragment() ,AdapterView.OnItemSelectedListener,SwipeRefreshLayout.OnRefreshListener{

    private var _binding: FragmentRatesBinding? = null
    private var cardView:CardView?=null
    private var currencyAdapter: CurrencyAdapter?=null
    private var isSpinnerItemSelected=0
    private val binding get() = _binding!!
      val viewModel:RatesViewModel by viewModels ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRatesBinding.inflate(inflater, container, false)
        val root: View = binding.root
         cardView = binding.basecardview

        val swipeToRefesh = binding.swiperefreshlayout
        swipeToRefesh?.let {
            it.setColorSchemeResources(R.color.blue,R.color.red,R.color.green,R.color.orange)
            it.setOnRefreshListener (this)
        }
       // val loadingProgressBar: ProgressBar = binding.progressbar
        val recyclerView = binding.currenciesList

        currencyAdapter= CurrencyAdapter(requireActivity(),emptyList())
        recyclerView.adapter=currencyAdapter
        val baseSpinner=binding.spinnerBase
        val currenciesArray=resources.getStringArray(R.array.symbolss)
        val spinneradapter=
            SpinnerAdapter(requireActivity(), currenciesArray.toList())//ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,currenciesArray)
        baseSpinner.adapter=spinneradapter
        baseSpinner.onItemSelectedListener=this

       val handler= CoroutineExceptionHandler { _, e ->
            Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_SHORT).show()
        }

        viewLifecycleOwner.lifecycleScope.launch(handler) {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.allRates.collect {
                        when (it) {
                            is RatesUIState.LoadingState -> {
                                swipeToRefesh.isRefreshing = true
                                //loadingProgressBar.visibility = View.VISIBLE
                                currencyAdapter?.setCurrenyList(it.data)
                            }
                            is RatesUIState.SuccessState -> {
                                swipeToRefesh.isRefreshing = false
                               // loadingProgressBar.visibility = View.INVISIBLE
                                currencyAdapter?.setCurrenyList(it.data)
                            }
                            is RatesUIState.ErrorState -> {
                                swipeToRefesh.isRefreshing = false
                                //loadingProgressBar.visibility = View.INVISIBLE
                                currencyAdapter?.setCurrenyList(it.data)
                                showSnackBar(requireView(), it.errorMsg.toString())
                            }
                            else -> {
                                swipeToRefesh.isRefreshing = false
                                //loadingProgressBar.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                launch {
                    viewModel.BaseSymbol.collect{
                         baseSpinner.setSelection(currenciesArray.indexOf(it))
                    }
                }
            }

        }


        return root
    }

    private fun showSnackBar(view: View, errorMsg: String) {
      Snackbar.make(view,errorMsg,Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isSpinnerItemSelected=0

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        generateCardViewColor(cardView,(parent?.selectedItem as String).lowercase())
        isSpinnerItemSelected++
        if (isSpinnerItemSelected<=1) return
        if (view != null) {


            val text = parent?.selectedItem as String
                  viewModel.saveBaseSymbolToSharedPrefs(text)

               }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun generateCardViewColor(card: CardView?,flagSymbol:String ){
        val drawableId:Int = resources.getIdentifier(flagSymbol,"drawable",requireContext().packageName)
        Palette.from(BitmapFactory.decodeResource(resources,drawableId)).generate(Palette.PaletteAsyncListener {
            val vibrant= it?.vibrantSwatch
            if (vibrant!= null){
               card?.setCardBackgroundColor(vibrant.rgb)
            }
        })
    }
    override fun onRefresh() {
        viewModel.refresh()
    }
}