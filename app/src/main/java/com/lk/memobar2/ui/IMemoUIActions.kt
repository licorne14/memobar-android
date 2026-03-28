package com.lk.memobar2.ui

interface IMemoListActions {

    fun onNewListener()
    fun onCheckBoxChecked(memoId: Int)
    fun onEditMemo(memoId: Int)
    fun onDeleteMemo (memoId: Int)

}

interface IMemoEditActions {
    fun onClickListener(newText: String)
    fun onCancelListener(newText: String)
}