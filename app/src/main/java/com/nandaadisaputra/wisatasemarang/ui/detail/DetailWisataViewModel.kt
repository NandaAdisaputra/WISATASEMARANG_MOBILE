package com.nandaadisaputra.wisatasemarang.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.model.DetailWisataResponse
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient.apiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailWisataViewModel : ViewModel() {
    // LiveData untuk menyimpan detail tempat wisata yang diambil dari API
    private val _wisataDetail = MutableLiveData<DetailWisataResponse?>()
    val wisataDetail: LiveData<DetailWisataResponse?> get() = _wisataDetail

    // LiveData untuk menyimpan status penghapusan tempat wisata
    private val _deleteStatus = MutableLiveData<Pair<Boolean, String>>()
    val deleteStatus: LiveData<Pair<Boolean, String>> get() = _deleteStatus

    /**
     * Mengambil detail wisata dari API berdasarkan ID wisata.
     * Jika ID tidak valid (-1), maka fungsi akan langsung berhenti.
     */
    fun fetchDetailWisata(id: Int) {
        if (id == -1) return // Jika ID tidak valid, tidak melakukan permintaan ke API

        viewModelScope.launch {
            // Menggunakan runCatching untuk menangani error dengan lebih baik
            runCatching { apiService.getDetailWisata(id) }
                .onSuccess { response ->
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody?.status == "success") {
                        // Jika data berhasil diambil, simpan ke LiveData
                        _wisataDetail.postValue(responseBody.data)
                    } else {
                        // Jika gagal, set LiveData menjadi null
                        _wisataDetail.postValue(null)
                    }
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _wisataDetail.postValue(null) // Set LiveData ke null jika terjadi kesalahan
                }
        }
    }

    /**
     * Menghapus data wisata berdasarkan ID.
     */
    fun deleteWisata(wisataId: Int) {
        viewModelScope.launch {
            // Menggunakan runCatching untuk menangani error dengan lebih baik
            runCatching { apiService.deleteWisata(wisataId) }
                .onSuccess { response ->
                    val responseBody = response.body()
                    val success = response.isSuccessful && responseBody?.status == "success"
                    // Mengupdate LiveData dengan status penghapusan dan pesan yang sesuai
                    _deleteStatus.postValue(Pair(success, responseBody?.message ?: if (success) "Data berhasil dihapus" else "Gagal menghapus data"))
                }
                .onFailure { exception ->
                    // Menangani berbagai jenis error yang mungkin terjadi selama proses penghapusan
                    val errorMessage = when (exception) {
                        is IOException -> "Terjadi kesalahan jaringan: ${exception.message}" // Kesalahan karena jaringan
                        is HttpException -> "Kesalahan server: ${exception.message}" // Kesalahan dari server API
                        else -> "Terjadi kesalahan: ${exception.localizedMessage}" // Kesalahan umum lainnya
                    }
                    // Mengupdate LiveData dengan status gagal dan pesan error
                    _deleteStatus.postValue(Pair(false, errorMessage))
                }
        }
    }
}
