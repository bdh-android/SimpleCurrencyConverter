package com.example.simplecurrencyconverter.features.rates


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplecurrencyconverter.data.CurrencyRepository
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates
import com.example.simplecurrencyconverter.features.common.roundBy
import com.example.simplecurrencyconverter.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RatesViewModel @Inject constructor(private val repository: CurrencyRepository)  : ViewModel() {
    private val _BaseSymbol :MutableStateFlow<String> = MutableStateFlow(
        "USD")
    val BaseSymbol=_BaseSymbol.asStateFlow()

    val  allRates:StateFlow<RatesUIState?>  = repository.getBaseSymbol().filter {
        !it.equals("")
    }.flatMapLatest {
        _BaseSymbol.value=it
        repository.getAllApiRatesBasedOnBaseCurrency(it)
    }.map {
        getRates(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),null)


      fun getRates(it : Result<List<CurrencyRates>>):RatesUIState {
          val filteredData= it.data.map {
              it.copy(
                  price = it.price.roundBy().toDouble()
              )
          }
       return when(it){

            is Result.Loading ->{

                RatesUIState.LoadingState(filteredData,true)
            }
            is Result.Error ->{

                 RatesUIState.ErrorState(filteredData,it.msg,false)
            }
            is Result.Success ->{

               RatesUIState.SuccessState(filteredData,false)
            }
        }
      }



      fun saveBaseSymbolToSharedPrefs(base: String){

        viewModelScope.launch {
            repository.setBaseSymbol(base)


        }
    }

    fun refresh(){
        //send empty string into datastore ,cause it will not send flow data if the old data is same as new data
        saveBaseSymbolToSharedPrefs("")
        saveBaseSymbolToSharedPrefs(_BaseSymbol.value)
    }

}