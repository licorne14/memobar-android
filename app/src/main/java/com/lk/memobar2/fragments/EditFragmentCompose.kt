package com.lk.memobar2.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.main.MemoViewModel
import com.lk.memobar2.main.Utils
import com.lk.memobar2.ui.Themes
import com.lk.memobar2.ui.EditNoteUIScreen
import com.lk.memobar2.ui.IMemoEditActions
import java.lang.Exception

/**
 * Erstellt von Lena am 26/04/2019.
 */
class EditFragmentCompose: Fragment(), IMemoEditActions {

    private lateinit var viewModel: MemoViewModel
    private var memo: MemoEntity? = null
    private var title = 0

    private val TAG = "EditFragmentCompose"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MemoViewModel::class.java]
        Log.d(TAG, "onCreateView: ${arguments?.keySet()}")
        memo = arguments?.getSerializable(Utils.MEMO_KEY) as MemoEntity?
        title = arguments?.getInt(Utils.DIALOG_TITLE_RESOURCE) ?: R.string.dialog_edit_title_error
        if(memo == null) {
            throw Exception("No Memo available for editing!!")
        }
        val rootView = inflater.inflate(R.layout.dialog_edit_compose, container, false)
        setupComposeView(rootView)
        return rootView
    }

    private fun setupComposeView(rootView: View) {
        val composeView = rootView.findViewById<ComposeView>(R.id.cv_edit_ui_compose1)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { // In Compose world
                Themes.setStatusBarIconColours(this@EditFragmentCompose.requireActivity().window)
                val colorScheme = if (Utils.isBuildVersionGreaterThan(Build.VERSION_CODES.S)) {
                    Themes.getMaterialColorScheme(this@EditFragmentCompose.requireContext())
                } else {
                    MaterialTheme.colorScheme
                }
                MaterialTheme(colorScheme = colorScheme) {
                    EditNoteUIScreen(
                        memo!!.content,
                        appBarTitleResource = title,
                        this@EditFragmentCompose
                    )
                }
            }
        }
    }

    override fun onClickListener(newText: String) {
        viewModel.updateContentOfMemo(newText, memo!!)
        this.parentFragmentManager.commit {
            remove(this@EditFragmentCompose)
            Log.d(TAG, "onClickSave: remove fragment, ideally")
        }
        this.parentFragmentManager.popBackStack()
    }

    override fun onCancelListener(newText: String) {
        this.parentFragmentManager.commit {
            remove(this@EditFragmentCompose)
            Log.d(TAG, "onClickCancel: remove fragment, ideally")
        }
        this.parentFragmentManager.popBackStack()
    }

}