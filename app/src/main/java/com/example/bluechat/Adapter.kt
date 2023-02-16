package com.example.bluechat


import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

//Adapter for paired devices adapter


class Adapter(var data: List<blueinfo>): RecyclerView.Adapter<Adapter.ViewHolder>(){


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val devicen: TextView = itemView.findViewById(R.id.devicen)
        val addres: TextView = itemView.findViewById(R.id.addres)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemview=LayoutInflater.from(parent.context).inflate(R.layout.view,parent,false)
        return ViewHolder(itemview)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.devicen.text=data[position].devicen
        holder.addres.text=data[position].addres
        holder.itemView.setOnClickListener{
            if(data[position].blud.bondState==BluetoothDevice.BOND_BONDED){
                Toast.makeText(holder.itemView.context, "${data[position].devicen}is bonded", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


}