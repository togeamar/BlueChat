package com.example.bluechat

import android.bluetooth.BluetoothDevice

object bliobj {
    var listdata = mutableListOf<blueinfo>()

    fun setdata(devicen:String, addres:String, blud:BluetoothDevice){
        listdata.add(blueinfo(devicen,addres,blud))
    }
    fun getalldata():List<blueinfo>{
        return listdata
    }
    fun getdata(pos:Int): BluetoothDevice {
        return listdata[pos].blud
    }
    fun deleteall(){
        listdata.clear()
    }

}