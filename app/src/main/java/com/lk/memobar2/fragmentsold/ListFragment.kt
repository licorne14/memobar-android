package com.lk.memobar2.fragmentsold

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.memobar2.R
import com.lk.memobar2.adapters.AdapterActionListener
import com.lk.memobar2.adapters.MemoListAdapter
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.fragments.EditFragmentCompose
import com.lk.memobar2.fragments.ListFragmentCompose
import com.lk.memobar2.main.MemoViewModel
import com.lk.memobar2.main.Utils
import com.lk.memobar2.ui.Themes
import com.lk.memobar2.devtests.RenderMainUI

/**
 * Erstellt von Lena am 26/04/2019.
 */
class ListFragment : Fragment(), Observer<List<MemoEntity>>, AdapterActionListener {

    private val TAG = "ListFragment"

    private lateinit var fab: ImageButton
    private lateinit var rv: RecyclerView
    private lateinit var composeView: ComposeView
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
        ViewCompat.setOnApplyWindowInsetsListener(fab) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            /* Apply the insets as a margin to the view. This solution sets
             only the bottom, left, and right dimensions, but you can apply whichever
             insets are appropriate to your layout. You can also update the view padding
             if that's more appropriate.*/
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            // Return CONSUMED if you don't want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
        rv = rootView.findViewById<View>(R.id.rv_list) as RecyclerView
        composeView = rootView.findViewById<ComposeView>(R.id.v_compose_main_test) as ComposeView
        setupComposeView()
        this.registerForContextMenu(rv)
        return rootView
    }

    private fun setupComposeView() {
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                if (Utils.isBuildVersionGreaterThan(Build.VERSION_CODES.S)) {
                    MaterialTheme(colorScheme = Themes.getMaterialColorScheme(this@ListFragment.requireContext())) {
                        RenderMainUI()
                    }
                } else {
                    MaterialTheme {
                        RenderMainUI()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, args: Bundle?) {
        super.onViewCreated(view, args)
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
        requireActivity().supportFragmentManager.commit {
            add(R.id.fl_main, editDialog, "EditDialogRegular")
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
        } else if (item.itemId == R.id.menu_edit_compose) {
            val selectedMemo = memos.find { memo -> memo.id == longClickId }
            if (selectedMemo != null) {
                Log.d(TAG, "onContextItemSelected: calling on the edit dialog")
                val args = bundleOf(
                    Utils.MEMO_KEY to selectedMemo,
                    Utils.DIALOG_TITLE_RESOURCE to R.string.dialog_edit_title
                )
                /*val editDialogCompose = EditDialogFullscreenCompose()
                editDialogCompose.arguments = args*/
                requireActivity().supportFragmentManager.commit {
                    add(
                        R.id.fl_main, EditFragmentCompose::class.java, args
                    , "EditDialogCompose")
                    addToBackStack("RegularList")
                }
            }
        } else if (item.itemId == R.id.menu_new_compose_list) {
            val listFragment = ListFragmentCompose()
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fl_main, listFragment, "ListFragmentCompose")
                addToBackStack("RegularList")
            }
        }
        return super.onContextItemSelected(item)
    }

}