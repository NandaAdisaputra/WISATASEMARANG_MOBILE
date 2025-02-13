package com.nandaadisaputra.wisatasemarang.model

/**
 * Data class TempatWisata merepresentasikan model data untuk tempat wisata.
 *
 * @property id ID unik tempat wisata.
 * @property nama Nama tempat wisata.
 * @property lokasi Alamat atau lokasi tempat wisata.
 * @property deskripsi Deskripsi singkat mengenai tempat wisata.
 * @property gambar URL gambar tempat wisata (opsional, bisa null).
 */
data class TempatWisata(
    val id: Int,         // ID unik dari tempat wisata
    val nama: String,    // Nama tempat wisata
    val lokasi: String,  // Lokasi atau alamat tempat wisata
    val deskripsi: String, // Deskripsi singkat tempat wisata
    val gambar: String?  // URL gambar tempat wisata (bisa null jika tidak tersedia)
)
