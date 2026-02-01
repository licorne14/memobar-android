package com.lk.memobar2.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.lk.memobar2.R
import com.lk.memobar2.database.MemoEntity
import com.lk.memobar2.main.*
import java.lang.Exception

/**
 * Erstellt von Lena am 26/04/2019.
 */
class EditDialogFullscreen: DialogFragment() {

    private lateinit var viewModel: MemoViewModel
    private var memo: MemoEntity? = null
    private var title = 0

    private val TAG = "EditDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MemoViewModel::class.java]
        memo = arguments?.getSerializable(Utils.MEMO_KEY) as MemoEntity?
        title = arguments?.getInt(Utils.DIALOG_TITLE_RESOURCE) ?: R.string.dialog_edit_title
        if(memo == null) {
            throw Exception("No Memo available for editing!!")
        }
        Log.d(TAG, "onCreateView: $showsDialog")
        return inflater.inflate(R.layout.dialog_edit_fullscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val editText = view.findViewById<EditText>(R.id.et_edit_fullscreen)
        editText.setText(memo!!.content, TextView.BufferType.EDITABLE)
        // titel setzen
        val tvTitle = view.findViewById<TextView>(R.id.tv_edit_fullscreen_title)
        tvTitle.setText(title)

        val btSave = view.findViewById<Button>(R.id.bt_edit_fullscreen_ok)
        btSave.setOnClickListener {
            val todo = editText.text.toString()
            viewModel.updateContentOfMemo(todo, memo!!)
            imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
            this.dismiss()
        }
        val btCancel = view.findViewById<Button>(R.id.bt_edit_fullscreen_cancel)
        btCancel.setOnClickListener {
            imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
            this.dismiss()
        }

        // TODO copy / paste bzw generell selektion ist buggy in Dialogen, vorläufig erfolgt die Anzeige Fullscreen
        // show input method
        if(editText.requestFocus()) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}