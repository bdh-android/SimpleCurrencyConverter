package com.example.simplecurrencyconverter.features.convert



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecurrencyconverter.data.CurrencyRepository
import com.example.simplecurrencyconverter.features.common.roundBy
import com.example.simplecurrencyconverter.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ConversionViewModel  @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    private val _uiState:MutableStateFlow<ConvertUiState?> = MutableStateFlow(
        ConvertUiState("USD","EUR",0.0,
            "","0","0",null,false))
    val uiState = _uiState.asStateFlow()

    val fromSymbol  = repository.getFromSymbol()
    val toSymbol  = repository.getToSymbol()
    val stateFlow = fromSymbol.combine(toSymbol) { fromSymbol, toSymbol ->
        fetchPrice(fromSymbol, toSymbol)

    }

      init {
         viewModelScope.launch {
             stateFlow.collect()
         }
   }


    suspend  fun fetchPrice(fromSymbol:String?,toSymbol:String?){
        if (fromSymbol != null && toSymbol != null) {

                repository.getApiConversionFromCurrencyToAnother(fromSymbol,toSymbol).collect{
                    _uiState.value = _uiState.value?.copy(
                        fromSymbol=fromSymbol,
                        toSymbol=toSymbol,)
                    when (it){
                        is Result.Loading ->{

                                try {
                                    _uiState.value = _uiState.value?.copy(
                                        fromText= if(it.data.price > 0)   "1" else "0",
                                        price = it.data.price.roundBy().toDouble()
                                                , date = it.data.date,
                                        toText=it.data.price.roundBy()
                                         , isLoading = true
                                    )
                                }catch (e:Exception){

                                }




                        }
                        is Result.Success ->{
                            try{
                                _uiState.value = _uiState.value?.copy(
                                    fromText="1",
                                    price =it.data.price.roundBy().toDouble()
                                    ,
                                    date = it.data.date,
                                    toText=it.data.price.roundBy()
                                     ,
                                    isLoading = false
                                )
                            }catch (e:Exception){

                            }
                        }
                        is Result.Error ->{
                            try{ _uiState.value = _uiState.value?.copy(
                                fromText=if(it.data.price>0) "1" else "0",
                                price =it.data.price.roundBy().toDouble()
                                ,
                                date = it.data.date,
                                toText= if(it.data.price>0) it.data.price.roundBy()
                                    else "0",errorMsg = it.msg,
                                isLoading = false)
                            }catch (e:Exception){

                            }
                        }

                    }

                }

        }
    }

    fun Event(event : ConvertUiEvents){
        when(event){
            is ConvertUiEvents.fromSpinnerChanged -> {
                val from = event.fromSymbol
               viewModelScope.launch {
                   repository.setFromSymbol(from)

               }


            }
            is ConvertUiEvents.toSpinnerChanged -> {

                val to = event.toSymbol
                viewModelScope.launch {
                    repository.setToSymbol(to)

                }

            }
            is ConvertUiEvents.fromEditTextChanged -> {


                val from =  Math.abs(event.fromText.toDoubleOrNull()?:1.0)
                val to = _uiState.value?.price?.times(from)
                _uiState.value = _uiState.value?.copy(
                    fromText = from.toString() ,
                    toText =(to?:0.0).roundBy()
                )

            }
            is ConvertUiEvents.toEditTextChanged -> {
                val to = Math.abs(event.toText.toDoubleOrNull()?:1.0)
                 try {
                     if (_uiState.value?.price!!>0) {
                         val from = to / _uiState.value?.price!!
                         _uiState.value = _uiState.value?.copy(
                             fromText = from.roundBy()
                             , toText = to.toString()
                         )
                     }else{
                         _uiState.value = _uiState.value?.copy(
                             fromText = "0.0",
                             toText = to.toString(),
                             errorMsg = "Price Data Not Available"
                         )
                     }

                 }catch (e:Exception){

                 }
            }
        }
    }



    fun refresh() {
       viewModelScope.launch {
           fetchPrice(_uiState.value?.fromSymbol,_uiState.value?.toSymbol)
       }
    }


}