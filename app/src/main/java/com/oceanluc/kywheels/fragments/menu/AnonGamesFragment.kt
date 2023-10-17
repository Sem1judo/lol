package com.oceanluc.kywheels.fragments.menu

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.OnItemClickListener
import com.oceanluc.kywheels.tools.PreferencesHelper
import com.oceanluc.kywheels.fragments.games.athenaslots.AthenaSlotsFragment
import com.oceanluc.kywheels.fragments.games.zeusslots.ZeusSlotsFragment
import com.oceanluc.kywheels.fragments.menu.adapteres.AuthUserGamesListAdapter
import com.oceanluc.kywheels.models.AuthUserGamesModel

class AnonGamesFragment : CurrentFragment(false), OnItemClickListener {

    private var isList = true
    private var preferencesHelper: PreferencesHelper? = null
    private lateinit var btnRegister: AppCompatImageButton;
    private lateinit var btnSetting: AppCompatImageButton;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anon_games, container, false)
        btnSetting = view.findViewById(R.id.ocean_btn_settings)
        btnRegister = view.findViewById(R.id.ocean_btn_register)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        preferencesHelper = PreferencesHelper(requireContext())
        setupOrientation()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSettingsButton(view)
        setupRecyclerView(view)

        setupRegisterButton(view)
    }

    private fun setupOrientation() {
        requireActivity().requestedOrientation = if (currentFragment) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun setupSettingsButton(view: View) {
        btnSetting.setOnClickListener {
            switchToFragment(SettingsFragment())
        }
    }

    private fun setupRecyclerView(view: View) {
        val adapter = AuthUserGamesListAdapter(this, requireContext())
        val anonList = listOf(
            AuthUserGamesModel(1, R.drawable.first_open_game, true),
            AuthUserGamesModel(2, R.drawable.second_open_gamee, true),
            AuthUserGamesModel(6, R.drawable.first_closed_game, false),
            AuthUserGamesModel(7, R.drawable.second_closed_gam, false),
            AuthUserGamesModel(8, R.drawable.third_closed_game, false)
        )
        adapter.submitList(anonList)
        isList = false
        view.findViewById<RecyclerView>(R.id.ocean_rv_games_list)?.adapter = adapter
    }

    private fun setupRegisterButton(view: View) {
        btnRegister.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignUpFragment())
                .commit()
        }
    }

    override fun onItemClick(model: AuthUserGamesModel) {
        when (model.id) {
            1 -> switchToFragment(ZeusSlotsFragment())
            2 -> switchToFragment(AthenaSlotsFragment())
        }
    }

    private fun switchToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
