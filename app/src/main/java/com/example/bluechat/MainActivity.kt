package com.example.bluechat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
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
import java.io.InputStream
import java.io.OutputStream


private lateinit var binding: ActivityMainBinding
val REQUEST_ACCESS_COARSE_LOCATION=101
// define bluetooth manager and adapter
lateinit var bluetoothManager: BluetoothManager
lateinit var bluetoothAdapter: BluetoothAdapter
class MainActivity : AppCompatActivity()  {
    private var selectedpos:Int=-1

    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
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
        val bld= mutableListOf<BluetoothDevice>()

        fun update(){
            val paired:Set<BluetoothDevice>?=bluetoothAdapter?.bondedDevices
            paired?.forEach{device ->
                dev.add(device.name)
                addr.add(device.address)
                bld.add(device)
                bliobj.setdata(device.name,device.address,device)
            }

            val pa= Adapter(bliobj.getalldata())
            binding.recyclerView.adapter=pa
            binding.recyclerView.layoutManager=LinearLayoutManager(this)
        }
        update()



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
        binding.update.setOnClickListener{
            bliobj.deleteall()
            update()
        }

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
                    binding.recyclev.adapter=devicelista
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