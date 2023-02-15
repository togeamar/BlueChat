package com.example.bluechat


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass.Device
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

//Adapter for paired devices adapter

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
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}