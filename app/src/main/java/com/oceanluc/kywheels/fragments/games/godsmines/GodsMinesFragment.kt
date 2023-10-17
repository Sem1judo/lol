package com.oceanluc.kywheels.fragments.games.godsmines

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.models.MinerModel
import com.oceanluc.kywheels.tools.CurrentFragment
import kotlin.random.Random

class GodsMinesFragment : CurrentFragment(false) {

    private lateinit var ivFirstItemMiner: ImageView
    private lateinit var ivSecondItemMiner: ImageView
    private lateinit var ivThirdItemMiner: ImageView
    private lateinit var ivFourthItemMiner: ImageView
    private lateinit var ivItemFiveMiner: ImageView
    private lateinit var ivSixthItemMiner: ImageView
    private lateinit var ivSeventhItemMiner: ImageView
    private lateinit var ivEightItemMiner: ImageView
    private lateinit var ivNinthItemMiner: ImageView
    private lateinit var tvTotal: TextView
    private lateinit var tvWin: TextView
    private lateinit var tvBid: TextView
    private lateinit var btnSpin: AppCompatImageButton
    private lateinit var rootView: View
    private lateinit var btn50: AppCompatImageButton
    private lateinit var btn100: AppCompatImageButton
    private lateinit var btn200: AppCompatImageButton
    private lateinit var btn500: AppCompatImageButton
    private lateinit var btn1000: AppCompatImageButton
    private lateinit var btn700: AppCompatImageButton

    private var list = arrayListOf<MinerModel>()
    private var win: Int = 0
    private var one = false
    private var two = false
    private var three = false
    private var four = false
    private var five = false
    private var six = false
    private var seven = false
    private var eight = false
    private var nine = false
    private var bid = 0
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        rootView = inflater.inflate(R.layout.fragment_gods_mines, container, false)

        init(rootView)

