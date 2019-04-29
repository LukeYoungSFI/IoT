package com.estimote.indoorapp

import android.app.Notification
//import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.estimote.indoorsdk.IndoorLocationManagerBuilder
import com.estimote.indoorsdk_module.algorithm.OnPositionUpdateListener
import com.estimote.indoorsdk_module.algorithm.ScanningIndoorLocationManager
import com.estimote.indoorsdk_module.cloud.Location
import com.estimote.indoorsdk_module.cloud.LocationPosition
import com.estimote.indoorsdk_module.view.IndoorLocationView
import com.estimote.internal_plugins_api.scanning.ScanHandler
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
//import com.estimote.scanning_sdk.api.EstimoteBluetoothScannerFactory
import android.os.AsyncTask
import android.util.Log
import com.estimote.indoorapp.R.string.azimuth
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Math.pow
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import kotlin.math.sqrt

class ListenActivity: AppCompatActivity() {

    private lateinit var indoorLocationView: IndoorLocationView
    private lateinit var indoorLocationManager: ScanningIndoorLocationManager
    private lateinit var location: Location
    private lateinit var notification: Notification



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen)
        var user_location_x: Double
        var user_location_y: Double
        user_location_x =0.0
        user_location_y =0.0
        var localdatetime: String
        localdatetime = LocalDateTime.now().toString()
        //setup scanner to receive telemetry data
        val textViewid = findViewById(R.id.textView_id_display) as TextView
        val textViewismoving = findViewById(R.id.textView_ismoving_display) as TextView
        val textViewlight = findViewById(R.id.textView_lightlux_display) as TextView
        val textViewpressure = findViewById(R.id.textView_pressure_display) as TextView
        val togglebtn = findViewById(R.id.togglebtn) as Switch
        val scanner = EstimoteBluetoothScannerFactory(applicationContext).getSimpleScanner()
        val scanLauncher = scanner.estimoteTelemetryFullScan()
        var scanHandler : ScanHandler? = null
        togglebtn.setOnCheckedChangeListener(object:OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked:Boolean) {
                 if(isChecked){
                     scanHandler= scanLauncher.withBalancedPowerMode()
                        .withOnPacketFoundAction {
                            textViewid.text = it.identifier
                            textViewismoving.text = it.motionState.toString()
                            textViewlight.text = "%.3f".format(it.ambientLightInLux)
                            textViewpressure.text = "%.3f".format(it.pressure)
                            //pass a group of arguments which are beacon's id, timestamp, temperature, pressure and light in lux to doInBackground() method
                            localdatetime = LocalDateTime.now().toString()
                            postBeaconData().execute(it.identifier, it.motionState.toString(),it.pressure.toString(),it.ambientLightInLux.toString(),
                                    localdatetime,it.temperatureInCelsiusDegrees.toString())}
                        .start()}
                else{ scanHandler?.stop()
                 }
            }
        })
        //setup location to get user position(x,y)
        // get id of location to show from intent
        val locationId = "jason-zhang-s-location"
        // get object of location. If something went wrong, build empty location with no data.
        location = (application as IndoorApplication).locationsById[locationId] ?: buildEmptyLocation()
        //Create IndoorManager object
        indoorLocationManager = IndoorLocationManagerBuilder(this, location, (application as IndoorApplication).cloudCredentials)
                .withDefaultScanner()
                .build()
        indoorLocationManager.startPositioning()
        //Hook the listener for position update events
        indoorLocationManager.setOnPositionUpdateListener(object : OnPositionUpdateListener {
            override fun onPositionOutsideLocation() {
                //indoorLocationView.hidePosition()
            }

            override fun onPositionUpdate(locationPosition: LocationPosition) {
                runOnUiThread(object:Runnable {
                    public override fun run() {
                        // display real time positon x,y
                        val textViewLocation_x = findViewById(R.id.textView_location_x_display) as TextView
                        val textViewLocation_y = findViewById(R.id.textView_location_y_display) as TextView
                        // write the x,y coordinates to MySQL database
                        textViewLocation_x.text = locationPosition.x.toString()
                        textViewLocation_y.text = locationPosition.y.toString()
                        postUserLocation().execute(locationPosition.x.toString(), locationPosition.y.toString())
                        //check to see if x,y have been initialized, this block is for block-chain integration, temporarily disabled
//                        if(user_location_x == 0.0) {
//                            user_location_x = locationPosition.x
//                            user_location_y = locationPosition.y
//                            postUserLocation().execute(locationPosition.x.toString(), locationPosition.y.toString())
//                        }
//                        //the x,y set a threshold (1 meter) to determine if the user's location has moved
//                        else{
//                            if(user_location_x != locationPosition.x){
//                                var offset = sqrt(pow((user_location_x-locationPosition.x),2.toDouble())+ pow((user_location_y-locationPosition.y),2.toDouble()))
//                                if(offset > 1){postUserLocation().execute(locationPosition.x.toString(), locationPosition.y.toString())}
//                            }
//                        }
                    }
                })
            }
        })
        // Check if bluetooth is enabled, location permissions are granted, etc.
        RequirementsWizardFactory.createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        onRequirementsFulfilled = {
                            indoorLocationManager.startPositioning()
                        },
                        onRequirementsMissing = {
                            Toast.makeText(applicationContext, "Unable to scan for beacons. Requirements missing: ${it.joinToString()}", Toast.LENGTH_SHORT)
                                    .show()
                        },
                        onError = {
                            Toast.makeText(applicationContext, "Unable to scan for beacons. Error: ${it.message}", Toast.LENGTH_SHORT)
                                    .show()
                        })

    }
    private fun buildEmptyLocation(): Location {
        return Location("", "", true, "", 0.0, emptyList(), emptyList(), emptyList())
    }
    override fun onDestroy() {
        indoorLocationManager.stopPositioning()
        super.onDestroy()
    }
    inner class postBeaconData : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            var Result = "";
            val API_URL = "http://192.168.1.58:8088/saveAsset"

            try {
                //call http post to write telemetry data to MySQL database through a REST service
                val URL = URL(API_URL)
                val connect = URL.openConnection() as HttpURLConnection
                //get the passed arguments to form a json data for post
                val postData = ("{"
                        + "\"bid\":\""+p0[0]+"\","
                        + "\"date_time\":\""+p0[4]+"\","
                        + "\"managed_by\":\"Jason_Ni\","
                        + "\"temperature\":"+p0[5]+","
                        + "\"light\":"+p0[3]+","
                        + "\"is_moving\":\""+p0[1]+"\","
                        + "\"transaction_type\":\"motion\","
                        + "\"location_x\":12.1," // this x and y coordinate are just used for test
                        + "\"location_y\":10.2,"
                        + "\"pressure\":\""+p0[2]+"\""
                        + "}")
                val postBody: ByteArray = postData.toByteArray(StandardCharsets.UTF_8)

                connect.readTimeout = 8000
                connect.connectTimeout = 8000
                connect.requestMethod = "POST"
                connect.doOutput = true;
                connect.setRequestProperty("charset", "utf-8")
                connect.setRequestProperty("Content-length", postData)
                connect.setRequestProperty("Content-Type", "application/json")
                connect.connect();

                val outputStream = DataOutputStream(connect.outputStream)
                outputStream.write(postBody)
                outputStream.flush()
                val ResponseCode: Int = connect.responseCode;
                val Tag: String = "ListenActivity"
                Log.d(Tag, "ResponseCode" + ResponseCode)
                if (ResponseCode == 200 ) {
                    val tempStream: InputStream = connect.inputStream;
                    Result = ConvertToString(tempStream);
                }
            } catch(Ex: Exception) {

                Log.d("", "Error in doInBackground " + Ex.message);
            }
            //reurn result for future use
            return Result

        }
    }

    inner class postUserLocation : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg p0: String?): String {
            var Result = "";
            val API_URL = "http://192.168.1.58:8088/saveLocation"

            try {
                //call http post to write telemetry data to MySQL database through a REST service
                val URL = URL(API_URL)
                val connect = URL.openConnection() as HttpURLConnection
                //convert local coordinates to UTM coordinates, set the origin to (305836,4311277)
                //since the indoor app coordinate system is different from WGS84/UTM, a transformation is needed
                //x,y need to switch and y need to be reflected
                var x = p0[1]?.toDouble()!!.plus(305836)
                var y = p0[0]?.toDouble()
                y = 0- y!!
                y = y + 4311277
                //get the passed arguments to form a json data for post
                val postData = ("{"
                        + "\"uid\":\"1\","
                        + "\"id\":\"1\","
                        + "\"location_x\":\""+ x +"\","
                        + "\"location_y\":\""+y+"\","
                        + "}")
                val postBody: ByteArray = postData.toByteArray(StandardCharsets.UTF_8)

                connect.readTimeout = 8000
                connect.connectTimeout = 8000
                connect.requestMethod = "POST"
                connect.doOutput = true;
                connect.setRequestProperty("charset", "utf-8")
                connect.setRequestProperty("Content-length", postData)
                connect.setRequestProperty("Content-Type", "application/json")
                connect.connect();

                val outputStream = DataOutputStream(connect.outputStream)
                outputStream.write(postBody)
                outputStream.flush()
                val ResponseCode: Int = connect.responseCode;
                val Tag: String = "ListenActivity"
                Log.d(Tag, "ResponseCode" + ResponseCode)
                if (ResponseCode == 200 ) {
                    val tempStream: InputStream = connect.inputStream;
                    Result = ConvertToString(tempStream);
                }
            } catch(Ex: Exception) {

                Log.d("", "Error in doInBackground " + Ex.message);
            }
            //reurn result for future use
            return Result

        }
    }

    fun ConvertToString(inStream: InputStream): String {

        var Result: String = ""

        val isReader = InputStreamReader(inStream)
        var bReader = BufferedReader(isReader)
        var temp_str: String?

        try {

            while (true) {

                temp_str = bReader.readLine()

                if (temp_str == null) {

                    break
                }
                Result += temp_str;
            }


        } catch(Ex: Exception) {
            val Tag = "ListenActivity"
            Log.e(Tag,"Error in ConvertToString " + Ex.printStackTrace())
        }

        return Result

    }

}