package org.wit.macrocount.models

interface MacroCountStore {
    fun findAll(): List<MacroCountModel>
    fun create(macroCount: MacroCountModel)
    fun update(macroCount: MacroCountModel)
    fun delete(macroCount: MacroCountModel)
    fun index(macroCount: MacroCountModel): Int
    fun findByCurrentUser(): List<MacroCountModel>
}