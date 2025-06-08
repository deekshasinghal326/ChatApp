package com.deelib.chatapp.utils

import android.content.Context
import com.deelib.chatapp.model.ChatData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Utils {
    fun loadMessage(context: Context): List<ChatData> {
        val json = context.assets.open("chat_data.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(json, object : TypeToken<List<ChatData>>() {}.type)
    }
}