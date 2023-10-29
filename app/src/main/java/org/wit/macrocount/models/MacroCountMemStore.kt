package org.wit.macrocount.models
import timber.log.Timber.Forest.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class MacroCountMemStore: MacroCountStore {

    val macroCounts = ArrayList<MacroCountModel>()

    override fun findAll(): List<MacroCountModel> {
        return macroCounts
    }
    override fun findByUserId(id: Long): List<MacroCountModel> {
        return macroCounts.filter { m -> m.userId == id }
    }

    override fun findByTitle(title: String): MacroCountModel {
        val foundMacros = macroCounts.filter { m -> m.title == title }
        if (foundMacros.isEmpty()) {
            throw NoSuchElementException("No item with title $title found")
        } else if (foundMacros.size > 1) {
            throw IllegalStateException("Multiple items with title $title found")
        }
        return foundMacros[0]
    }


    override fun create(macroCount: MacroCountModel) {
        macroCount.id = getId()
        macroCounts.add(macroCount)
        logAll()
    }

    override fun update(macroCount: MacroCountModel) {
        var foundMacroCount: MacroCountModel? = macroCounts.find { m -> m.id == macroCount.id }
        if (foundMacroCount != null) {
            foundMacroCount.title = macroCount.title
            foundMacroCount.description = macroCount.description
            foundMacroCount.calories = macroCount.calories
            foundMacroCount.carbs = macroCount.carbs
            foundMacroCount.protein = macroCount.protein
            foundMacroCount.fat = macroCount.fat

            logAll()
        }
    }

    override fun delete(macroCount: MacroCountModel) {
        var foundMacroCount: MacroCountModel? = macroCounts.find { m -> m.id == macroCount.id }
        if (foundMacroCount != null) {
            macroCounts.remove(macroCount)
        }
    }

    override fun index(macroCount: MacroCountModel): Int {
        var foundMacroCount: MacroCountModel? = macroCounts.find { m -> m.id == macroCount.id }
        if (foundMacroCount != null) {
            return macroCounts.indexOf(macroCount)
        } else {
            return -1
        }
    }

    private fun logAll() {
        macroCounts.forEach{ i("$it")}
    }
}