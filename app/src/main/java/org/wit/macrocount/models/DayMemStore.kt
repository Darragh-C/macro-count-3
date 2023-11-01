package org.wit.macrocount.models
import timber.log.Timber.Forest.i
import java.time.LocalDate
import java.util.Date

class DayMemStore: DayStore {

    val days = ArrayList<DayModel>()

    override fun findAll(): List<DayModel> {
        return days
    }

    override fun findByUserId(id: Long): List<DayModel> {
        return days.filter { d -> d.userId == id }
    }

    override fun create(day: DayModel) {
        days.add(day)
        logAll()
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
        var foundDay: DayModel? = days.find { d -> d.date == day.date && d.userId == day.userId }
        if (foundDay != null) {
            foundDay = day
        }
    }

    private fun logAll() {
        days.forEach{ i("$it")}
    }

}