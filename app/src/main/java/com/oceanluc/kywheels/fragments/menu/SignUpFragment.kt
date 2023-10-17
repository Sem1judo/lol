package com.oceanluc.kywheels.fragments.menu

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper

class SignUpFragment : CurrentFragment(false) {

    private lateinit var etInputPhone: EditText
    private lateinit var tvAnonymousMode: TextView
    private lateinit var btnPlay: AppCompatImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setScreenOrientation()
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etInputPhone = view.findViewById(R.id.ocean_et_input_phone)
        tvAnonymousMode = view.findViewById(R.id.ocean_tv_anonymous_mode)
        btnPlay = view.findViewById(R.id.ocean_btn_play)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        underlineTextView(tvAnonymousMode)

        btnPlay.setOnClickListener {
            val phoneNumber = etInputPhone.text.toString()
            if (isValidPhoneNumber(phoneNumber)) {
                performRegistration(phoneNumber)
            } else {
                etInputPhone.error = "Invalid phone number"
            }
        }

        tvAnonymousMode.setOnClickListener {
            switchToFragment(AnonGamesFragment())
        }
    }

    private fun setScreenOrientation() {
        requireActivity().requestedOrientation = if (currentFragment) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun underlineTextView(textView: TextView) {
        val text = textView.text
        val ss = SpannableString(text)
        ss.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 9
    }

    private fun performRegistration(phoneNumber: String) {
        saveData()
        switchToFragment(AuthGamesFragment())
    }

    private fun saveData() {
        val sharedPreferences = PreferencesHelper(requireContext())
        sharedPreferences.isReg = true
        showToast("Registration successful")
    }

    private fun switchToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
