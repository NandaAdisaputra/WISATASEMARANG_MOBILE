package com.nandaadisaputra.wisatasemarang.model

// Import library untuk deserialisasi JSON menggunakan Gson
import com.google.gson.annotations.SerializedName

/**
 * Data class ResponseWisata digunakan untuk merepresentasikan respons dari API
 * ketika mengambil daftar tempat wisata.
 *
 * @property status Menyimpan status respons dari API (contoh: "success" atau "error").
 * @property message Berisi pesan dari API, misalnya "Data berhasil diambil".
 * @property data Daftar objek TempatWisata yang dikembalikan oleh API.
 * @property pagination Berisi informasi terkait pagination (jumlah halaman, total data, dll.).
 */
data class ResponseWisata(
    val status: String,           // Status respons API (contoh: "success" atau "error")
    val message: String,          // Pesan dari API terkait respons
    val data: List<TempatWisata>, // List data tempat wisata yang dikembalikan API
    val pagination: Pagination    // Informasi pagination jika tersedia
)

/**
 * Data class ResponseTambahWisata digunakan untuk menangani respons API
 * setelah menambahkan tempat wisata baru.
 *
 * @property success Menunjukkan apakah permintaan berhasil (true/false).
 * @property message Berisi pesan dari API terkait proses penambahan.
 * @property data Objek TempatWisata yang baru ditambahkan.
 */
data class ResponseTambahWisata(
    val success: Boolean,         // Indikator keberhasilan proses penambahan
    val message: String,          // Pesan status dari API
    val data: TempatWisata        // Data tempat wisata yang baru ditambahkan
)

/**
 * Data class ResponseEditWisata digunakan untuk menangani respons API
 * setelah memperbarui data tempat wisata.
 *
 * @property success Menunjukkan apakah proses edit berhasil.
 * @property message Berisi pesan dari API terkait proses pengeditan.
 * @property data Objek TempatWisata yang telah diperbarui.
 */
data class ResponseEditWisata(
    val success: Boolean,         // Indikator keberhasilan proses pengeditan
    val message: String,          // Pesan status dari API
    val data: TempatWisata        // Data tempat wisata yang telah diperbarui
)

/**
 * Data class DetailWisataResponse digunakan untuk menyimpan detail tempat wisata
 * yang dikembalikan oleh API berdasarkan ID tertentu.
 *
 * @property id ID unik tempat wisata.
 * @property nama Nama tempat wisata.
 * @property lokasi Lokasi tempat wisata.
 * @property deskripsi Deskripsi tempat wisata.
 * @property gambar URL gambar dari tempat wisata.
 */
data class DetailWisataResponse(
    val id: Int,                  // ID unik dari tempat wisata
    val nama: String,             // Nama tempat wisata
    val lokasi: String,           // Lokasi tempat wisata
    val deskripsi: String,        // Deskripsi tentang tempat wisata
    val gambar: String            // URL gambar tempat wisata
)

/**
 * Data class ApiResponse digunakan untuk menangani respons API
 * yang mengembalikan data spesifik, misalnya detail tempat wisata.
 *
 * @property status Status dari respons API.
 * @property data Data yang berisi detail tempat wisata.
 */
data class ApiResponse(
    val status: String,           // Status dari respons API (contoh: "success" atau "error")
    val data: DetailWisataResponse // Data yang dikembalikan berupa detail tempat wisata
)

/**
 * Data class DeleteResponse digunakan untuk menangani respons API
 * setelah menghapus tempat wisata.
 *
 * @property status Status dari respons API terkait penghapusan data.
 * @property message Pesan dari API mengenai hasil penghapusan.
 */
data class DeleteResponse(
    val status: String,           // Status dari proses penghapusan (contoh: "success" atau "error")
    val message: String           // Pesan dari API terkait hasil penghapusan
)
