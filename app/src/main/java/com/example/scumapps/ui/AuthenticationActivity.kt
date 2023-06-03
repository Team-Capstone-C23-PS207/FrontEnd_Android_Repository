package com.example.scumapps.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.scumapps.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportActionBar?.hide()
    }
}