package com.example.scumapps.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.scumapps.ui.HomeActivity
import com.example.scumapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wasteType.text = intent.getStringExtra("waste")

        when (intent.getBooleanExtra("type", true)) {
            true -> binding.objectPhoto.setImageBitmap(IMAGEBITMAP)
            false -> binding.objectPhoto.setImageURI(IMAGEURI)
        }

        binding.backHomeBtn.setOnClickListener {
            startActivity(
                Intent(this@DetailActivity, HomeActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }
    companion object {
        lateinit var IMAGEBITMAP : Bitmap
        lateinit var IMAGEURI : Uri
    }
}