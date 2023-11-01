package org.wit.macrocount.models
import timber.log.Timber.Forest.i

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

    override fun update(day: DayModel) {
        var foundDay: DayModel? = days.find { d -> d.date == day.date && d.userId == day.userId }
        if (foundDay != null) {
            foundDay.userMacroIds = day.userMacroIds
            logAll()
        }
    }

    private fun logAll() {
        days.forEach{ i("$it")}
    }

}