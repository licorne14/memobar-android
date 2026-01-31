package com.lk.memobar2.adapters

import android.view.ContextMenu
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.lk.memobar2.R

/**
 * Erstellt von Lena am 26/04/2019.
 */
class AdapterViewHolder(v: View, listener: AdapterActionListener):
    RecyclerView.ViewHolder(v),
    View.OnCreateContextMenuListener {

    val tvId: TextView = v.findViewById(R.id.tv_view_id)
    val tvContent: TextView = v.findViewById(R.id.tv_view_content)
    val toggleActive: CompoundButton = v.findViewById(R.id.toggle_view_active)
    /*val toggleImportance: CompoundButton = v.findViewById(R.id.cb_change_importance)*/

    init {
        toggleActive.setOnClickListener {
            listener.changeActiveState(getCurrentId())
        }
        /*toggleImportance.setOnClickListener {
            listener.changeImportance(getCurrentId())
        }*/
        tvContent.setOnClickListener {
            listener.editMemo(getCurrentId())
        }
        tvContent.setOnLongClickListener {
            listener.storeLongClickId(getCurrentId())
            false
        }
        v.setOnCreateContextMenuListener(this)
    }

    private fun getCurrentId(): Int = tvId.text.toString().toInt()

    override fun onCreateContextMenu(contextMenu: ContextMenu,
                                     view: View,
                                     contextMenuInfo: ContextMenu.ContextMenuInfo?) {
        contextMenu.add(0, R.id.menu_delete_item, 0, R.string.menu_delete)
    }


}