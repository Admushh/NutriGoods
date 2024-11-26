package com.example.nutrigood

import android.content.Intent
import com.data.retrofit.ApiConfig
import kotlin.jvm.java
import kotlin.text.isNotEmpty

class ProfileFragment : androidx.fragment.app.Fragment() {

    private lateinit var mUserEmail: android.widget.TextView
    private lateinit var mUserName: android.widget.TextView
    private lateinit var mLogoutButton: android.widget.Button

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        // Inflate the fragment layout
        val view = inflater.inflate(com.example.nutrigood.R.layout.fragment_profile, container, false)

        // Initialize UI elements
        mUserEmail = view.findViewById(com.example.nutrigood.R.id.tv_user_email)
        mUserName = view.findViewById(com.example.nutrigood.R.id.tv_user_name)
        mLogoutButton = view.findViewById(com.example.nutrigood.R.id.btn_logout)

        // Fetch user data
        fetchUserData()

        // Set up Logout button functionality
        mLogoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }

    // Fetch the user data from SharedPreferences and backend API
    private fun fetchUserData() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            android.util.Log.d("ProfileFragment", "Token: $token")

            val apiService = ApiConfig.getApiService()
            apiService.getUserDetails("Bearer $token").enqueue(object :
                retrofit2.Callback<com.data.response.UserDetailsResponse> {
                override fun onResponse(call: retrofit2.Call<com.data.response.UserDetailsResponse>, response: retrofit2.Response<com.data.response.UserDetailsResponse>) {
                    android.util.Log.d("ProfileFragment", "Response Code: ${response.code()}")
                    android.util.Log.d("ProfileFragment", "Raw JSON Body: ${response.errorBody()?.string() ?: response.body()}") // Tambahkan log untuk JSON mentah

                    if (response.isSuccessful) {
                        val userDetails = response.body()
                        android.util.Log.d("ProfileFragment", "Parsed Response: $userDetails") // Log hasil parsing
                        if (userDetails != null) {
                            mUserEmail.text = userDetails.email
                            mUserName.text = userDetails.username
                        }
                    } else {
                        android.util.Log.e("ProfileFragment", "Failed to fetch user details: ${response.message()}")
                        android.widget.Toast.makeText(requireContext(), "Failed to fetch user details", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: retrofit2.Call<com.data.response.UserDetailsResponse>, t: Throwable) {
                    android.util.Log.e("ProfileFragment", "Network error: ${t.message}")
                    android.widget.Toast.makeText(requireContext(), "Network error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            android.widget.Toast.makeText(requireContext(), "No token found, please log in", android.widget.Toast.LENGTH_SHORT).show()
            logoutUser()
        }
    }


    // Logout the user
    private fun logoutUser() {
        // Clear token from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        android.widget.Toast.makeText(context, "Logged out successfully", android.widget.Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
