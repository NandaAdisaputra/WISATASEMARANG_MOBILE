package com.nandaadisaputra.wisatasemarang.ui

import TambahWisataViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.nandaadisaputra.wisatasemarang.databinding.ActivityAddWisataBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahWisataActivity : AppCompatActivity() {
    // Inisialisasi ViewModel menggunakan delegation
    private val viewModel: TambahWisataViewModel by viewModels()

    // View Binding untuk mengakses elemen UI
    private lateinit var binding: ActivityAddWisataBinding

    // Variabel untuk menyimpan URI gambar yang dipilih atau diambil
    private var imageUri: Uri? = null
    private var currentPhotoPath: String? = null

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi binding
        binding = ActivityAddWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Event listener untuk tombol unggah gambar
        binding.btnUploadGambar.setOnClickListener {
            pilihGambar()
        }

        // Event listener untuk tombol simpan
        binding.btnSave.setOnClickListener {
            // Validasi jika gambar belum dipilih
            if (imageUri == null) {
                Toast.makeText(this, "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mengubah input teks menjadi RequestBody untuk dikirim ke server
            val nama = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etNama.text.toString())
            val lokasi = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etLokasi.text.toString())
            val deskripsi = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etDeskripsi.text.toString())

            // Mendapatkan file dari URI yang dipilih
            val file = File(currentPhotoPath ?: getRealPathFromURI(imageUri!!))

            // Validasi jika file gambar tidak ditemukan
            if (!file.exists()) {
                Toast.makeText(this, "File gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Konversi file gambar menjadi MultipartBody untuk dikirim ke server
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val gambar = MultipartBody.Part.createFormData("gambar", file.name, requestFile)

            // Mengirim data ke ViewModel untuk ditambahkan ke database
            viewModel.tambahWisata(nama, lokasi, deskripsi, gambar) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    finish() // Menutup activity jika berhasil
                }
            }
        }
    }

    // Fungsi untuk memilih gambar dari galeri atau kamera
    private fun pilihGambar() {
        val intentGallery = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Menentukan hanya file gambar yang dapat dipilih
        }
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Membuat file untuk menyimpan gambar dari kamera
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Gagal membuat file gambar", Toast.LENGTH_SHORT).show()
            null
        }

        // Jika file berhasil dibuat, set URI untuk menyimpan gambar dari kamera
        photoFile?.also {
            imageUri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                it
            )
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }

        // Menampilkan pilihan antara galeri dan kamera
        val chooser = Intent.createChooser(intentGallery, "Pilih Gambar")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentCamera))
        startActivityForResult(chooser, 100)
    }

    // Menangani hasil pemilihan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                // Jika gambar dipilih dari galeri
                imageUri = data.data
                binding.ivPreview.setImageURI(imageUri)
                currentPhotoPath = getRealPathFromURI(imageUri!!)
            } else if (currentPhotoPath != null) {
                // Jika gambar diambil dari kamera
                imageUri = Uri.fromFile(File(currentPhotoPath))
                binding.ivPreview.setImageURI(imageUri)
            }
        }
    }

    // Fungsi untuk membuat file gambar baru untuk kamera
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    // Fungsi untuk mendapatkan path asli dari URI
    private fun getRealPathFromURI(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, filePathColumn, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return uri.path ?: ""
    }
}
