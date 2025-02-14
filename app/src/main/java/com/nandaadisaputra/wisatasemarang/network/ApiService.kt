package com.nandaadisaputra.wisatasemarang.network

// Import library Retrofit untuk mendefinisikan endpoint API
import com.nandaadisaputra.wisatasemarang.model.ApiResponse
import com.nandaadisaputra.wisatasemarang.model.DeleteResponse
import com.nandaadisaputra.wisatasemarang.model.ResponseEditWisata
import com.nandaadisaputra.wisatasemarang.model.ResponseTambahWisata
import com.nandaadisaputra.wisatasemarang.model.ResponseWisata
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface `ApiService` digunakan untuk mendefinisikan endpoint API
 * yang akan digunakan dalam aplikasi menggunakan Retrofit.
 */
interface ApiService {

    /**
     * Mengambil daftar tempat wisata dengan paginasi.
     *
     * @param page Nomor halaman yang ingin diambil.
     * @param limit Jumlah data per halaman.
     * @param sort Urutan data (default: "desc" untuk terbaru di atas).
     * @return Response<ResponseWisata> berisi daftar tempat wisata dan informasi paginasi.
     */
    @GET("backend/read.php")
    suspend fun getTempatWisata(
        @Query("page") page: Int,   // Parameter untuk menentukan halaman
        @Query("limit") limit: Int,  // Parameter untuk menentukan jumlah data per halaman
        @Query("sort") sort: String = "desc" // Urutan data (default: terbaru di atas)
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
     * @param nama Nama tempat wisata (RequestBody untuk dikirim dalam multipart form).
     * @param lokasi Lokasi tempat wisata (RequestBody untuk dikirim dalam multipart form).
     * @param deskripsi Deskripsi tempat wisata (RequestBody untuk dikirim dalam multipart form).
     * @param gambar File gambar tempat wisata (MultipartBody.Part untuk mengunggah file).
     * @return Response<ResponseTambahWisata> berisi respons dari server setelah proses penyimpanan.
     */
    @Multipart
    @POST("backend/create.php")
    suspend fun addTempatWisata(
        @Part("nama") nama: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): Response<ResponseTambahWisata>

    /**
     * Memperbarui informasi tempat wisata berdasarkan ID.
     *
     * @param wisataId ID tempat wisata yang akan diperbarui.
     * @param nama Nama baru tempat wisata.
     * @param lokasi Lokasi baru tempat wisata.
     * @param deskripsi Deskripsi baru tempat wisata.
     * @param gambar (Opsional) Gambar baru tempat wisata jika ingin diperbarui.
     * @return Response<ResponseEditWisata> berisi respons setelah proses pembaruan.
     */
    @Multipart
    @POST("backend/update.php")
    suspend fun updateWisata(
        @Part("id") wisataId: Int,
        @Part("nama") nama: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part?
    ): Response<ResponseEditWisata>

    /**
     * Mengambil detail tempat wisata berdasarkan ID.
     *
     * @param id ID tempat wisata yang ingin ditampilkan detailnya.
     * @return Response<ApiResponse> berisi data tempat wisata secara lengkap.
     */
    @GET("backend/detail.php")
    suspend fun getDetailWisata(
        @Query("id") id: Int
    ): Response<ApiResponse>

    /**
     * Menghapus tempat wisata berdasarkan ID.
     *
     * @param wisataId ID tempat wisata yang akan dihapus.
     * @return Response<DeleteResponse> berisi status penghapusan.
     */
    @FormUrlEncoded
    @POST("backend/delete.php")
    suspend fun deleteWisata(
        @Field("id") wisataId: Int
    ): Response<DeleteResponse>
}
