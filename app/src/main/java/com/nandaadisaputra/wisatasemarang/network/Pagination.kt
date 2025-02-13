package com.nandaadisaputra.wisatasemarang.network

/**
 * Data class Pagination digunakan untuk menyimpan informasi tentang sistem paginasi
 * dalam respons API.
 *
 * @property current_page Halaman saat ini yang sedang ditampilkan.
 * @property total_pages Jumlah total halaman yang tersedia.
 * @property total_data Jumlah total data yang tersedia dalam API.
 */
data class Pagination(
    val current_page: Int, // Nomor halaman saat ini
    val total_pages: Int,  // Total halaman yang tersedia
    val total_data: Int    // Total jumlah data yang tersedia
)
