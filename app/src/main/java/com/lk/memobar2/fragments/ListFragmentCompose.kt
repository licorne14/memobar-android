package com.lk.memobar2.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.*
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.main.*
import com.lk.memobar2.ui.IMemoListActions
import com.lk.memobar2.ui.ListUIScreen
import com.lk.memobar2.ui.Themes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Erstellt von Lena am 26/04/2019.
 */
class ListFragmentCompose : Fragment(), Observer<List<MemoEntity>>, IMemoListActions {

    private val TAG = "ListFragmentCompose"
    private lateinit var composeView: ComposeView
    private var memos: List<MemoEntity> = listOf()

    private lateinit var viewModel: MemoViewModel
    private val _memosState = MutableStateFlow(memos)
    private val memosState: StateFlow<List<MemoEntity>> = _memosState.asStateFlow()

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        args: Bundle?
    ): View? {
        initialiseViewModel()
        val rootView = inflater.inflate(R.layout.fragment_list_compose, container, false)
        composeView = rootView.findViewById<ComposeView>(R.id.cv_list_ui_compose1)!!
        setupComposeView()
        return rootView
    }

    private fun initialiseViewModel() {
        viewModel = ViewModelProvider(requireActivity())[MemoViewModel::class.java]
        viewModel.observeMemos(this, this)
        memos = viewModel.getMemos()
        _memosState.update { oldValue ->
            Log.d(TAG, "initialiseViewModel: ${memos.size}")
            memos
        }
    }

    private fun setupComposeView() {
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                Themes.setStatusBarIconColours(this@ListFragmentCompose.requireActivity().window)
                val colorScheme = if (Utils.isBuildVersionGreaterThan(Build.VERSION_CODES.S)) {
                    Themes.getMaterialColorScheme(this@ListFragmentCompose.requireContext())
                } else {
                    MaterialTheme.colorScheme
                }
                MaterialTheme(colorScheme = colorScheme) {
                    ListUIScreen(
                        memoListActions = this@ListFragmentCompose,
                        memosState = memosState,
                        openSettings = this@ListFragmentCompose::openSettingsFragment
                    )
                }
            }
        }
    }

    // override for the viewmodel changes
    override fun onChanged(value: List<MemoEntity>) {
        Log.v(TAG, "Update of list with size: ${value.size}")
        memos = value
        _memosState.update { oldValue ->
            Log.d(TAG, "onChanged: ${memos.size}")
            value
        }
    }

    private fun openSettingsFragment() {
        requireActivity().supportFragmentManager.commit {
            add(R.id.fl_main, SettingsFragment::class.java, null, "SettingsFragment")
            addToBackStack("SettingsFragment")
        }
    }

    /* list actions implementation for Compose IMemoListActions */
    override fun onNewListener() {
        val memo = MemoEntity()
        memo.isActive = true    // change default value for show
        memo.lastUpdated = "00:00"
        callEditDialogForMemo(memo, R.string.dialog_new_title)
    }

    override fun onCheckBoxChecked(memoId: Int) {
        Log.d(TAG, "changeActiveState: $memoId")
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            selectedMemo.isActive = !selectedMemo.isActive
            viewModel.updateMemo(selectedMemo)
        }
    }

    override fun onEditMemo(memoId: Int) {
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            callEditDialogForMemo(selectedMemo, R.string.dialog_edit_title)
        }
    }

    override fun onDeleteMemo(memoId: Int) {
        val selectedMemo = memos.find { memo -> memo.id == memoId }
        if (selectedMemo != null) {
            selectedMemo.isActive = !selectedMemo.isActive
            viewModel.deleteMemo(selectedMemo)
        }
    }

    // unused for now
    fun changeImportance(memoId: Int) {
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

    private fun callEditDialogForMemo(memo: MemoEntity, titleResource: Int) {
        val args = bundleOf(
            Utils.MEMO_KEY to memo,
            Utils.DIALOG_TITLE_RESOURCE to titleResource
        )
        requireActivity().supportFragmentManager.commit {
            add(R.id.fl_main, EditFragmentCompose::class.java, args, "EditDialogCompose2")
            addToBackStack("EditDialog")
        }
    }

}