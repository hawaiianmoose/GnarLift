package utils

import android.content.Context
import android.util.Log
import java.io.InputStream

object JsonUtil {

    fun parseJsonAsString(context: Context, fileName: String): String {
        try {
            val inputStream:InputStream = context.assets.open(fileName)
            val inputString = inputStream.bufferedReader().use{it.readText()}
            Log.d("Successful Json Parse",inputString)
            return inputString
        } catch (e:Exception){
            Log.d("Failed Json Parse", e.toString())
        }
        return String()
    }
}