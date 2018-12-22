package com.example.who.testofbaseadapter.recycler

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.top = verticalSpaceHeight
        outRect.bottom = verticalSpaceHeight
    }
}
