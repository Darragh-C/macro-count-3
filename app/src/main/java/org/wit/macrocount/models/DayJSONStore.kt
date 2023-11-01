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
import java.time.LocalDate


private const val JSON_FILE = "days.json"
private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
private val listType: Type = object : TypeToken<ArrayList<DayModel>>() {}.type

class DayJSONStore(private val context: Context) : DayStore {

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

    override fun findByUserDate(id: Long, date: LocalDate): List<DayModel> {
        return days.filter { d -> d.userId == id && d.date == date }
    }

    override fun create(day: DayModel) {
        days.add(day)
        serialize()
    }

    override fun addMacroId(macroId: Long, userId: Long, date: LocalDate) {
        var dayModel = DayModel()
        dayModel.userId = userId
        dayModel.date = date

        var foundDay: DayModel? = days.find { d -> d.date == date && d.userId == userId }

        if (foundDay != null) {
            var macroIds = foundDay.userMacroIds.toMutableList()
            macroIds.add(macroId.toString())
            dayModel.userMacroIds = macroIds

            update(dayModel)

        } else {
            dayModel.userMacroIds = listOf(macroId.toString())
            create(dayModel)
        }
    }

    private fun update(day: DayModel) {
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


