package org.wit.macrocount.models

interface DayStore {

    fun findAll(): List<DayModel>
    fun findByUserId(id: Long): List<DayModel>
    fun create(day: DayModel)
    fun update(day: DayModel)

}