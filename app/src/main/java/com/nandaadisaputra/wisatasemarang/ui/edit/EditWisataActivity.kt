package com.nandaadisaputra.wisatasemarang.ui.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.nandaadisaputra.wisatasemarang.databinding.ActivityEditWisataBinding
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient
import com.nandaadisaputra.wisatasemarang.ui.detail.DetailWisataActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditWisataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditWisataBinding
    private lateinit var viewModel: EditWisataViewModel
    private var imageUri: Uri? = null
    private var currentPhotoPath: String? = null
    private var wisataId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel dengan Retrofit
        val apiService = RetrofitClient.apiService
        viewModel = EditWisataViewModel(apiService)

        // Mendapatkan ID wisata dari Intent
        wisataId = intent.getIntExtra("WISATA_ID", 0)

        // Mengisi data input dengan nilai yang dikirim melalui Intent
        binding.etNamaEdit.setText(intent.getStringExtra("WISATA_NAMA"))
        binding.etLokasiEdit.setText(intent.getStringExtra("WISATA_LOKASI"))
        binding.etDeskripsiEdit.setText(intent.getStringExtra("WISATA_DESKRIPSI"))

        // Mengatur tombol pemilihan gambar dan tombol update
        binding.btnPilihGambar.setOnClickListener { pilihGambar() }
        binding.btnUpdate.setOnClickListener { updateWisata() }
    }

    /**
     * Memilih gambar dari galeri atau mengambil foto dengan kamera
     */
    private fun pilihGambar() {
        val intentGallery = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Gagal membuat file gambar", Toast.LENGTH_SHORT).show()
            null
        }

        photoFile?.also {
            imageUri = FileProvider.getUriForFile(this, "$packageName.provider", it)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }

        val chooser = Intent.createChooser(intentGallery, "Pilih Gambar")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentCamera))
        startActivityForResult(chooser, 100)
    }

    /**
     * Menangani hasil pemilihan gambar atau pengambilan foto
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data ?: Uri.fromFile(File(currentPhotoPath ?: ""))
            if (imageUri != null) {
                binding.imgWisataEdit.setImageURI(imageUri) // Menampilkan gambar yang dipilih
                currentPhotoPath = getRealPathFromURI(imageUri!!)
            } else {
                Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Mengupdate data wisata beserta gambarnya
     */
    private fun updateWisata() {
        if (imageUri == null) {
            Toast.makeText(this, "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        val nama = binding.etNamaEdit.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasi = binding.etLokasiEdit.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsi = binding.etDeskripsiEdit.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        try {
            val file = File(currentPhotoPath ?: getRealPathFromURI(imageUri!!) ?: "")
            if (!file.exists()) throw IOException("File tidak ditemukan")

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val gambar = MultipartBody.Part.createFormData("gambar", file.name, requestFile)

            // Mengirim permintaan update ke server
            viewModel.updateWisata(wisataId, nama, lokasi, deskripsi, gambar) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailWisataActivity::class.java).apply {
                    putExtra("WISATA_ID", wisataId)
                }
                startActivity(intent) // Pindah ke halaman DetailWisataActivity
                finish()
            }
            Log.d("UpdateWisata", "Navigating to DetailWisataActivity with ID: $wisataId")

        } catch (e: Exception) {
            Toast.makeText(this, "Terjadi kesalahan saat mengunggah gambar", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Membuat file gambar baru untuk menyimpan foto yang diambil dari kamera
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.apply {
            if (!exists()) mkdirs()
        }
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    /**
     * Mengubah URI gambar menjadi path file yang dapat digunakan
     */
    private fun getRealPathFromURI(uri: Uri): String? {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val file = File(cacheDir, "temp_image.jpg")
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return file.absolutePath
        }
        return null
    }
}
