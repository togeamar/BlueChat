package com.example.bluechat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.bluechat.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter?=bluetoothManager.adapter



        if(bluetoothAdapter==null){
            binding.hi.text="bluetooth is not supported"
        }
        else{
            binding.hi.text="bluetooth is supported"
        }
        fun chblue(){
            if (bluetoothAdapter?.isEnabled==false){
                val enablebtintent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                ) {
                   ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),101)
                }
                startActivity(enablebtintent)
            }

        }
        binding.on.setOnClickListener {
            chblue()
        }
        val mainhandler=Handler(Looper.getMainLooper())

        mainhandler.post(object:Runnable {
            override fun run() {
                if (bluetoothAdapter?.isEnabled==false){
                    binding.on.text="turn on"
                }
                else{
                    binding.on.text="its on"
                }
                mainhandler.postDelayed(this,1000)
            }
        })

        val dev= mutableListOf<String>()
        val addr= mutableListOf<String>()

        val paired:Set<BluetoothDevice>?=bluetoothAdapter?.bondedDevices
        paired?.forEach{device ->
            dev.add(device.name)
            addr.add(device.address)
        }
        binding.devi.text=dev.toString()
        binding.address.text=addr.toString()


    }
}