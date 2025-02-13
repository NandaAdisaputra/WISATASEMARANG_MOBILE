package com.nandaadisaputra.wisatasemarang.network

// Import library Retrofit untuk mendefinisikan endpoint API
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface ApiService digunakan untuk mendefinisikan endpoint API yang akan digunakan
 * dalam aplikasi menggunakan Retrofit.
 */
interface ApiService {

    /**
     * Mengambil daftar tempat wisata dengan paginasi.
     *
     * @param page Nomor halaman yang ingin diambil.
     * @param limit Jumlah data per halaman.
     * @return Response<ResponseWisata> berisi daftar tempat wisata dan informasi paginasi.
     */
    @GET("backend/read.php")
    suspend fun getTempatWisata(
        @Query("page") page: Int,   // Parameter untuk menentukan halaman
        @Query("limit") limit: Int  // Parameter untuk menentukan jumlah data per halaman
    ): Response<ResponseWisata>

    /**
     * Melakukan pencarian tempat wisata berdasarkan kata kunci.
     *
     * @param keyword Kata kunci yang digunakan untuk mencari tempat wisata.
     * @return Response<ResponseWisata> berisi daftar tempat wisata yang sesuai dengan kata kunci.
     */
    @GET("backend/search.php")
    suspend fun searchTempatWisata(
        @Query("q") keyword: String // Parameter untuk kata kunci pencarian
    ): Response<ResponseWisata>

    /**
     * Menambahkan tempat wisata baru.
     *
     * @param nama Nama tempat wisata.
     * @param lokasi Lokasi tempat wisata.
     * @param deskripsi Deskripsi tempat wisata.
     * @param gambar File gambar tempat wisata.
     * @return Response dari server setelah proses penyimpanan.
     */
    @Multipart
    @POST("backend/create.php")
    suspend fun addTempatWisata(
        @Part("nama") nama: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): Response<ResponseTambahWisata>
}
