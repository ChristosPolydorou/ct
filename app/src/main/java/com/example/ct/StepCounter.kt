package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class StepCounter  : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SecurityManager
    private lateinit var stepSensor: Sensor
    private  var totalSteps = 0
    private  var goalSteps = 10000 // Replace with the user's goal

    private lateinit var stepsTextView: TextView

    override fun onCreate(savedTnstanceState: Bundle?) {
        val savedInstanceState
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the sensor manager and step counter sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Get the steps TextView
        stepsTextView = findViewById(R.id.stepsTextView)

        //Register a broadcast receiver to listen for step count updates
        LocalBroadcastManager.getInstance(this).registerReceiver(
            stepCountReceiver, IntentFilter("step-count")
        )
    }

    override fun onResume() {
        super.onResume()

        // Register the step counter sensor listener
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        // UnRegister the step counter sensor listerner
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            // Update the total steps count
            totalSteps = event.values[0].toInt()

            // Update the steps TextView
            stepsTextView.text = totalSteps.toString()

            // Check if the user has reached their goal
            if(totalSteps >= goalSteps) {

                    // Trigger the contextual action for reaching the goal
                   // (e.g., show a congratulatory message)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do Nothing
    }

    private val stepCountReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //Get the step count from the intent
            val stepCount = intent?.getIntExtra("step-count", 0) ?: 0

            //Update the total steps count
            totalSteps = stepCount

            //Update the steps TextView
            stepsTextView.text = totalSteps.toString()

            //Check if the user has reached their goal
            if (totalSteps >= goalSteps) {
                //Trigger the contextual action for reaching the goal
                //(e.g, show a congratulatory message)
            }

        }
    }
}