package com.walhalla.horolib.helper

/**
 * Created by combo on 10/22/2017.
 */
interface ParentFragmentCallback {
    fun onFinish(tag: String, state: Boolean)

    fun refreshData()

    fun shareData(buffer: String)
}
