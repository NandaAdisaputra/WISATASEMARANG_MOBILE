package com.nandaadisaputra.wisatasemarang.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.network.ApiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class EditWisataViewModel(private val apiService: ApiService) : ViewModel() {

    /**
     * Fungsi untuk memperbarui data tempat wisata.
     * @param wisataId ID tempat wisata yang akan diperbarui
     * @param nama Nama tempat wisata dalam bentuk RequestBody
     * @param lokasi Lokasi tempat wisata dalam bentuk RequestBody
     * @param deskripsi Deskripsi tempat wisata dalam bentuk RequestBody
     * @param gambar Gambar tempat wisata dalam bentuk MultipartBody.Part (opsional)
     * @param callback Callback untuk menangani hasil pembaruan, mengembalikan status dan pesan
     */
    fun updateWisata(
        wisataId: Int,
        nama: RequestBody,
        lokasi: RequestBody,
        deskripsi: RequestBody,
        gambar: MultipartBody.Part?,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            // Menggunakan runCatching untuk menangani kesalahan secara lebih rapi
            runCatching {
                apiService.updateWisata(wisataId, nama, lokasi, deskripsi, gambar)
            }.onSuccess { response ->
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null && responseBody.success) {
                    // Jika pembaruan berhasil, kirimkan status true dan pesan keberhasilan
                    callback(true, responseBody.message ?: "Berhasil memperbarui data")
                } else {
                    // Jika gagal, kirimkan status false dan pesan kesalahan dari server
                    callback(false, responseBody?.message ?: "Gagal memperbarui data")
                }
            }.onFailure { exception ->
                // Menangani kesalahan yang terjadi selama proses pembaruan
                val errorMessage = when (exception) {
                    is IOException -> "Terjadi kesalahan jaringan: ${exception.message}" // Kesalahan karena jaringan
                    is HttpException -> "Kesalahan server: ${exception.message}" // Kesalahan dari server API
                    else -> "Terjadi kesalahan: ${exception.localizedMessage}" // Kesalahan umum lainnya
                }
                // Kirimkan status false dan pesan kesalahan
                callback(false, errorMessage)
            }
        }
    }
}
