package data

import java.io.Serializable

class ResortDataItemResponse: Serializable{
    val id: String? = null
    val name: String? = null
    val href: String? = null
    val ll: Array<String>? = null
    val lifts: Lifts? = null
    val twitter: Twitter? = null
    val weather: Weather? = null
    val webcams: Array<Webcams>? = null
    val open: Boolean? = null
    var liftStatus: MutableList<Lift>? = null

}

class Lifts {
    var status: HashMap<String, String>? = null
    var stats: Stats? = null
}

class Lift(var name: String, var isOpen: Boolean) {}

class Stats {
    var open: String? = null
    var scheduled: String? = null
    var percentage: Percentage? = null
    var hold: String? = null
    var closed: String? = null
}

class Percentage {
    var open: String? = null
    var scheduled: String? = null
    var hold: String? = null
    var closed: String? = null
}

class Twitter {
    var tweets: Array<Tweets>? = null
    var user: String? = null
}

class Tweets {
    var extended_entities: Extended_entities? = null
    var text: String? = null
    var created_at: String? = null
    var id_str: String? = null
    var entities: Entities? = null
}

class Extended_entities {
    var media: Array<Media>? = null
}

class Entities {
    var symbols: Array<String>? = null
    val urls: Array<Urls>? = null
    var hashtags: Array<Hashtags>? = null
    var media: Array<Media>? = null
    var user_mentions: Array<UserMentions>? = null
}

class Media {
    var sizes: Sizes? = null
    var id: String? = null
    var media_url_https: String? = null
    var media_url: String? = null
    var expanded_url: String? = null
    var indices: Array<String>? = null
    var id_str: String? = null
    var type: String? = null
    var display_url: String? = null
    var url: String? = null
}

class Hashtags {
    var text: String? = null
    var indices: Array<String>? = null
}

class Sizes {
    var small: ImageSize? = null
    var thumb: ImageSize? = null
    var medium: ImageSize? = null
    var large: ImageSize? = null
}

class ImageSize {
    var w: String? = null
    var resize: String? = null
    var h: String? = null
}

class UserMentions {
    var id: String? = null
    var name: String? = null
    var indices: Array<String>? = null
    var screen_name: String? = null
    var id_str: String? = null
}

class Weather {
    var conditions: String? = null
    var icon: Array<String>? = null
    var text: String? = null
    var snow: String? = null
    var notice: Notice? = null
    var date: String? = null
    var temperature: Temperature? = null
}

class Notice {
    var site: String? = null
    var width: String? = null
    var img: String? = null
    var href: String? = null
}

class Temperature {
    var max: String? = null
}

class Webcams {
    var source: String? = null
    var name: String? = null
    var image: String? = null
    var notice: String? = null
    var mobile: Mobile? = null
}

class Mobile {
    var source: String? = null
    var name: String? = null
    var image: String? = null
    var notice: String? = null
}

class Urls {
    var expanded_url: String? = null
    var indices: Array<String>? = null
    var display_url: String? = null
    var url: String? = null
}