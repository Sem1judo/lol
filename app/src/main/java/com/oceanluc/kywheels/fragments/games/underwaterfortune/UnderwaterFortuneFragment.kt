package com.oceanluc.kywheels.fragments.games.underwaterfortune

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.oceanluc.kywheels.R
import com.oceanluc.kywheels.databinding.FragmentUnderwaterFortuneBinding
import com.oceanluc.kywheels.models.OneOfFourModel
import com.oceanluc.kywheels.tools.CurrentFragment
import com.oceanluc.kywheels.tools.PreferencesHelper
import kotlin.random.Random

class UnderwaterFortuneFragment : CurrentFragment(false) {

    private var list = mutableListOf<OneOfFourModel>()
    private var currentItem = 0.0
    private var clickItem = false
    private var currentClick = 1
    private var bid = 0
    private lateinit var sharedPreferences: PreferencesHelper
    private var isBid = false

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        rootView = inflater.inflate(R.layout.fragment_underwater_fortune, container, false)
        return rootView
    }

    private fun orientationSetUp() {
        requireActivity().requestedOrientation = if (this.currentFragment) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferencesHelper(requireContext())
        rootView.findViewById<TextView>(R.id.ocean_tv_total)?.text =
            sharedPreferences.balance.toString()
        clickTracking()
    }
    @SuppressLint("SetTextI18n")
    private fun clickTracking() {
        setupBidClick()

        val itemViews = listOf(
            R.id.ocean_iv_first_item,
            R.id.ocean_iv_second_item,
            R.id.ocean_iv_third_item,
            R.id.ocean_iv_fourth_item
        )

        itemViews.forEachIndexed { index, viewId ->
            rootView?.findViewById<ImageView>(viewId)?.setOnClickListener {
                handleItemClick(index)
            }
        }

        rootView?.findViewById<AppCompatImageButton>(R.id.ocean_btn_spin)?.setOnClickListener {
            handleSpinClick()
        }
    }

    private fun handleItemClick(index: Int) {
        if (clickItem) {
            currentClick = index + 1
            if (currentItem != 0.0) {
                mixItem()
                val winnings = bid * currentItem
                showToast("Your winnings: $winnings")
                updateTotalAndBalance(winnings)
                clickItem = false
                isBid = false
            } else {
                showToast("Place a bet")
            }
        }
    }

    private fun handleSpinClick() {
        if (!isBid) {
            if (bid != 0) {
                if (totalBalance() <= 0) {
                    resetBalance()
                    showToast("Update your balance")
                } else {
                    resetItemViews()
                    deductBidFromTotal()
                    isBid = true
                    clickItem = true
                    showToast("The bet is placed\nChoose a box!")
                }
            } else {
                showToast("Place a bid")
            }
        } else {
            showToast("You have already placed a bid, select a box")
        }
    }

    private fun updateTotalAndBalance(winnings: Double) {
        val totalTextView = rootView?.findViewById<TextView>(R.id.ocean_tv_total)
        val winTextView = rootView?.findViewById<TextView>(R.id.ocean_tv_win)

        totalTextView?.text = (totalBalance() + winnings).toInt().toString()
        sharedPreferences.balance = totalTextView?.text.toString().toDouble().toInt()
        winTextView?.text = winnings.toInt().toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun totalBalance(): Int {
        val totalTextView = rootView?.findViewById<TextView>(R.id.ocean_tv_total)
        return totalTextView?.text.toString().toInt()
    }

    private fun resetBalance() {
        sharedPreferences.balance = 15000
        rootView?.findViewById<TextView>(R.id.ocean_tv_total)?.text = "15000"
    }


    private fun resetItemViews() {
        val itemViews = listOf(
            rootView?.findViewById<ImageView>(R.id.ocean_iv_first_item),
            rootView?.findViewById<ImageView>(R.id.ocean_iv_second_item),
            rootView?.findViewById<ImageView>(R.id.ocean_iv_third_item),
            rootView?.findViewById<ImageView>(R.id.ocean_iv_fourth_item)
        )

        itemViews.forEach { item ->
            item?.let {
                Glide.with(it).load(R.drawable.background_one).into(it)
            }
        }
    }

    private fun deductBidFromTotal() {
        rootView?.findViewById<TextView>(R.id.ocean_tv_total)?.text =
            (totalBalance() - bid).toString()
    }

    private fun mixItem() {
        val itemViews = listOf(
            rootView.findViewById(R.id.ocean_iv_first_item),
            rootView.findViewById(R.id.ocean_iv_second_item),
            rootView.findViewById(R.id.ocean_iv_third_item),
            rootView.findViewById<ImageView>(R.id.ocean_iv_fourth_item)
        )
        val itemIndexes = itemViews.indices.toMutableList()

        itemIndexes.remove(currentClick - 1)
        val randomIndex = itemIndexes.random()
        val randomItemView = itemViews[randomIndex]

        currentItem = list[randomIndex].bid

        chooseItem(randomItemView, list[randomIndex].image)
    }

    private fun chooseItem(item: ImageView, image: Int) {
        item.startAnimation(anim())
        val handler = Handler()
        handler.postDelayed({
            Glide.with(item).load(image).into(item)
            item.startAnimation(reverseAnim())
        }, 500)
    }

    private fun anim(): Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide)

    private fun reverseAnim(): Animation =
        AnimationUtils.loadAnimation(requireContext(), R.anim.reverse_slide)

    private fun addData() {
        list.add(OneOfFourModel(R.drawable.first_item, 1.5, 0))
        list.add(OneOfFourModel(R.drawable.second_item, 2.0, 1))
        list.add(OneOfFourModel(R.drawable.third_item, 0.0, 2))
    }

    private fun setupBidClick() {
        val bidButtons = listOf(
            rootView.findViewById(R.id.ocean_btn_50),
            rootView.findViewById(R.id.ocean_btn_100),
            rootView.findViewById(R.id.ocean_btn_200),
            rootView.findViewById(R.id.ocean_btn_1000),
            rootView.findViewById(R.id.ocean_btn_700),
            rootView.findViewById<AppCompatImageButton>(R.id.ocean_btn_500)
        )

        bidButtons.forEachIndexed { index, button ->
            button?.setOnClickListener {
                bid = when (index) {
                    0 -> 50
                    1 -> 100
                    2 -> 200
                    3 -> 1000
                    4 -> 700
                    5 -> 500
                    else -> 0
                }
                rootView.findViewById<TextView>(R.id.ocean_tv_bid)?.text = bid.toString()
            }
        }
    }
}
