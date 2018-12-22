package com.example.who.testofbaseadapter.recycler.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val context: Context
        get() = itemView.context

    abstract fun bind(obj: T?)

    abstract fun getCurrentItem():T?

    open var updateAdapterPosition:((Int, T)->Unit)? = null
}