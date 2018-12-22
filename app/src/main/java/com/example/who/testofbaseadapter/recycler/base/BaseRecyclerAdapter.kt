package com.example.who.testofbaseadapter.recycler.base

import android.content.Context
import android.os.SystemClock
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.who.testofbaseadapter.recycler.TestViewHolder
import java.lang.IllegalArgumentException

class BaseRecyclerAdapter<T> constructor() : RecyclerView.Adapter<BaseViewHolder<T>>() {

    private var holder: Class<*>? = null
    private var layoutResId: Int = 0
    private var filteredItems: MutableList<T> = mutableListOf()
    private var layoutInflater: LayoutInflater? = null
    private var onItemClickListener: ((item: T) -> Unit)? = null
    private var replaceItemListener: ((item: T) -> Unit)? = null
    private var lastClickStamp: Long = 0L

    private constructor(context: Context? = null, items: List<T>? = null, @LayoutRes layoutResId: Int) : this() {
        layoutInflater = LayoutInflater.from(context)
        this.layoutResId = layoutResId
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val itemView = layoutInflater?.inflate(layoutResId, parent, false)
        val currentViewHolder = createHolder(itemView)
        initClickListener(itemView, currentViewHolder)
        return currentViewHolder
    }

    private fun initClickListener(itemView: View?, currentViewHolder: BaseViewHolder<T>) {
        itemView?.setOnClickListener {
            debounceClick(currentViewHolder.getCurrentItem())
        }
    }

    fun setOnItemClickListener(onItemClickListener: ((item: T) -> Unit)?) {
        onItemClickListener?.run { this@BaseRecyclerAdapter.onItemClickListener = this }
    }

    fun setReplaceItemListener(replaceItemListener: ((item: T) -> Unit)?) {
        replaceItemListener?.run { this@BaseRecyclerAdapter.replaceItemListener = this }
    }

    private fun removeListeners(){
        this@BaseRecyclerAdapter.onItemClickListener = null
        this@BaseRecyclerAdapter.replaceItemListener = null
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        removeListeners()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun debounceClick(item: T?) {
        item?.run {
            val currentTimestamp = SystemClock.uptimeMillis()
            val lastTimestamp = lastClickStamp
            lastClickStamp = currentTimestamp
            if (currentTimestamp - lastTimestamp > 500) {
                onItemClickListener?.invoke(this)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        filteredItems[position]?.run {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    private fun replaceItemAt(position:Int, value:T){
        filteredItems[position] = value
        notifyItemChanged(position)
        replaceItemListener?.invoke(value)
    }

    override fun getItemId(position: Int): Long {
        return filteredItems[position].hashCode().toLong()
    }

    private fun createHolder(itemView: View?): BaseViewHolder<T> {
        return when (holder) {
            TestViewHolder::class.java -> TestViewHolder(
                itemView!!
            )
            else -> throw IllegalArgumentException("add view holder ${holder?.simpleName} to BaseRecyclerAdapter createHolder() method !!!!!")
        }
    }

    fun updateAdapter(list: List<T>?) {
        filteredItems = if (list != null) ArrayList(list) else ArrayList()
        notifyDataSetChanged()
    }

    inner class AdapterBuilder(private val context:Context) {
        private var items: List<T>? = null
        @LayoutRes
        var layoutResId: Int = 0
        var holder: Class<*>? = null
        fun setList(itemsIn: List<T>): AdapterBuilder {
            items = itemsIn
            return this
        }
        inline fun <reified VH : BaseViewHolder<T>> setHolder(@LayoutRes layoutResIdIn: Int): AdapterBuilder {
            holder = VH::class.java
            layoutResId = layoutResIdIn
            return this@AdapterBuilder
        }
        fun build(): BaseRecyclerAdapter<T>? {
            var adapter: BaseRecyclerAdapter<T>? = null
            if (holder != null && items != null && layoutResId != 0) {
                adapter = BaseRecyclerAdapter(context, items, layoutResId)
                adapter.holder = holder
            }
            return adapter
        }
    }
}