package com.lk.memobar2.adapters

import android.graphics.Color
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity

/**
 * Erstellt von Lena am 26/04/2019.
 */
class MemoListAdapter(private val memoList: List<MemoEntity>, private val listener: AdapterActionListener) :
    RecyclerView.Adapter<AdapterViewHolder>() {

    var grey = Color.GRAY

    override fun getItemCount(): Int = memoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_recyclerlist,
            parent, false)
        grey = v.context.resources.getColor(R.color.grey, null)
        return AdapterViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val currentMemo = memoList[position]
        holder.tvId.text = currentMemo.id.toString()
        holder.tvContent.text = currentMemo.content
        holder.toggleActive.isChecked = currentMemo.isActive
        //holder.toggleImportance.isChecked = currentMemo.importance == -1
        if(currentMemo.importance == -1) {
            /*holder.tvContent.alpha = 0.65f
            holder.toggleActive.alpha = 0.65f*/
        }
    }

}