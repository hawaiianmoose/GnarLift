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
    val latitude: Double? = null
    val longitude: Double? = null
}