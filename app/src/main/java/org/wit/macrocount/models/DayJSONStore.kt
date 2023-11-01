package org.wit.macrocount.models

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import org.wit.macrocount.helpers.exists
import org.wit.macrocount.helpers.read
import org.wit.macrocount.helpers.write
import java.lang.reflect.Type


private const val JSON_FILE = "days.json"
private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
private val listType: Type = object : TypeToken<ArrayList<DayModel>>() {}.type

class DayJSONMemStore(private val context: Context) : DayStore {

    var days = mutableListOf<DayModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<DayModel> {
        logAll()
        return days
    }

    override fun findByUserId(id: Long): List<DayModel> {
        return days.filter { day -> day.userId == id }
    }

    override fun create(day: DayModel) {
        days.add(day)
        serialize()
    }

    override fun update(day: DayModel) {
        val foundDay = days.find { it.date == day.date && it.userId == day.userId }
        foundDay?.let {
            it.userMacroIds = day.userMacroIds
            serialize()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(days, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        days.addAll(gsonBuilder.fromJson(jsonString, listType))
    }

    private fun logAll() {
        days.forEach { Timber.i("$it") }
    }
}


