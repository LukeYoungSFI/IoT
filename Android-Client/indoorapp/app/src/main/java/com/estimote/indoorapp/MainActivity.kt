package com.estimote.indoorapp

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.estimote.indoorsdk.IndoorLocationManagerBuilder
import com.estimote.indoorsdk_module.algorithm.OnPositionUpdateListener
import com.estimote.indoorsdk_module.algorithm.ScanningIndoorLocationManager
import com.estimote.indoorsdk_module.cloud.Location
import com.estimote.indoorsdk_module.cloud.LocationPosition
import com.estimote.indoorsdk_module.view.IndoorLocationView
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory
//import com.estimote.scanning_sdk.api.EstimoteBluetoothScannerFactory

/**
 * Main view for indoor location
 */

class MainActivity : AppCompatActivity() {

    private lateinit var indoorLocationView: IndoorLocationView
    private lateinit var indoorLocationManager: ScanningIndoorLocationManager
    private lateinit var location: Location
    private lateinit var notification: Notification

    companion object {
        val intentKeyLocationId = "location_id"
        fun createIntent(context: Context, locationId: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(intentKeyLocationId, locationId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanner = EstimoteBluetoothScannerFactory(applicationContext).getSimpleScanner()
        val scanHandler = scanner.estimoteTelemetryFullScan()
                .withBalancedPowerMode()
                .withOnPacketFoundAction {
                    Log.d("Full Telemetry", "Got Full Telemetry packet: $it")
                    val sensor_id = it.identifier.toString()
                    val is_moving = it.motionState
                    val lux = "%.3f".format(it.ambientLightInLux)
                    val pressure = "%.3f".format(it.pressure)
                    val textViewSensor = findViewById(R.id.sensor_data) as TextView
                    textViewSensor.text = resources.getString(R.string.sensor_data, sensor_id,is_moving,lux,pressure)
                }
                .start()
        // Declare notification that will be displayed in user's notification bar.
        // You can modify it as you want/
        /*notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.beacon_gray)
                .setContentTitle("Estimote Inc. \u00AE")
                .setContentText("Indoor location is running...")
                .setPriority(Notification.PRIORITY_HIGH)
                .build()*/
        // Get location id from intent and get location object from list of locations
        setupLocation()

        // Init indoor location view here
        indoorLocationView = findViewById(R.id.indoor_view) as IndoorLocationView

        // Give location object to your view to draw it on your screen
        indoorLocationView.setLocation(location)

        // Create IndoorManager object.
        // Long story short - it takes list of scanned beacons, does the magic and returns estimated position (x,y)
        // You need to setup it with your app context,  location data object,
        // and your cloud credentials that you declared in IndoorApplication.kt file
        // we are using .withScannerInForegroundService(notification)
        // this will allow for scanning in background and will ensura that the system won't kill the scanning.
        // You can also use .withSimpleScanner() that will be handled without service.
        indoorLocationManager = IndoorLocationManagerBuilder(this, location, (application as IndoorApplication).cloudCredentials)
                .withDefaultScanner()
                .build()

        // Hook the listener for position update events
        indoorLocationManager.setOnPositionUpdateListener(object : OnPositionUpdateListener {
            override fun onPositionOutsideLocation() {
                indoorLocationView.hidePosition()
            }

            override fun onPositionUpdate(locationPosition: LocationPosition) {
                indoorLocationView.updatePosition(locationPosition)
                Log.d("User Position X: ", locationPosition.x.toString())
                Log.d("User Position Y: ", locationPosition.y.toString())
                val textViewPosition = findViewById(R.id.user_position) as TextView
                val x = "%.3f".format(locationPosition.x)
                val y = "%.3f".format(locationPosition.y)
                textViewPosition.text = resources.getString(R.string.user_position,x,y )
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

    private fun setupLocation() {
        // get id of location to show from intent
        val locationId = intent.extras.getString(intentKeyLocationId)
        // get object of location. If something went wrong, we build empty location with no data.
        location = (application as IndoorApplication).locationsById[locationId] ?: buildEmptyLocation()
        // Set the Activity title to you location name
        title = location.name
    }

    private fun buildEmptyLocation(): Location {
        return Location("", "", true, "", 0.0, emptyList(), emptyList(), emptyList())
    }


    override fun onDestroy() {
        indoorLocationManager.stopPositioning()
        super.onDestroy()
    }
}
