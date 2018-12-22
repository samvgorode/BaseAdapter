package com.example.who.testofbaseadapter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import com.example.who.testofbaseadapter.recycler.base.BaseRecyclerAdapter
import com.example.who.testofbaseadapter.recycler.HorisontalSpaceItemDecoration
import com.example.who.testofbaseadapter.recycler.TestViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = BaseRecyclerAdapter<String>()
            .AdapterBuilder(this)
            .setList(arrayListOf("First", "Second"))
            .setHolder<TestViewHolder<String>>(R.layout.test_list_item)
            .build()?.also {
                it.setOnItemClickListener { item ->
                    // setup onclick if needed
                }
                testRecycler.run {
                    // setup recycler view
                    layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    addItemDecoration(HorisontalSpaceItemDecoration(11.toDp(this@MainActivity).toInt()))
                    this.adapter = it
                }
            }
        adapter?.updateAdapter(arrayListOf("First", "Second"))
    }

    private fun Int.toDp(context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

}


