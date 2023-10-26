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
import org.wit.macrocount.helpers.exists
import org.wit.macrocount.helpers.read
import org.wit.macrocount.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Random

const val USER_JSON_FILE = "users.json"

var currentUser = UserModel()

val userGsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()

val userListType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun genRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context): UserStore {

    var users = mutableListOf<UserModel>()

    init {
        if (exists(context, USER_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

//    override fun getUser(user: UserModel): UserModel {
//        var foundUser: UserModel? = users.find { m -> m.id == user.id }
//    }

    override fun create(user: UserModel) {
        users.add(user)
        serialize()
    }

    override fun logIn(user: UserModel): Boolean {
        var foundUser: UserModel? = users.find { u -> u.email == user.email}
        if (foundUser != null && foundUser.email == user.email) {
            currentUser = foundUser
            return true
        } else {
            return false
        }
    }

    override fun signUp(user: UserModel) {
        this.create(user)
        logIn(user)
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { m -> m.id == user.id }
        if (foundUser != null) {
            foundUser.name = user.name
            foundUser.gender = user.gender
            foundUser.weight = user.weight
            foundUser.dob = user.dob
            foundUser.email = user.email
            foundUser.password = user.password

            serialize()

            //logAll()
        }
    }

    override fun delete(user: UserModel) {
        var foundUser: UserModel? = users.find { u -> u.id == user.id }
        if (foundUser != null) {
            users.remove(user)
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = userGsonBuilder.toJson(users, userListType)
        write(context, USER_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, USER_JSON_FILE)
        users = userGsonBuilder.fromJson(jsonString, userListType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}

class UserUriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}