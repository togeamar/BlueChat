package com.example.bluechat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
//adapter for available devices.

class ADapter : RecyclerView.Adapter<ADapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val devicen: TextView =itemView.findViewById(R.id.devicen)
        val addres: TextView =itemView.findViewById(R.id.addres)

    }
    private val devices= ArrayList<BluetoothDevice>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADapter.ViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.view,parent,false)
        return ViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        return devices.size
    }
    fun addDevice(device: BluetoothDevice){
        devices.add(device)
    }
    fun clearDevices(){
        devices.clear()
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.devicen.text=devices[position].name
        holder.addres.text=devices[position].address
    }


}