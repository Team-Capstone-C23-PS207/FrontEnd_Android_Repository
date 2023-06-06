package com.example.scumapps.ui.detail

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scumapps.R
import com.example.scumapps.adapter.RecommendAdapter
import com.example.scumapps.databinding.ActivityDetailBinding
import com.example.scumapps.service.RekomendasiItem
import com.example.scumapps.ui.scan.ScanningViewModel
import com.example.scumapps.utils.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val scanningViewModel: ScanningViewModel by viewModels()
    private var listRec = ArrayList<RekomendasiItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvWaste.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        binding.rvWaste.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvWaste.addItemDecoration(itemDecoration)
        binding.rvWaste.adapter = RecommendAdapter(listRec)

        binding.wasteType.text = intent.getStringExtra("waste")

        val getFile : File = intent.extras?.get("File") as File
        sendImage(getFile)

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

        scanningViewModel.listRecommend.observe(this) { listRec ->
            setRecommendData(listRec)
        }

        scanningViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun sendImage(getFile : File) {
        val file = Utils.reduceFileImage(getFile)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
        scanningViewModel.uploadImageApi(imageMultipart)
    }

    private fun setRecommendData(listrecommend: List<RekomendasiItem>) {
        for (rec in listrecommend) {
            listRec.addAll(listOf(rec))
        }
        val adapter = RecommendAdapter(listRec)
        binding.rvWaste.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        lateinit var IMAGEBITMAP : Bitmap
        lateinit var IMAGEURI : Uri
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}