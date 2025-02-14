package com.nandaadisaputra.wisatasemarang.ui.read

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.model.TempatWisata
import com.nandaadisaputra.wisatasemarang.model.Pagination
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient.apiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {

    // LiveData untuk menyimpan daftar tempat wisata
    private val _wisataData = MutableLiveData<List<TempatWisata>>()
    val wisataData: LiveData<List<TempatWisata>> get() = _wisataData

    // LiveData untuk menyimpan informasi tentang pagination (halaman dan jumlah data)
    private val _pagination = MutableLiveData<Pagination>()
    val pagination: LiveData<Pagination> get() = _pagination

    // LiveData untuk menyimpan pesan kesalahan jika terjadi error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /**
     * Fungsi untuk mengambil data tempat wisata dari API berdasarkan halaman dan jumlah data per halaman.
     * @param page Halaman yang ingin diambil
     * @param limit Jumlah data per halaman
     */
    fun getWisataData(page: Int, limit: Int) {
        viewModelScope.launch {
            // Menggunakan runCatching untuk menangani error dengan lebih rapi
            runCatching { apiService.getTempatWisata(page, limit) }
                .onSuccess { response ->
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        // Jika berhasil, update LiveData dengan data yang diterima
                        _wisataData.postValue(responseBody.data)
                        _pagination.postValue(responseBody.pagination)
                    } else {
                        // Jika gagal, simpan pesan kesalahan ke LiveData
                        _errorMessage.postValue("Gagal mengambil data: ${response.message()}")
                    }
                }
                .onFailure { exception ->
                    // Menangani error yang terjadi
                    _errorMessage.postValue(handleException(exception))
                }
        }
    }

    /**
     * Fungsi untuk mencari tempat wisata berdasarkan kata kunci.
     * @param keyword Kata kunci yang digunakan untuk pencarian
     */
    fun searchWisata(keyword: String) {
        viewModelScope.launch {
            runCatching { apiService.searchTempatWisata(keyword) }
                .onSuccess { response ->
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        // Jika berhasil, update LiveData dengan hasil pencarian
                        _wisataData.postValue(responseBody.data)
                    } else {
                        // Jika gagal, simpan pesan kesalahan
                        _errorMessage.postValue("Gagal mencari data: ${response.message()}")
                    }
                }
                .onFailure { exception ->
                    // Menangani error yang terjadi saat pencarian
                    _errorMessage.postValue(handleException(exception))
                }
        }
    }

    /**
     * Fungsi untuk menangani berbagai jenis error yang mungkin terjadi.
     * @param exception Error yang terjadi
     * @return Pesan kesalahan yang lebih informatif
     */
    private fun handleException(exception: Throwable): String {
        return when (exception) {
            is IOException -> "Terjadi kesalahan jaringan: ${exception.message}" // Kesalahan karena jaringan
            is HttpException -> "Kesalahan server: ${exception.message}" // Kesalahan dari server API
            else -> "Terjadi kesalahan: ${exception.localizedMessage}" // Kesalahan umum lainnya
        }
    }
}
