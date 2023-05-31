package com.example.scumapps.ui.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.scumapps.R
import com.example.scumapps.databinding.ActivityDetailBinding
import com.example.scumapps.ui.HomeActivity

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

        when(binding.wasteType.text) {
            "PET" -> binding.wasteDesc.text = getString(R.string.PET)
            "Glass" -> binding.wasteDesc.text = getString(R.string.Glass)
            "AluCan" -> binding.wasteDesc.text = getString(R.string.AluCan)
            else -> binding.wasteDesc.text = "No description for this image"
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