package com.aristo.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.aristo.admin.databinding.BottomSheetMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


fun processColorCode(apiColorCode: String): String {
    // Check if apiColorCode starts with "#" and add "#" if it doesn't
    val processedColorCode = if (!apiColorCode.startsWith("#")) {
        "#$apiColorCode"
    } else {
        apiColorCode
    }

    return processedColorCode
}
