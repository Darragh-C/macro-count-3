package org.wit.macrocount.models
import timber.log.Timber.Forest.i
class UserMemStore: UserStore {

    val users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        users.add(user)
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { u -> u.id == user.id }
        if (foundUser != null) {
            foundUser.name = user.name
            foundUser.gender = foundUser.gender
            foundUser.weight = foundUser.weight
            foundUser.dob = foundUser.dob

            logAll()
        }
    }

    fun logAll() {
        users.forEach{ i("${it}") }
    }

}