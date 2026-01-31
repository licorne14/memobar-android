package com.lk.memobar2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.memobar2.R
import com.lk.memobar2.adapters.AdapterActionListener
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.adapters.MemoListAdapter
import com.lk.memobar2.dialogs.EditDialogFullscreen
import com.lk.memobar2.main.*

/**
 * Erstellt von Lena am 26/04/2019.
 */
class ListFragment : Fragment(), Observer<List<MemoEntity>>, AdapterActionListener {

    private val TAG = "ListFragment"

    private lateinit var fab: ImageButton
    private lateinit var rv: RecyclerView
    private var memos: List<MemoEntity> = listOf()
    private var longClickId: Int = -1

    private lateinit var viewModel: MemoViewModel

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        args: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recyclerlist, container, false)
        fab = rootView.findViewById<View>(R.id.fab_add_memo) as ImageButton
        rv = rootView.findViewById<View>(R.id.rv_list) as RecyclerView
        this.registerForContextMenu(rv)
        return rootView
    }

    override fun onActivityCreated(args: Bundle?) {
        super.onActivityCreated(args)
        initialiseViewModel()
        requireActivity().actionBar?.setTitle(R.string.app_name)
        fab.setOnClickListener {
            createNewMemo()
        }
        setupRecyclerAdapter()
    }

    private fun initialiseViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(MemoViewModel::class.java)
        viewModel.observeMemos(this, this)
        memos = viewModel.getMemos()
    }

    private fun setupRecyclerAdapter() {
        val adapter = MemoListAdapter(memos, this)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
    }

    private fun createNewMemo() {
        val memo = MemoEntity()
        memo.isActive = true    // change default value for show
        memo.lastUpdated = "00:00"
        callEditDialogForMemo(memo, R.string.dialog_new_title)
    }


    override fun onChanged(value: List<MemoEntity>) {
        Log.v(TAG, "Update of list with size: ${value.size}")
        memos = value
        setupRecyclerAdapter()
    }

    override fun changeActiveState(memoId: Int) {
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            selectedMemo.isActive = !selectedMemo.isActive
            viewModel.updateMemo(selectedMemo)
        }
    }

    override fun changeImportance(memoId: Int) {
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            if(selectedMemo.importance == 0) {
                selectedMemo.importance = -1
            } else {
                selectedMemo.importance = 0
            }
            viewModel.updateMemo(selectedMemo)
        }
    }

    override fun editMemo(memoId: Int) {
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            callEditDialogForMemo(selectedMemo, R.string.dialog_edit_title)
        }
    }

    override fun storeLongClickId(memoId: Int) {
        longClickId = memoId
    }

    private fun callEditDialogForMemo(memo: MemoEntity, titleResource: Int) {
        val args = bundleOf(
            Utils.MEMO_KEY to memo,
            Utils.DIALOG_TITLE_RESOURCE to titleResource
        )
        val editDialog = EditDialogFullscreen()
        editDialog.arguments = args
        // editDialog.show(requireFragmentManager(), "EditDialog")
        requireActivity().supportFragmentManager.transaction {
            add(R.id.fl_main, editDialog)
        }
    }

    // TODO add delete dialog or enable to revert removing
    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_item) {
            val selectedMemo = memos.find { memo -> memo.id == longClickId }
            if (selectedMemo != null) {
                selectedMemo.isActive = !selectedMemo.isActive
                viewModel.deleteMemo(selectedMemo)
            }
        }
        return super.onContextItemSelected(item)
    }

}