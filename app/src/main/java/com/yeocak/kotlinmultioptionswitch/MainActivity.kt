package com.yeocak.kotlinmultioptionswitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yeocak.kotlinmultioptionswitchlib.MultiOptionSwitch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val multiOptionSwitch = findViewById<MultiOptionSwitch>(R.id.multiOptionSwitch)
        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.text)

        multiOptionSwitch.setOptionChangedListener {
            textView.text = "Selected: $it"
        }

        button.setOnClickListener { multiOptionSwitch.selectOption(2) }
    }
}