package data

import java.io.Serializable

class StaticResortDataItemResponse: Serializable {
    val resorts: MutableList<StaticResortDataItem> = mutableListOf()
}

class StaticResortDataItem : Serializable {
    val resortId: String? = null
    val name: String? = null
    val phone: String? = null
    val imageUrl: String? = null
    val latitude: Double = 0.0
    val longitude: Double = 0.0
}