        return rootView
    }


    private fun setUpOrient() {
        if (this.currentFragment) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun init(rootView: View) {
        ivFirstItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_first_item_miner)
        ivSecondItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_second_item_miner)
        ivThirdItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_third_item_miner)
        ivFourthItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_fourth_item_miner)
        ivItemFiveMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_item_five_miner)
        ivSixthItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_sixth_item_miner)
        ivSeventhItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_seventh_item_miner)
        ivEightItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_eight_item_miner)
        ivNinthItemMiner = rootView.findViewById<ImageView>(R.id.ocean_iv_ninth_item_miner)
        tvTotal = rootView.findViewById<TextView>(R.id.ocean_tv_total)
        tvWin = rootView.findViewById<TextView>(R.id.ocean_tv_win)
        tvBid = rootView.findViewById<TextView>(R.id.ocean_tv_bid)
        btnSpin = rootView.findViewById(R.id.ocean_btn_spin)
        btn50 = rootView.findViewById(R.id.ocean_btn_500)
        tvBid = rootView.findViewById<TextView>(R.id.ocean_tv_bid)
        btn100 = rootView.findViewById(R.id.ocean_btn_100)
        btn200 = rootView.findViewById(R.id.ocean_btn_200)
        btn500 = rootView.findViewById(R.id.ocean_btn_500)
        btn1000 = rootView.findViewById(R.id.ocean_btn_1000)
        btn700 = rootView.findViewById(R.id.ocean_btn_700)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize your views
        init(view)

        // Initialize shared preferences
        sharedPreferences = requireContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)

        // Set up click listeners for the bid buttons
        setupBidClick(view)

        // Load the user's balance from shared preferences
        val balance = sharedPreferences.getInt("balance", 15000)
        tvTotal.text = balance.toString()

        // Load the miner images and set up click listeners
        loadMiner()

        // Set click listeners for each miner item
        ivFirstItemMiner.setOnClickListener { handleMinerClick(ivFirstItemMiner, one) }
        ivSecondItemMiner.setOnClickListener { handleMinerClick(ivSecondItemMiner, two) }
        ivThirdItemMiner.setOnClickListener { handleMinerClick(ivThirdItemMiner, three) }
        ivFourthItemMiner.setOnClickListener { handleMinerClick(ivFourthItemMiner, four) }
        ivItemFiveMiner.setOnClickListener { handleMinerClick(ivItemFiveMiner, five) }
        ivSixthItemMiner.setOnClickListener { handleMinerClick(ivSixthItemMiner, six) }
        ivSeventhItemMiner.setOnClickListener { handleMinerClick(ivSeventhItemMiner, seven) }
        ivEightItemMiner.setOnClickListener { handleMinerClick(ivEightItemMiner, eight) }
        ivNinthItemMiner.setOnClickListener { handleMinerClick(ivNinthItemMiner, nine) }

        // Handle the spin button click
        btnSpin.setOnClickListener { handleSpinButtonClick() }
    }

    // Function to handle clicks on miner items
    private fun handleMinerClick(imageView: ImageView, marker: Boolean) {
        if (bid == 0) {
            Toast.makeText(requireContext(), "Place a bet", Toast.LENGTH_SHORT).show()
            return
        }

        val bidAmount = tvBid.text.toString().toInt()
        val totalAmount = tvTotal.text.toString().toInt()

        if (bidAmount <= totalAmount && !marker) {
            val item = list[rand()]
            tvTotal.text = (totalAmount - bidAmount).toString()
            Glide.with(imageView).load(item.image).into(imageView)

            if (item.win == 0.0) {
                reset()
                win = 0
                tvWin.text = win.toString()
                Toast.makeText(requireContext(), "You've caught a mine, start over", Toast.LENGTH_SHORT).show()
            } else {
                val itemWin = (bidAmount * item.win).toInt()
                win = if (win == 0) itemWin else (win * item.win).toInt() + itemWin
                tvWin.text = win.toString()
            }

            when (imageView) {
                ivFirstItemMiner -> one = true
                ivSecondItemMiner -> two = true
                ivThirdItemMiner -> three = true
                ivFourthItemMiner -> four = true
                ivItemFiveMiner -> five = true
                ivSixthItemMiner -> six = true
                ivSeventhItemMiner -> seven = true
                ivEightItemMiner -> eight = true
                ivNinthItemMiner -> nine = true
            }
        } else if (marker) {
            Toast.makeText(requireContext(), "This item is already clicked", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Not enough coins", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to handle spin button click
    private fun handleSpinButtonClick() {
        tvTotal.text = (tvTotal.text.toString().toInt() + tvWin.text.toString().toInt()).toString()
        val newBalance = tvTotal.text.toString().toInt()
        sharedPreferences.edit().putInt("balance", newBalance).apply()
        Toast.makeText(requireContext(), newBalance.toString(), Toast.LENGTH_SHORT).show()
        reset()
        tvWin.text = "0"
    }


    private fun setupBidClick(view: View) {

        btn50.setOnClickListener {
            bid = 50
            tvBid.text = bid.toString()
        }


        btn100.setOnClickListener {
            bid = 100
            tvBid.text = bid.toString()
        }


        btn200.setOnClickListener {
            bid = 150
            tvBid.text = bid.toString()
        }


        btn500.setOnClickListener {
            bid = 200
            tvBid.text = bid.toString()
        }


        btn700.setOnClickListener {
            bid = 500
            tvBid.text = bid.toString()
        }


        btn1000.setOnClickListener {
            bid = 1000
            tvBid.text = bid.toString()
        }
    }

    private fun reset() {
        Glide.with(ivFirstItemMiner).load(R.drawable.background_saper).into(ivFirstItemMiner)
        Glide.with(ivSecondItemMiner).load(R.drawable.background_saper).into(ivSecondItemMiner)
        Glide.with(ivThirdItemMiner).load(R.drawable.background_saper).into(ivThirdItemMiner)
        Glide.with(ivFourthItemMiner).load(R.drawable.background_saper).into(ivFourthItemMiner)
        Glide.with(ivItemFiveMiner).load(R.drawable.background_saper).into(ivItemFiveMiner)
        Glide.with(ivSixthItemMiner).load(R.drawable.background_saper).into(ivSixthItemMiner)
        Glide.with(ivSeventhItemMiner).load(R.drawable.background_saper).into(ivSeventhItemMiner)
        Glide.with(ivEightItemMiner).load(R.drawable.background_saper).into(ivEightItemMiner)
        Glide.with(ivNinthItemMiner).load(R.drawable.background_saper).into(ivNinthItemMiner)

        one = false
        two = false
        three = false
        four = false
        five = false
        six = false
        seven = false
        eight = false
        nine = false
    }

    private fun loadMiner() {
        list.add(MinerModel(R.drawable.first_saper, 1.2))
        list.add(MinerModel(R.drawable.second_saper, 1.4))
        list.add(MinerModel(R.drawable.third_saper, 0.0))
    }

    private fun rand() = Random.nextInt(0, 3)

}