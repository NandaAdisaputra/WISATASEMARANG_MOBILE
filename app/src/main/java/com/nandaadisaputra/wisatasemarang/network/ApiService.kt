package com.nandaadisaputra.wisatasemarang.network

// Import library Retrofit untuk mendefinisikan endpoint API
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
}
