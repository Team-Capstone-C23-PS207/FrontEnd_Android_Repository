package com.example.scumapps.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.scumapps.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentToAuth = Intent(this, AuthenticationActivity::class.java)
        startActivity(intentToAuth)
        finish()
    }
}