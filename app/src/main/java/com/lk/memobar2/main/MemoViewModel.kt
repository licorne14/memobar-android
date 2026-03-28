package com.lk.memobar2.main

import android.app.Application
import androidx.lifecycle.*
import com.lk.memobar2.database.*

/**
 * Erstellt von Lena am 26/04/2019.
 */
class MemoViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "MemoViewModel"
    private val repository: DataRepository = LocalDBRepository(application)
    private val liveMemos = repository.getMemos()

    fun getMemos(): List<MemoEntity>{
        return liveMemos.value ?: listOf()
    }

    fun observeMemos(owner: LifecycleOwner, observer: Observer<List<MemoEntity>>){
        liveMemos.observe(owner, observer)
    }
    // TODO Aufruf noch notwendig??
    fun removeMemosObserver(observer: Observer<List<MemoEntity>>) {
        liveMemos.removeObserver(observer)
    }

    fun updateContentOfMemo(content: String, memo: MemoEntity) {
        if(content.isNotEmpty()) {
            memo.content = content
        }
        memo.setCurrentTimeStamp()
        val selectedMemo = getMemos().find { memoItem -> memoItem.id == memo.id }
        if(selectedMemo == null && content.isNotEmpty()) {
            // Memo not found and content isn't empty, insert
            insertMemo(memo)
        } else {
            // Memo found, update
            updateMemo(memo)
        }
    }

    private fun insertMemo(memo: MemoEntity){
        repository.insertMemo(memo)
    }

    fun updateMemo(memo: MemoEntity) {
        repository.updateMemo(memo)
    }

    fun deleteMemo(memo: MemoEntity){
        repository.deleteMemo(memo)
    }

}