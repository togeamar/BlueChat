package com.example.bluechat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluechat.databinding.ActivityMainBinding


private lateinit var binding: ActivityMainBinding
val REQUEST_ACCESS_COARSE_LOCATION=101
// define bluetooth manager and adapter
lateinit var bluetoothManager: BluetoothManager
lateinit var bluetoothAdapter: BluetoothAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bluetoothManager=getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter= bluetoothManager.adapter

        //check if the device supports bluetooth
        if(bluetoothAdapter==null){
            binding.hi.text="bluetooth is not supported"
        }
        else{
            binding.hi.text="bluetooth is supported"
        }
        //function to turn bluetooth turn off or on
        fun chblue(){
            if (bluetoothAdapter?.isEnabled==false){
                val enablebtintent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                ) {
                   ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT),101)
                }
                startActivity(enablebtintent)
            }
            if(bluetoothAdapter?.isEnabled==true){
                bluetoothAdapter.disable()
            }

        }
        binding.on.setOnClickListener {
            chblue()
        }
        //mainhandler to refresh paired devices list every second which is not a good practice
        //but i am too lazy to change it.its better handled in available devices list below
        val mainhandler=Handler(Looper.getMainLooper())

        mainhandler.post(object:Runnable {
            override fun run() {
                if (bluetoothAdapter?.isEnabled==false){
                    binding.on.text="turn on"
                }
                else{
                    binding.on.text="turn off"
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
            bliobj.setdata(device.name,device.address)
        }

        binding.recyclerView.adapter=Adapter(bliobj.getalldata())
        binding.recyclerView.layoutManager=LinearLayoutManager(this)

        //commented code used for making android discoverable
       // val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
       // startActivity(intent)

        //taking location service permission
        binding.scan.setOnClickListener {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                when(ContextCompat.checkSelfPermission(baseContext,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    PackageManager.PERMISSION_DENIED ->{
                            if (ContextCompat.checkSelfPermission(baseContext,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                                    PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                    REQUEST_ACCESS_COARSE_LOCATION)
                            }
                        }
                    
                    PackageManager.PERMISSION_GRANTED ->{
                         Log.d("discoverdevices","permission granted")
                    }
                }
                //registering receiver which will receive actions in onreceive
                registerReceiver(mReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
                registerReceiver(mReceiver,IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
                registerReceiver(mReceiver,IntentFilter(BluetoothDevice.ACTION_FOUND))
                //starting scanning process
                bluetoothAdapter.startDiscovery()

            }
        }
        binding.recyclev.layoutManager=LinearLayoutManager(this)

    }
    //defining adapter
    private val devicelista =ADapter()
    //initializing Broadcast receivr
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent?.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("hi","started")
                    devicelista.clearDevices()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("hi","finished")
                }
                BluetoothDevice.ACTION_FOUND->{
                    val device:BluetoothDevice= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    Log.d("hi","${device.name} ${device.address}")
                    devicelista.addDevice(device)
                    Toast.makeText(context,"${device.name} ${device.address}", Toast.LENGTH_SHORT).show()
                    //updating list of devices everytime we found new device
                    binding.recyclev.adapter=devicelista

                }
            }
        }

    }
    override fun onDestroy() {
        devicelista.clearDevices()
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }


}