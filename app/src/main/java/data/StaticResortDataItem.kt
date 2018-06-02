package data

import java.io.Serializable

class StaticResortDataItemResponse: Serializable {

    val resorts: List<StaticResortDataItem> = listOf()
}

class StaticResortDataItem {
    val name: String? = null
    val phone: String? = null
    val imageUrl: String? = null
}