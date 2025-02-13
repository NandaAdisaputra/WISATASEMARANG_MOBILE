package com.nandaadisaputra.wisatasemarang.network

// Import model TempatWisata yang digunakan dalam respons API
import com.nandaadisaputra.wisatasemarang.model.TempatWisata

/**
 * Data class ResponseWisata merepresentasikan struktur respons dari API saat mengambil data tempat wisata.
 *
 * @property status Menyimpan status respons (misalnya "success" atau "error").
 * @property message Menyimpan pesan dari server (misalnya "Data berhasil diambil").
 * @property data Berisi daftar tempat wisata yang dikembalikan oleh API.
 * @property pagination Menyimpan informasi tentang pagination (jumlah halaman, total data, dll.).
 */
data class ResponseWisata(
    val status: String,           // Status respons API
    val message: String,          // Pesan dari API
    val data: List<TempatWisata>, // List data tempat wisata
    val pagination: Pagination    // Informasi pagination untuk navigasi data
)
