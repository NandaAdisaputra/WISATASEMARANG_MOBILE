package com.nandaadisaputra.wisatasemarang.ui

// Import library yang diperlukan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.model.TempatWisata
import com.nandaadisaputra.wisatasemarang.network.Pagination
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient
import kotlinx.coroutines.launch

// Kelas WisataViewModel yang bertanggung jawab mengelola data wisata
class WisataViewModel : ViewModel() {

    // MutableLiveData digunakan untuk menyimpan daftar tempat wisata yang bisa diperbarui
    private val _wisataData = MutableLiveData<List<TempatWisata>>()

    // LiveData bersifat read-only untuk dipantau oleh UI
    val wisataData: LiveData<List<TempatWisata>> get() = _wisataData

    // MutableLiveData untuk menyimpan informasi pagination dari API
    private val _pagination = MutableLiveData<Pagination>()
    val pagination: LiveData<Pagination> get() = _pagination

    // MutableLiveData untuk menyimpan pesan kesalahan jika terjadi error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /**
     * Fungsi untuk mengambil data wisata dari API berdasarkan halaman dan jumlah data per halaman.
     * @param page Nomor halaman yang ingin diambil.
     * @param limit Jumlah data per halaman.
     */
    fun getWisataData(page: Int, limit: Int) {
        // Menggunakan coroutine agar operasi jaringan berjalan secara asynchronous
        viewModelScope.launch {
            try {
                // Melakukan request ke API menggunakan Retrofit
                val response = RetrofitClient.apiService.getTempatWisata(page, limit)

                // Mengecek apakah request berhasil
                if (response.isSuccessful) {
                    // Jika response berhasil dan body tidak null, simpan data ke LiveData
                    response.body()?.let { body ->
                        _wisataData.postValue(body.data) // Menyimpan daftar wisata ke LiveData
                        _pagination.postValue(body.pagination) // Menyimpan informasi pagination
                    }
                } else {
                    // Jika request gagal, simpan pesan error
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Jika terjadi error, tangkap exception dan simpan pesan kesalahan
                _errorMessage.postValue("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    /**
     * Fungsi untuk mencari tempat wisata berdasarkan kata kunci.
     * @param keyword Kata kunci yang digunakan untuk pencarian.
     */
    fun searchWisata(keyword: String) {
        viewModelScope.launch {
            try {
                // Melakukan request ke API untuk pencarian data wisata
                val response = RetrofitClient.apiService.searchTempatWisata(keyword)

                // Mengecek apakah request berhasil
                if (response.isSuccessful) {
                    // Jika response berhasil dan body tidak null, perbarui LiveData
                    response.body()?.let { body ->
                        _wisataData.postValue(body.data) // Menyimpan hasil pencarian ke LiveData
                    }
                } else {
                    // Jika request gagal, simpan pesan error
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Jika terjadi error, tangkap exception dan simpan pesan kesalahan
                _errorMessage.postValue("Terjadi kesalahan: ${e.message}")
            }
        }
    }

}
