package com.example.simplecurrencyconverter.features.rates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecurrencyconverter.R
import com.example.simplecurrencyconverter.data.locale.database.model.CurrencyRates

class CurrencyAdapter(val context: Context, var list:List<CurrencyRates>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {




    class CurrencyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var flag: ImageView = itemView.findViewById(R.id.recycler_flag_id)
        var currencySymbol:TextView=itemView.findViewById(R.id.to_currency)
        var price:TextView=itemView.findViewById(R.id.price)
        var date:TextView=itemView.findViewById(R.id.date)
        fun bind(x: CurrencyRates, context: Context) {

            val img = context.resources.getIdentifier(x.toCurrency.lowercase(),"drawable",context.packageName)
            flag.setImageResource(img)
         currencySymbol.setText(x.toCurrency)
            price.setText(x.price.toString())
            date.setText(x.date)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row_layout,parent,false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val rates = list.get(position)

        holder.bind(rates,context)
    }

    fun setCurrenyList(currencyList: List<CurrencyRates>){
        list=currencyList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return list.size
    }
}