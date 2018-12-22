package com.example.who.testofbaseadapter.recycler

import android.view.View
import android.widget.TextView
import com.example.who.testofbaseadapter.R
import com.example.who.testofbaseadapter.recycler.base.BaseViewHolder

class TestViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView) {

    private var item: T? = null
    override fun bind(obj: T?) {
        item = obj
        if (obj is String) {
            (obj as String).run {
                itemView.findViewById<TextView>(R.id.text).text = obj
            }
        }
    }

    override fun getCurrentItem() = item
}