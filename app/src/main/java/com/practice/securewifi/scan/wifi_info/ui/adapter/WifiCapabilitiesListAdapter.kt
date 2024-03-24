package com.practice.securewifi.scan.wifi_info.ui.adapter

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.securewifi.databinding.WifiCapabilityItemBinding
import com.practice.securewifi.scan.wifi_info.model.WifiCapabilityItem

class WifiCapabilitiesListAdapter : ListAdapter<WifiCapabilityItem, WifiCapabilitiesListAdapter.WifiCapabilitiesViewHolder>(
    WifiCapabilitiesDiffItemCallback()
) {

    private var itemToExpand: Int? = null
    private var itemToCollapse: Int? = null
    private var expandedItems: MutableSet<Int> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiCapabilitiesViewHolder =
        WifiCapabilitiesViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: WifiCapabilitiesViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.capabilityShortName.text = item.shortName
        binding.capabilityFullName.text = item.shortExplanation
        binding.explanation.text = item.explanation
        if (itemToCollapse == position) {
            collapseExplanation(binding.explanation, position, binding.bottomSeparator)
        }
        binding.root.setOnClickListener {
            val itemCurPos = holder.bindingAdapterPosition
            // if not expanding or collapsing something right now
            if (itemToExpand == null && itemToCollapse == null) {
                val currentlyExpandedItem = expandedItems.find { it != itemCurPos }
                // if there is an expanded item that is not this item
                if (currentlyExpandedItem != null) {
                    // collapse it
                    itemToCollapse = currentlyExpandedItem
                    notifyItemChanged(currentlyExpandedItem)
                }
                if (expandedItems.contains(itemCurPos)) {
                    // if clicked item is expanded, collapse it
                    // only one item could be collapsed at a time
                    itemToCollapse = itemCurPos
                    collapseExplanation(binding.explanation, itemCurPos, binding.bottomSeparator)
                } else {
                    // else expand clicked item
                    itemToExpand = itemCurPos
                    expandExplanation(binding.explanation, itemCurPos, binding.bottomSeparator)
                }
            }
        }
    }

    class WifiCapabilitiesViewHolder(val binding: WifiCapabilityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): WifiCapabilitiesViewHolder {
                return WifiCapabilitiesViewHolder(
                    WifiCapabilityItemBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    private fun expandExplanation(explanation: TextView, itemPosition: Int, separator: View) {
        separator.isVisible = true
        val onAnimationEndCallback: () -> Unit = {
            itemToExpand = null
            expandedItems.add(itemPosition)
        }
        expand(explanation, onAnimationEndCallback)
    }

    private fun collapseExplanation(explanation: TextView, itemPosition: Int, separator: View) {
        val onAnimationEndCallback: () -> Unit = {
            itemToCollapse = null
            expandedItems.remove(itemPosition)
            separator.isVisible = false
        }
        collapse(explanation, onAnimationEndCallback)
    }

    private fun expand(v: View, onAnimationEndCallback: () -> Unit) {
        val targetHeight = measureViewHeight(v)
        v.isVisible = false
        v.requestLayout()

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE

        val va = ValueAnimator.ofInt(1, targetHeight)
        va.addUpdateListener { animation ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                onAnimationEndCallback()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        va.setDuration(300)
        va.interpolator = DecelerateInterpolator()
        va.start()
    }

    private fun measureViewHeight(view: View): Int {
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (view.parent as View).width, View.MeasureSpec.EXACTLY
        )
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0, View.MeasureSpec.UNSPECIFIED
        )
        view.measure(widthMeasureSpec, heightMeasureSpec)
        return view.measuredHeight
    }

    private fun collapse(v: View, onAnimationEndCallback: () -> Unit) {
        val initialHeight = measureViewHeight(v)
        v.isVisible = true
        v.requestLayout()
        val va = ValueAnimator.ofInt(initialHeight, 0)
        va.addUpdateListener { animation ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.visibility = View.GONE
                onAnimationEndCallback()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        va.setDuration(300)
        va.interpolator = DecelerateInterpolator()
        va.start()
    }
}