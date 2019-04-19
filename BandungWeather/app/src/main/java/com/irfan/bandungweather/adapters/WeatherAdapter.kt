package com.irfan.bandungweather.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.irfan.bandungweather.R
import com.irfan.bandungweather.models.WeatherList
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(val dataList: MutableList<WeatherList>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        Picasso.get()
            .load("http://openweathermap.org/img/w/" + data.weather[0].icon + ".png")
            .into(holder.imgIcon)
        holder.txtMain.text = data.weather[0].main
        holder.txtDesc.text = data.weather[0].description

        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val newDate = dateFormat.parse(dataList[position].dt_txt)
        dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        holder.txtDate.text = dateFormat.format(newDate)
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