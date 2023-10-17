package com.oceanluc.kywheels.fragments.games.zeusslots

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper

import com.oceanluc.kywheels.fragments.games.slots.SlotObjAdapter
import com.oceanluc.kywheels.fragments.games.slots.SlotObj
import com.oceanluc.kywheels.fragments.games.slots.SmoothLayoutManager
import com.oceanluc.kywheels.fragments.games.slots.getCurrentPosition
import kotlin.random.Random

class ZeusSlotsFragment : CurrentFragment(true) {

    private lateinit var slot1RecyclerView: RecyclerView
    private lateinit var slot2RecyclerView: RecyclerView
    private lateinit var slot3RecyclerView: RecyclerView
    private lateinit var slot4RecyclerView: RecyclerView

    private lateinit var adapter1: SlotObjAdapter
    private lateinit var adapter2: SlotObjAdapter
    private lateinit var adapter3: SlotObjAdapter
    private lateinit var adapter4: SlotObjAdapter

    private lateinit var item1: SlotObj
    private lateinit var item2: SlotObj
    private lateinit var item3: SlotObj
    private lateinit var item4: SlotObj

    private lateinit var winTextView: TextView
    private lateinit var balanceTextView: TextView
    private lateinit var betTextView: TextView

    private lateinit var plusImageButton: ImageButton
    private lateinit var minusImageButton: ImageButton
    private lateinit var playLayout: ConstraintLayout


    private var musicVolume = false
    private var isVibration = false

    private var mediaPlayerOnClickButton: MediaPlayer? = null
    private lateinit var sharedPreferences: PreferencesHelper

    private var scrollCount: Int = 0
    private var bet = 100
    private var balance = 0
    private var win = 0
    private var isBt = false

    companion object {
        private const val BET_ARG = "bet"
        private const val WIN_ARG = "win"
        private const val TOTAL = "total"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BET_ARG, bet)
        outState.putInt(WIN_ARG, win)
        outState.putInt(TOTAL, balance)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            bet = savedInstanceState.getInt(BET_ARG)
            win = savedInstanceState.getInt(WIN_ARG)
            balance = savedInstanceState.getInt(TOTAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_zeus_slots, container, false)

        initViews(view)

        setupScreenOrientation()
        initializePreferences()

        initAdapters()

