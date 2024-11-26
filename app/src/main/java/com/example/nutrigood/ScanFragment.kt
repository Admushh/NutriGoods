package com.example.nutrigood

import androidx.fragment.app.FragmentTransaction

class ScanFragment : androidx.fragment.app.Fragment(com.example.nutrigood.R.layout.fragment_scan) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil referensi ke tombol yang baru saja ditambahkan
        val navigateButton: android.widget.Button = view.findViewById(com.example.nutrigood.R.id.btn_navigate_to_form)

        // Menambahkan OnClickListener untuk mengganti fragment
        navigateButton.setOnClickListener {
            // Menavigasi ke FormFragment menggunakan FragmentTransaction
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(com.example.nutrigood.R.id.fragment_container, FormFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}
