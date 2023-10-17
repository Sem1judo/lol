package com.oceanluc.kywheels.fragments.menu

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.activities.WebViewActivity
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper

class PrivacyPoliceFragment : CurrentFragment(false) {

    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var tvPrivacy: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_privacy_police, container, false)
        tvPrivacy = view.findViewById(R.id.ocean_tv_privacy)
        screenSetUp()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesHelper = PreferencesHelper(requireContext())
        setupPrivacyText(view)
        initBtn(view)
    }

    private fun screenSetUp() {
        requireActivity().requestedOrientation = if (currentFragment) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun setupPrivacyText(view: View) {
        val text = tvPrivacy.text
        val underlinedText = createUnderlinedText(text)
        setUnderlinedText(tvPrivacy, underlinedText)

        initClick()
    }

    private fun createUnderlinedText(text: CharSequence): Spannable {
        val underlinedText = SpannableString(text)
        underlinedText.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return underlinedText
    }

    private fun setUnderlinedText(tvPrivacy: TextView, underlinedText: Spannable) {
        tvPrivacy.text = underlinedText
    }

    private fun initClick() {
        tvPrivacy?.setOnClickListener {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("url", "https://www.google.com/")
            startActivity(intent)
        }
    }


    private fun initBtn(view: View) {
        val btnYes = view.findViewById<AppCompatImageButton>(R.id.ocean_btn_yes)
        val btnNo = view.findViewById<AppCompatImageButton>(R.id.ocean_btn_no)

        btnYes.setOnClickListener {
            val fragment = if (preferencesHelper.isReg) {
                AuthGamesFragment()
            } else {
                SignUpFragment()
            }
            switchToFragment(fragment)
        }

        btnNo.setOnClickListener {
            showPolicyAcceptanceMessage()
        }
    }

    private fun switchToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showPolicyAcceptanceMessage() {
        Toast.makeText(
            requireContext(),
            "To use the app, you must accept the policy",
            Toast.LENGTH_SHORT
        ).show()
    }
}
