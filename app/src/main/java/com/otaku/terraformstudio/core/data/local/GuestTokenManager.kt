package com.otaku.terraformstudio.core.data.local

import android.content.Context
import java.util.UUID

class GuestTokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("guest_prefs", Context.MODE_PRIVATE)

    fun getGuestId(): String {
        val existing = prefs.getString(KEY_GUEST_ID, null)
        if (existing != null) return existing
        val newId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_GUEST_ID, newId).apply()
        return newId
    }

    fun clearGuestId() {
        prefs.edit().remove(KEY_GUEST_ID).apply()
    }

    companion object {
        private const val KEY_GUEST_ID = "guest_id"
    }
}