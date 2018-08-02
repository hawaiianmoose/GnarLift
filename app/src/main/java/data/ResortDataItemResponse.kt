package data

import java.io.Serializable

class ResortDataItemResponse: Serializable {
    val id: String = String()
    val name: String = String()
    val href: String = String()
    val ll: Array<String> = arrayOf()
    val lifts: Lifts = Lifts()
    val twitter: Twitter = Twitter()
    val weather: Weather = Weather()
    val webcams: Array<Webcams> = arrayOf()
    val open: Boolean = false
    var liftStatus: MutableList<Lift> = mutableListOf()
}

class Lifts: Serializable {
    var status: HashMap<String, String> = hashMapOf()
    var stats: Stats = Stats()
}

class Lift(var name: String, var isOpen: Boolean) : Serializable

class Stats: Serializable {
    var open: String = String()
    var scheduled: String = String()
    var percentage: Percentage = Percentage()
    var hold: String = String()
    var closed: String = String()
}

class Percentage: Serializable {
    var open: String = String()
    var scheduled: String = String()
    var hold: String = String()
    var closed: String = String()
}

class Twitter: Serializable {
    var tweets: Array<Tweets> = arrayOf()
    var user: String = String()
}

class Tweets: Serializable {
    var extended_entities: Extended_entities = Extended_entities()
    var text: String = String()
    var created_at: String = String()
    var id_str: String = String()
    var entities: Entities = Entities()
}

class Extended_entities: Serializable {
    var media: Array<Media> = arrayOf()
}

class Entities: Serializable {
    var symbols: Array<String> = arrayOf()
    val urls: Array<Urls> = arrayOf()
    var hashtags: Array<Hashtags> = arrayOf()
    var media: Array<Media> = arrayOf()
    var user_mentions: Array<UserMentions> = arrayOf()
}

class Media: Serializable {
    var sizes: Sizes = Sizes()
    var id: String = String()
    var media_url_https: String = String()
    var media_url: String = String()
    var expanded_url: String = String()
    var indices: Array<String> = arrayOf()
    var id_str: String = String()
    var type: String = String()
    var display_url: String = String()
    var url: String = String()
}

class Hashtags: Serializable {
    var text: String = String()
    var indices: Array<String> = arrayOf()
}

class Sizes: Serializable {
    var small: ImageSize = ImageSize()
    var thumb: ImageSize = ImageSize()
    var medium: ImageSize = ImageSize()
    var large: ImageSize = ImageSize()
}

class ImageSize: Serializable {
    var w: String = String()
    var resize: String = String()
    var h: String = String()
}

class UserMentions: Serializable {
    var id: String = String()
    var name: String = String()
    var indices: Array<String> = arrayOf()
    var screen_name: String = String()
    var id_str: String = String()
}

class Weather: Serializable {
    var conditions: String = String()
    var icon: Array<String> = arrayOf()
    var text: String = String()
    var snow: String = String()
    var notice: Notice = Notice()
    var date: String = String()
    var temperature: Temperature = Temperature()
}

class Notice: Serializable {
    var site: String = String()
    var width: String = String()
    var img: String = String()
    var href: String = String()
}

class Temperature: Serializable {
    var max: String = String()
    var min: String = String()
}

class Webcams: Serializable {
    var source: String = String()
    var name: String = String()
    var image: String = String()
    var notice: String = String()
    var mobile: Mobile = Mobile()
}

class Mobile: Serializable {
    var source: String = String()
    var name: String = String()
    var image: String = String()
    var notice: String = String()
}

class Urls: Serializable {
    var expanded_url: String = String()
    var indices: Array<String> = arrayOf()
    var display_url: String = String()
    var url: String = String()
}