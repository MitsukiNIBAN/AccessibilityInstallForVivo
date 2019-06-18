package com.mitsuki.vivo.autoinstall

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.os.Bundle

class MyAccessibilityService : AccessibilityService() {

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        rootInActiveWindow?.let {
            if (event?.packageName == "com.bbk.account") {
                fillPassword()
                press(rootInActiveWindow.findAccessibilityNodeInfosByText("确定"))
            }

            if (event?.packageName == "com.android.packageinstaller") {
                install()
            }
        }
    }

    private fun fillPassword() {
        val editText = rootInActiveWindow.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        if (editText?.packageName == "com.bbk.account" && editText.className == "android.widget.EditText") {
            val arguments = Bundle()
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    BuildConfig.PASSWORD)
            editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        }
    }

    private fun install() {
        val nodeInfoList = ArrayList<AccessibilityNodeInfo>()
        nodeInfoList.addAll(rootInActiveWindow.findAccessibilityNodeInfosByText("继续安装"))
        nodeInfoList.addAll(rootInActiveWindow.findAccessibilityNodeInfosByText("安装"))
        nodeInfoList.addAll(rootInActiveWindow.findAccessibilityNodeInfosByText("打开"))
        press(nodeInfoList)
    }

    private fun press(list: List<AccessibilityNodeInfo>) {
        for (nodeInfo in list) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }
}