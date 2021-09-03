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

        val multiOptionSwitch1 = findViewById<MultiOptionSwitch>(R.id.multiOptionSwitch1)
        val multiOptionSwitch2 = findViewById<MultiOptionSwitch>(R.id.multiOptionSwitch2)
        val button = findViewById<Button>(R.id.button)
        val textView1 = findViewById<TextView>(R.id.text1)
        val textView2 = findViewById<TextView>(R.id.text2)

        multiOptionSwitch1.setOptionChangedListener {
            textView1.text = "Selected: $it"
        }

        multiOptionSwitch2.setOptionChangedListener {
            textView2.text = "Selected: $it"
        }

        button.setOnClickListener {
            multiOptionSwitch1.selectOption(2)
            multiOptionSwitch2.selectOption(2)
        }
    }
}