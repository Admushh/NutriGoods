package com.example.nutrigood

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class ScanFragment : Fragment(R.layout.fragment_scan) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil referensi ke tombol yang baru saja ditambahkan
        val navigateButton: Button = view.findViewById(R.id.btn_navigate_to_form)

        // Menambahkan OnClickListener untuk mengganti fragment
        navigateButton.setOnClickListener {
            // Menavigasi ke FormFragment menggunakan FragmentTransaction
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, FormFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}
