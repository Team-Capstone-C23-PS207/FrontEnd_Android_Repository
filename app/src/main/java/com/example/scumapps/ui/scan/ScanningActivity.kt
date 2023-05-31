package com.example.scumapps.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import com.example.scumapps.R
import com.example.scumapps.databinding.ActivityScanningBinding
import com.example.scumapps.service.ApiConfig
import com.example.scumapps.service.ScanResponse
import com.example.scumapps.ui.HomeActivity
import com.example.scumapps.ui.detail.DetailActivity
import com.example.scumapps.utils.Utils.Companion.createCustomTempFile
import com.example.scumapps.utils.Utils.Companion.reduceFileImage
import com.example.scumapps.utils.Utils.Companion.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class ScanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanningBinding
    private var getFile: File? = null
    private lateinit var scanningViewModel: ScanningViewModel
    private lateinit var drawable : Drawable
    private lateinit var bmp : Bitmap
    private lateinit var baos : ByteArrayOutputStream
    private lateinit var byteArr : ByteArray
    private var isBitmap : Boolean = false

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(getFile?.path)
            DetailActivity.IMAGEBITMAP = result
            binding.previewImageView.setImageBitmap(result)
            isBitmap = true
            uploadImage()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@ScanningActivity)
            getFile = myFile
            DetailActivity.IMAGEURI = selectedImg
            binding.previewImageView.setImageURI(selectedImg)
            isBitmap = false
            uploadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this@ScanningActivity, HomeActivity::class.java))

        }

        binding.nextButton.setOnClickListener {

            startActivity(Intent(this@ScanningActivity, DetailActivity::class.java)
                .putExtra("waste", binding.wasteType.text)
                .putExtra("type", isBitmap))
        }

        binding.cameraButton.setOnClickListener {
            startTakePhoto()
        }

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@ScanningActivity,
                "com.example.scumapps",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            uploadImageApi(imageMultipart)

        }
        else {
            binding.wasteType.text = "not found"
        }
    }
    private fun uploadImageApi(img: MultipartBody.Part) {
        val client = ApiConfig.getApiService().uploadPicture(img)
        client.enqueue(object : Callback<ScanResponse> {
            override fun onResponse(
                call: Call<ScanResponse>,
                response: Response<ScanResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        binding.wasteType.text = responseBody.result
                    }
                } else {
                    Log.e(TAG, "onFailure: data not found")
                }
            }

            override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: image failed to upload")
            }
        })
    }

    private fun convertToByteArr() {
        drawable = binding.previewImageView.drawable
        bmp = drawable.toBitmap()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos)
        byteArr = baos.toByteArray()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "ScanningActivity"
    }
}