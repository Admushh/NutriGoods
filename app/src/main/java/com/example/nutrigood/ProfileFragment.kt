package com.example.nutrigood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var mUserEmail: TextView
    private lateinit var mUserName: TextView
    private lateinit var mLogoutButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize UI elements
        mUserEmail = view.findViewById(R.id.tv_user_email)
        mUserName = view.findViewById(R.id.tv_user_name)
        mLogoutButton = view.findViewById(R.id.btn_logout)

        // Fetch user data
        fetchUserData()

        // Set up Logout button functionality
        mLogoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }

    // Fetch the user data from Firebase Auth and Firestore
    private fun fetchUserData() {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        if (firebaseUser != null) {
            // Display user's email from Firebase Authentication
            mUserEmail.text = firebaseUser.email

            // Fetch username from Firestore
            val userId = firebaseUser.uid
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Fetch username from Firestore document
                        val userName = document.getString("username") ?: "No Name Provided"
                        mUserName.text = userName
                    } else {
                        Log.d("ProfileFragment", "No such document")
                        mUserName.text = "No Name Provided"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileFragment", "Error getting documents: ", exception)
                    mUserName.text = "Error fetching username"
                }
        }
    }

    // Logout the user
    private fun logoutUser() {
        firebaseAuth.signOut()
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Redirect to LoginActivity
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
