package com.example.irfan.myfirstapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.irfan.myfirstapp.R
import com.example.irfan.myfirstapp.model.WeatherList
import com.squareup.picasso.Picasso

class WeatherAdapter(val context: Context, val dataList: MutableList<WeatherList>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
                .load("http://openweathermap.org/img/w/" + dataList[position].weather[0].icon + ".png")
                .into(holder.imgIcon)
        holder.txtMain.text = dataList[position].weather[0].main
        holder.txtDesc.text = dataList[position].weather[0].description
        holder.txtDate.text = dataList[position].dt_txt
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgIcon = itemView.findViewById<ImageView>(R.id.imgIcon)
        val txtMain = itemView.findViewById<TextView>(R.id.txtMain)
        val txtDesc = itemView.findViewById<TextView>(R.id.txtDesc)
        val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
    }
}