        return view
    }

    private fun initViews(view: View) {
        winTextView = view.findViewById(R.id.ocean_winTextView)
        balanceTextView = view.findViewById(R.id.ocean_balanceTextView)
        betTextView = view.findViewById(R.id.ocean_tv_bid)
        plusImageButton = view.findViewById(R.id.ocean_btn_plus)
        minusImageButton = view.findViewById(R.id.ocean_btn_minus)
        playLayout = view.findViewById(R.id.ocean_playLayout)
        slot1RecyclerView = view.findViewById(R.id.ocean_slot1RecyclerView)
        slot2RecyclerView = view.findViewById(R.id.ocean_slot2RecyclerView)
        slot3RecyclerView = view.findViewById(R.id.ocean_slot3RecyclerView)
        slot4RecyclerView = view.findViewById(R.id.ocean_slot4RecyclerView)
    }

    private fun setupScreenOrientation() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        if (this.currentFragment) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    private fun initializePreferences() {
        sharedPreferences = PreferencesHelper(requireContext())
        balance = if (sharedPreferences.isReg) {
            sharedPreferences.balance
        } else {
            sharedPreferences.balanceAnon
        }
        isVibration = sharedPreferences.isVibration
        musicVolume = sharedPreferences.isSound
    }

    private fun initAdapters() {
        adapter1 = SlotObjAdapter(requireContext(), getGameItems(1))
        adapter2 = SlotObjAdapter(requireContext(), getGameItems(2))
        adapter3 = SlotObjAdapter(requireContext(), getGameItems(3))
        adapter4 = SlotObjAdapter(requireContext(), getGameItems(4))
    }

    private fun updateBet(newBet: Int) {
        if (newBet in 0..balance) {
            bet = newBet
            betTextView.text = bet.toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            bet = savedInstanceState.getInt(BET_ARG)
            win = savedInstanceState.getInt(WIN_ARG)
            balance = savedInstanceState.getInt(TOTAL)
        }

        setupWinAndBalance(win, balance)
        betTextView.text = bet.toString()

        val recyclerViewList = listOf(
            slot1RecyclerView to adapter1,
            slot2RecyclerView to adapter2,
            slot3RecyclerView to adapter3,
            slot4RecyclerView to adapter4
        )

        recyclerViewList.forEach { (recyclerView, adapter) ->
            recyclerView.setOnTouchListener { v, event -> true }
            setAdapter(recyclerView, adapter)
            setOnScrollListener(
                recyclerView,
                adapter,
                recyclerViewList.indexOfFirst { it.second == adapter } + 1)
        }

        plusImageButton.setOnClickListener {
            updateBet(bet + 100)
        }

        minusImageButton.setOnClickListener {
            updateBet(bet - 100)
        }

        playLayout.setOnClickListener {
            if (!isBt) {
                isBt = true

                when {
                    balance <= 0 -> showToast("Update your balance in settings")
                    balance < bet -> showToast("The bet cannot exceed the balance")
                    else -> {
                        activity?.requestedOrientation = getScreenOrientation(requireContext())
                        playClickMusic()
                        vibration()

                        if (bet != 0) {
                            balance -= bet
                            setupWinAndBalance(win, balance)
                            val isReg = sharedPreferences.isReg
                            if (isReg) {
                                sharedPreferences.balance = balance
                            } else {
                                sharedPreferences.balanceAnon = balance
                            }
                            scrollCount = 0
                            recyclerViewList.forEach { (recyclerView, _) -> scroll(recyclerView) }
                        }
                    }
                }

                Handler().postDelayed({
                    isBt = false
                }, 4500)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getGameItems(seed: Int): List<SlotObj> {
        val slotItems = listOf(
            R.drawable.first_item_first_slots to "K",
            R.drawable.second_item_first_slots to "Q",
            R.drawable.third_item_first_slots to "J",
            R.drawable.fourth_item_first_slots to "A",
            R.drawable.fifth_item_first_slots to "10"
        ).map { (drawableId, label) ->
            SlotObj(
                ResourcesCompat.getDrawable(resources, drawableId, requireContext().theme),
                label
            )
        }

        return slotItems.shuffled(Random(seed))
    }


    private fun setAdapter(recyclerView: RecyclerView, adapter: SlotObjAdapter) {
        recyclerView.apply {
            layoutManager = SmoothLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    private fun setOnScrollListener(
        recyclerView: RecyclerView,
        adapter: SlotObjAdapter,
        slotIndex: Int
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    when (slotIndex) {
                        1 -> item1 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        2 -> item2 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        3 -> item3 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                        4 -> item4 = adapter.getItemByPosition(recyclerView.getCurrentPosition())
                    }
                    scrollCount += 1
                    calculate()
                }
            }
        })
    }


    private fun calculate() {
        if (scrollCount == 4) {
            val currentGameItems = listOf(item1, item2, item3, item4)

            val boxCount = currentGameItems.count { it.id == "box" }
            val uniqueItems = currentGameItems.distinctBy { it.id }

            val coef = calculateCoefficient(currentGameItems.size - uniqueItems.size, boxCount)

            win = (bet * coef).toInt()
            balance += win

            updateBalancePreference()
            setupWinAndBalance(win, balance)
            playLayout.isEnabled = true
            enableFullSensorOrientation()
        }
    }

    private fun calculateCoefficient(itemCountDifference: Int, boxCount: Int): Double {
        val baseCoefficient = when (itemCountDifference) {
            1 -> 2.0
            2 -> 5.0
            3 -> 10.0
            else -> 0.0
        }

        return when (boxCount) {
            1 -> {
                baseCoefficient * 1.6
            }
            2 -> {
                8.0
            }
            3 -> {
                16.0
            }
            else -> {
                baseCoefficient
            }
        }
    }

    private fun updateBalancePreference() {
        val isReg = sharedPreferences.isReg
        if (isReg) {
            sharedPreferences.balance = balance
        } else {
            sharedPreferences.balanceAnon = balance
        }
    }

    private fun setupWinAndBalance(win: Int, balance: Int) {
        winTextView.text = "$win"
        balanceTextView.text = "$balance"
    }

    private fun enableFullSensorOrientation() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    private fun scroll(recyclerView: RecyclerView) {
        val currentPosition = recyclerView.getCurrentPosition()
        val randomPosition = generateRandomScrollPosition(currentPosition)
        recyclerView.smoothScrollToPosition(randomPosition)
    }

    private fun generateRandomScrollPosition(currentPosition: Int): Int {
        val exclusionRange = currentPosition - 1..currentPosition + 1
        var randomPosition = (0..50).random()
        while (randomPosition in exclusionRange) {
            randomPosition = (0..50).random()
        }
        return randomPosition
    }

    private fun playClickMusic() {
        if (sharedPreferences.isSound) {
            mediaPlayerOnClickButton =
                MediaPlayer.create(context, R.raw.app_src_main_res_raw_spinner)
            mediaPlayerOnClickButton?.start()
        }
    }

    private fun vibration() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        val duration = 200L
        val amplitude = VibrationEffect.DEFAULT_AMPLITUDE
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(duration, amplitude))
            } else {
                it.vibrate(duration)
            }
        }
    }

    private fun getScreenOrientation(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Surface.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}