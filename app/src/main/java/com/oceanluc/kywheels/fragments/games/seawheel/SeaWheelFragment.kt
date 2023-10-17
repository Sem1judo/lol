package com.oceanluc.kywheels.fragments.games.seawheel

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SeaWheelFragment : CurrentFragment(false), AnimationListener {

    private var list: ArrayList<Double> = arrayListOf()
    private var isClickAllowed = true
    private var balance = 15000
    private var minusBalance: Int? = null
    private var isSpin = false
    private lateinit var sharedPreferences: PreferencesHelper
    private var winInstance = 0
    var bet = 200
    private var sectors = arrayListOf(100, 0, 100, 75, 50, 100, 200, 100, 0, 150)
    private var countSectors = arrayListOf<Int>()
    private var randomSectorIndex = 0
    private var isClick = false

    private lateinit var balanceTextView: TextView
    private lateinit var winTextView: TextView
    private lateinit var tvBid: TextView
    private lateinit var ivWheel: ImageView
    private lateinit var btnMinus: AppCompatImageButton
    private lateinit var btnPlus: AppCompatImageButton
    private lateinit var playLayout: ConstraintLayout

    companion object {
        private const val BET_ARG = "bet"
        private const val WIN_ARG = "win"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BET_ARG, bet)
        outState.putInt(WIN_ARG, winInstance)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            bet = savedInstanceState.getInt(BET_ARG)
            winInstance = savedInstanceState.getInt(WIN_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_sea_wheel, container, false)

        orientationSetUp()

        balanceTextView = view.findViewById(R.id.ocean_balanceTextView)
        winTextView = view.findViewById(R.id.ocean_winTextView)
        tvBid = view.findViewById(R.id.ocean_tv_bid)
        ivWheel = view.findViewById(R.id.ocean_iv_wheel)
        btnMinus = view.findViewById(R.id.ocean_btn_minus)
        btnPlus = view.findViewById(R.id.ocean_btn_plus)
        playLayout = view.findViewById(R.id.ocean_playLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferencesHelper(requireContext())
        if (savedInstanceState != null) {
            bet = savedInstanceState.getInt(BET_ARG)
            winInstance = savedInstanceState.getInt(WIN_ARG)
        }
        generateSectorDegrees()
        setupView()
        initialize()
        clickTracking()
    }
    private fun orientationSetUp(){
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (this.currentFragment) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

    }
    private fun generateSectorDegrees() {
        val sectorDegree = 360 / sectors.size
        countSectors.addAll((1 until sectors.size).map { it * sectorDegree })
    }

    private fun generateRandomDegreeToSpinTo(): Int {
        return (360 * (sectors.size - 1)) + countSectors[randomSectorIndex]
    }


    private fun setupView() {
        var isReg = sharedPreferences.isReg
        if (isReg) {
            balance = saveBalance()
        }
        balanceTextView.text = balance.toString()
        winTextView.text = winInstance.toString()
        tvBid.text = bet.toString()
    }

    private fun clickTracking() {
        playLayout?.setOnClickListener {
            if (isClick || balanceTextView.text.toString().toInt() < tvBid.text.toString().toInt() || !isClickAllowed) {
                return@setOnClickListener
            }

            isClick = true
            click()
            isSpin = true
            spin()
            isClickAllowed = false
        }

        btnMinus?.setOnClickListener {
            val currentBid = tvBid.text.toString().toInt()
            if (currentBid > 100) {
                tvBid.text = (currentBid - 100).toString()
                bet = tvBid.text.toString().toInt()
            } else {
                Toast.makeText(
                    requireContext(),
                    "The minimum bet is 100",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnPlus?.setOnClickListener {
            val currentBid = tvBid.text.toString().toInt()
            tvBid.text = (currentBid + 100).toString()
            bet = tvBid.text.toString().toInt()
        }
    }

    private fun spin() {
        if (isClickAllowed) {
            randomSectorIndex = Random.nextInt(sectors.size - 1)

            val randomDegree = generateRandomDegreeToSpinTo()

            val rotateAnimation = RotateAnimation(
                0.5f,
                randomDegree.toFloat(),
                RotateAnimation.RELATIVE_TO_SELF,
                0.50f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.50f
            )

            vibration()
            rotateAnimation.duration = 3200
            rotateAnimation.fillAfter = true

            rotateAnimation.interpolator = DecelerateInterpolator()
            rotateAnimation.setAnimationListener(this)
            ivWheel?.startAnimation(rotateAnimation)
        }
    }


    private fun initialize() {
        list.reverse()
    }

    private fun click(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {

            Handler().postDelayed({ isClickAllowed = true }, 2000)
        }
        return current
    }

    private fun newBalance(win: Int) {
        val r = tvBid?.text.toString().toDouble().toInt()
        if (win == 2 || win == 1) {
            val bid = r * win
            val result = minusBalance?.plus(bid)!!.toInt()
            balance = result
            winTextView?.text = bid.toString()
            balanceTextView?.text = balance.toString()
            winInstance = winTextView?.text.toString().toDouble().toInt()
            saveBalance()
        } else {
            val result = minusBalance?.plus(win)
            if (result != null) {
                balance = result
            }
            winTextView?.text = win.toString()
            balanceTextView?.text = balance.toString()
            winInstance = winTextView?.text.toString().toDouble().toInt()
            saveBalance()
        }
    }

    private fun vibration() {
        val isVib = sharedPreferences.isVibration
        if (isVib) {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    private fun saveBalance(): Int {
        val userBalance = sharedPreferences.balance
        val regEmail = sharedPreferences.isReg
        if (regEmail) {
            if (isSpin) {
                sharedPreferences.balance = balance
            }
        }
        return userBalance
    }

    override fun onAnimationStart(p0: Animation?) {
        val bid = balance - tvBid.text.toString().toInt()
        minusBalance = bid
        balanceTextView.text = minusBalance.toString()
    }

    override fun onAnimationEnd(p0: Animation?) {
        val earnedCoins = sectors[(sectors.size - 1) - (randomSectorIndex)]
        newBalance(earnedCoins.toString().toInt())
        lifecycleScope.launch {
            delay(500)
            isClickAllowed = true
            isClick = false
        }
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }
}