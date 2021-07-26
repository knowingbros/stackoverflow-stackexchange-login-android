package com.reputationoverflow

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData


@BindingAdapter("app:visibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = (if (visible) View.VISIBLE else View.GONE)
}