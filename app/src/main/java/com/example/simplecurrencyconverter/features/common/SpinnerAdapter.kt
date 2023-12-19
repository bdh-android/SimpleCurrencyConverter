package com.example.simplecurrencyconverter.features.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.simplecurrencyconverter.R

class SpinnerAdapter(val context: Context, val currencyList:List<String>) : BaseAdapter() {
  private val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return currencyList.size
    }

    override fun getItem(position: Int): Any {
        return currencyList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
         val view : View
         val vh: ItemHolder
         if (convertView == null){
             view = inflator.inflate(R.layout.spinner_row,parent,false)
             vh = ItemHolder(view)
             view?.tag=vh
         }else{
             view= convertView
             vh = view.tag as ItemHolder
         }
        vh.label.text = currencyList.get(position).toString()
        val flag = context.resources.getIdentifier(currencyList.get(position).lowercase(),"drawable",context.packageName)
        vh.img.setImageResource(flag)
        return view
    }
    private class ItemHolder(row:View?){
        val label: TextView
        val img:ImageView
        init {
            label = row?.findViewById(R.id.symbol) as TextView
            img = row.findViewById(R.id.spinner_flag_id) as ImageView
        }
    }
}