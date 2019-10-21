package io.github.zwieback.familyfinance.core.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingHolder<B : ViewDataBinding> internal constructor(
    val binding: B
) : RecyclerView.ViewHolder(binding.root)
