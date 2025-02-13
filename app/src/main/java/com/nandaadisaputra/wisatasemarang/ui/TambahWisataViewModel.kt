import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nandaadisaputra.wisatasemarang.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException

// ViewModel untuk mengelola penambahan data wisata
class TambahWisataViewModel : ViewModel() {
    // Fungsi untuk menambah data wisata ke server
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun tambahWisata(
        nama: RequestBody,
        lokasi: RequestBody,
        deskripsi: RequestBody,
        gambar: MultipartBody.Part,
        callback: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.addTempatWisata(nama, lokasi, deskripsi, gambar)
                if (response.isSuccessful) {
                    callback(true, "Data berhasil ditambahkan!")
                } else {
                    callback(false, "Gagal menambahkan data! Kode: ${response.code()}")
                }
            } catch (e: IOException) {
                callback(false, "Jaringan bermasalah! Periksa koneksi internet Anda.")
            } catch (e: HttpException) {
                callback(false, "Kesalahan server! ${e.message}")
            }
        }
    }
}
