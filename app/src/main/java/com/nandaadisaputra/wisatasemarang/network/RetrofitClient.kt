package com.nandaadisaputra.wisatasemarang.network

// Import library Retrofit untuk melakukan HTTP request dan parsing JSON
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient adalah objek singleton yang digunakan untuk mengatur dan menyediakan
 * instance Retrofit untuk melakukan komunikasi dengan API.
 */
object RetrofitClient {
    // URL dasar untuk API, menggunakan 10.0.2.2 untuk mengakses localhost dari emulator Android
    private const val BASE_URL = "http://10.0.2.2/"

    /**
     * Instance Retrofit yang dikonfigurasi dengan BASE_URL dan GsonConverterFactory
     * untuk mengonversi respons JSON menjadi objek Kotlin.
     */
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Menentukan URL dasar dari API
        .addConverterFactory(GsonConverterFactory.create()) // Menambahkan konverter JSON agar respons dapat diparsing ke objek Kotlin
        .build()

    /**
     * Instance ApiService yang digunakan untuk memanggil endpoint API.
     * Retrofit akan secara otomatis mengimplementasikan interface ApiService.
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java) // Membuat instance ApiService dari interface
}
