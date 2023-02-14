package com.example.bluechat

object bliobj {
    var listdata = mutableListOf<blueinfo>()

    fun setdata(devicen:String,addres:String){
        listdata.add(blueinfo(devicen,addres))
    }
    fun getalldata():List<blueinfo>{
        return listdata
    }

}