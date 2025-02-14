package com.nandaadisaputra.wisatasemarang.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient.apiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class TambahWisataViewModel : ViewModel() {

    /**
     * Fungsi untuk menambahkan data tempat wisata ke server.
     * @param nama Nama tempat wisata
     * @param lokasi Lokasi tempat wisata
     * @param deskripsi Deskripsi tempat wisata
     * @param gambar Gambar tempat wisata dalam format Multipart
     * @param callback Callback untuk mengembalikan hasil operasi (sukses/gagal)
     */
    fun tambahWisata(
        nama: RequestBody,
        lokasi: RequestBody,
        deskripsi: RequestBody,
        gambar: MultipartBody.Part,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            runCatching { apiService.addTempatWisata(nama, lokasi, deskripsi, gambar) }
                .onSuccess { response ->
                    val message = response.body()?.message ?: "Data berhasil ditambahkan!"
                    if (response.isSuccessful) {
                        callback(true, message) // Jika sukses, callback dengan status true
                    } else {
                        callback(false, "Gagal menambahkan data! Kode: ${response.code()} - ${response.message()}")
                    }
                }
                .onFailure { exception ->
                    callback(false, handleException(exception)) // Jika gagal, tangani error
                }
        }
    }

    /**
     * Fungsi untuk menangani berbagai jenis kesalahan (network, server, dll.)
     * @param exception Exception yang terjadi
     * @return Pesan kesalahan yang lebih mudah dipahami pengguna
     */
    private fun handleException(exception: Throwable): String {
        return when (exception) {
            is IOException -> "Jaringan bermasalah! Periksa koneksi internet Anda."
            is HttpException -> "Kesalahan server! ${exception.message}"
            else -> "Terjadi kesalahan: ${exception.localizedMessage}"
        }
    }
}
