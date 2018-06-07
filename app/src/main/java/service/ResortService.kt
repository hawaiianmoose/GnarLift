package service

import android.content.Context
import com.google.gson.Gson
import data.StaticResortDataItemResponse
import utils.JsonUtil

class ResortService private constructor(passedContext: Context) {
    companion object : SingletonHolder<ResortService, Context>(::ResortService)
    private var context: Context = passedContext

    fun fetchStaticResortData(): StaticResortDataItemResponse {
        val staticResortDataString = JsonUtil.parseJsonAsString(context, "StaticResortsData")
        val gson = Gson()

        return gson.fromJson(staticResortDataString, StaticResortDataItemResponse::class.java)
    }
}

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}