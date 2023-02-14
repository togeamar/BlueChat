package com.example.bluechat


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Adapter(var data:List<blueinfo>): RecyclerView.Adapter<Adapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val devicen:TextView=itemView.findViewById(R.id.devicen)
        val addres:TextView=itemView.findViewById(R.id.addres)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemview=LayoutInflater.from(parent.context).inflate(R.layout.view,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.devicen.text=data[position].devicen
        holder.addres.text=data[position].addres
    }

    override fun getItemCount(): Int {
        return data.size
    }
}