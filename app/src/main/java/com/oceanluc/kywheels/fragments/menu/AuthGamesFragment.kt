package com.oceanluc.kywheels.fragments.menu


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.OnItemClickListener
import com.oceanluc.kywheels.tools.PreferencesHelper
import com.oceanluc.kywheels.fragments.games.athenaslots.AthenaSlotsFragment
import com.oceanluc.kywheels.fragments.games.godsmines.GodsMinesFragment
import com.oceanluc.kywheels.fragments.games.seawheel.SeaWheelFragment
import com.oceanluc.kywheels.fragments.games.underwaterfortune.UnderwaterFortuneFragment
import com.oceanluc.kywheels.fragments.games.zeusslots.ZeusSlotsFragment
import com.oceanluc.kywheels.fragments.menu.adapteres.AuthUserGamesListAdapter
import com.oceanluc.kywheels.models.AuthUserGamesModel

class AuthGamesFragment : CurrentFragment(false), OnItemClickListener {

    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var btnSetting: AppCompatImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth_games, container, false)
        preferencesHelper = PreferencesHelper(requireContext())

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        btnSetting = view.findViewById(R.id.ocean_btn_settings)

        orientationSetUp()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSetting.setOnClickListener {
            switchToFragment(SettingsFragment())
        }

        setupView(view)

        setupRecyclerView(view)
    }

    private fun orientationSetUp() {
        requireActivity().requestedOrientation = if (currentFragment) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun setupView(view: View) {
        setupWin()
        setupTextView(view, R.id.ocean_tv_balance, preferencesHelper.balance.toString())
        setupTextView(view, R.id.ocean_tv_level, preferencesHelper.level.toString())
    }

    private fun setupTextView(view: View, textViewId: Int, text: String) {
        view.findViewById<TextView>(textViewId)?.text = text
    }

    private fun setupWin() {
        if (preferencesHelper.win >= 50) {
            preferencesHelper.win = 0
            preferencesHelper.level++
        }
    }

    private fun setupRecyclerView(view: View) {
        val adapter = AuthUserGamesListAdapter(this, requireContext())

        val authUserGamesList = listOf(
            AuthUserGamesModel(1, R.drawable.first_open_game, true),
            AuthUserGamesModel(2, R.drawable.second_open_gamee, true),
            AuthUserGamesModel(3, R.drawable.third_open_game, true),
            AuthUserGamesModel(4, R.drawable.fourth_open_game, true),
            AuthUserGamesModel(5, R.drawable.fifth_open_game, true),
            AuthUserGamesModel(11, R.drawable.first_closed_game, false),
            AuthUserGamesModel(111, R.drawable.second_closed_gam, false),
            AuthUserGamesModel(4324, R.drawable.third_closed_game, false)
        )

        adapter.submitList(authUserGamesList)
        view.findViewById<RecyclerView>(R.id.ocean_rv_games_list)?.adapter = adapter
    }

    override fun onItemClick(model: AuthUserGamesModel) {
        val fragment = when (model.id) {
            1 -> ZeusSlotsFragment()
            2 -> AthenaSlotsFragment()
            3 -> SeaWheelFragment()
            4 -> GodsMinesFragment()
            5 -> UnderwaterFortuneFragment()
            else -> return
        }
        switchToFragment(fragment)
    }

    private fun switchToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
