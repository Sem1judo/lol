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
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.activities.SavedUserPrefs
import com.oceanluc.kywheels.activities.WebRemoveViewActivity
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper

class SettingsFragment : CurrentFragment(false) {

    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var savedUserPrefs: SavedUserPrefs
    private lateinit var btnSound: SeekBar
    private lateinit var btnVibration: SeekBar
    private lateinit var privacy: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferencesHelper = PreferencesHelper(requireContext())
        setOrientation()
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSound = view.findViewById<SeekBar>(R.id.ocean_btn_sound)
        btnVibration = view.findViewById<SeekBar>(R.id.ocean_btn_vibration)
        privacy = view.findViewById(R.id.ocean_privacy_btn_open)

        privacy.setOnClickListener {
            switchToFragment(PrivacyPoliceFragment())
        }

        initView(view)

        initClick()
    }

    private fun setOrientation() {
        if (this.currentFragment) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun initView(view: View) {
        setUnderlinedText(view, R.id.ocean_tv_remove_account)

        editProgress()

        setSeekBarValue(view, R.id.ocean_btn_sound, preferencesHelper.sound)
        setSeekBarValue(view, R.id.ocean_btn_vibration, preferencesHelper.vibration)
    }

    private fun setUnderlinedText(view: View, textViewId: Int) {
        val textView = view.findViewById<TextView>(textViewId)
        val text = textView.text
        val ss = SpannableString(text)
        ss.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
    }

    private fun setSeekBarValue(view: View, seekBarId: Int, value: Int) {
        val seekBar = view.findViewById<SeekBar>(seekBarId)
        seekBar.progress = value
    }

    private fun editProgress() {

        btnSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                preferencesHelper.isSound = progress > 50
                preferencesHelper.sound = btnSound.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var a = 2
                while (a <= 7) {
                    a++
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var i = 1
                while (i <= 4) {
                    i++
                }
            }
        })

        btnVibration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                preferencesHelper.isVibration = progress > 50
                preferencesHelper.vibration = btnVibration.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var a = 2
                while (a <= 7) {
                    a++
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var i = 1
                while (i <= 4) {
                    i++
                }
            }
        })
    }

    private fun initClick() {

        if (preferencesHelper.isReg) {
            setupRemoveAccountClick()
        } else {
            view?.findViewById<View>(R.id.ocean_tv_remove_account)?.visibility = View.GONE
        }

        view?.findViewById<View>(R.id.ocean_btn_reset_score)?.setOnClickListener {
            resetAccount()
        }
    }

    private fun setupRemoveAccountClick() {
        view?.findViewById<View>(R.id.ocean_tv_remove_account)?.setOnClickListener {
            val intent = Intent(requireActivity(), WebRemoveViewActivity::class.java)
            intent.putExtra("url", "https://www.google.com/")
            startActivity(intent)
        }
    }

    private fun resetAccount() {
        preferencesHelper.balance = 15000
        Toast.makeText(
            requireContext(),
            "You have successfully reset your account!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun switchToFragment(fr: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fr)
            .commit()
    }
}
