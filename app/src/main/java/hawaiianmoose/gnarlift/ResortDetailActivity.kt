package hawaiianmoose.gnarlift

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.phrase.Phrase
import com.squareup.picasso.Picasso
import data.Constants
import data.ResortDataItemResponse
import data.StaticResortDataItem
import data.Temperature
import data.Weather
import kotlinx.android.synthetic.main.activity_resort_detail.*
import utils.WeatherToIconConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import android.text.style.StyleSpan



class ResortDetailActivity : AppCompatActivity() {

    private var staticData: StaticResortDataItem = StaticResortDataItem()
    private var liftieData: ResortDataItemResponse = ResortDataItemResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        setContentView(R.layout.activity_resort_detail)
        staticData = intent.extras[Constants.favoritesData] as StaticResortDataItem
        liftieData = intent.extras[Constants.resortDetailData] as ResortDataItemResponse
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener { this.onBackPressed() }
        bindStaticData()
        bindLiftieData(liftieData)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    private fun bindStaticData(){
        val textView = findViewById<TextView>(R.id.resort_name_title)
        textView.text = staticData.name
    }

    private fun bindLiftieData(liftieResortDataResponse: ResortDataItemResponse) {
        val viewManager = LinearLayoutManager(this.baseContext)
        val viewAdapter = ResortDetailRecyclerViewAdapter(liftieResortDataResponse)
        lift_status.setHasFixedSize(true)
        lift_status.layoutManager = viewManager
        lift_status.adapter = viewAdapter
        lift_status.isNestedScrollingEnabled = false

        setCurrentWeatherIcon(liftieResortDataResponse.weather, current_weather_icon_image_view)
        setTemperatureText(liftieResortDataResponse.weather.temperature)
        setupTwitter(liftieResortDataResponse)
        setupMap(liftieResortDataResponse)
        setupLifts(liftieResortDataResponse)
        setupPhone()

        snow_base_text.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_snow_base_inches))
                .put("snow",liftieResortDataResponse.weather.snow).format().toString()
    }

    private fun setupPhone() {
        resort_phone.text = staticData.phone
        resort_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_phone_dialer))
                    .put("phone_number", staticData.phone).format().toString())
            startActivity(intent)
        }
    }

    private fun setupLifts(liftieResortDataResponse: ResortDataItemResponse) {
        lift_Status_Bar.progress = liftieResortDataResponse.lifts.stats.percentage.open.toBigDecimal().toInt()

        val stringBuilder = SpannableStringBuilder()
        val percentage = SpannableString(Phrase.from(this.resources.getString(R.string.TEMPLATE_lift_percent))
                .put("lift_percent", lift_Status_Bar.progress.toString()).format().toString())
        stringBuilder.append(percentage)
        stringBuilder.append(resources.getString(R.string.of_lifts_open))
        val boldStyle = StyleSpan(android.graphics.Typeface.BOLD)
        stringBuilder.setSpan(boldStyle, 0, percentage.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        lift_percent_text.text = stringBuilder
    }

    private fun setupMap(liftieResortDataResponse: ResortDataItemResponse) {
        map_image.setOnClickListener {
            val gmapsIntentUri = Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_map_nav))
                    .put("lat", liftieResortDataResponse.ll.get(1))
                    .put("lon", liftieResortDataResponse.ll.get(0))
                    .format().toString())
            val mapIntent = Intent(Intent.ACTION_VIEW, gmapsIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        Picasso.get().load(Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_resort_map_image_uri))
                .put("lat", liftieResortDataResponse.ll.get(1))
                .put("lon", liftieResortDataResponse.ll.get(0))
                .format().toString())).fit().into(map_image)
    }

    @SuppressLint("NewApi")
    private fun setupTwitter(liftieResortDataResponse: ResortDataItemResponse) {
        val latestTweet = liftieResortDataResponse.twitter.tweets.first()
        val stringBuilder = SpannableStringBuilder()
        val twitterHandle = SpannableString(Phrase.from(resources.getString(R.string.TEMPLATE_twitter_handle))
                .put("twitter_handle", liftieResortDataResponse.twitter.user).format().toString())
        twitterHandle.setSpan(ForegroundColorSpan(resources.getColor(R.color.tweet_color)), 0, twitterHandle.length, 0)
        stringBuilder.append(twitterHandle)
        stringBuilder.append(Html.fromHtml(latestTweet.text))
        tweet_text_view.setText(stringBuilder, TextView.BufferType.SPANNABLE)

        val formatter = DateTimeFormatter.ofPattern("eee MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(latestTweet.created_at, formatter)
        tweet_date_text_view.text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))

        if (!latestTweet.entities.media.any() || latestTweet.entities.media.first().media_url.isEmpty()) {
            tweet_image_view.visibility = View.GONE
        } else {
            Picasso.get().load(latestTweet.entities.media.first().media_url).fit().centerCrop().into(tweet_image_view)
        }

        tweet_card_view.setOnClickListener {
            val twitterDeeplink = Uri.parse(Phrase.from(resources.getString(R.string.TEMPLATE_twitter_deeplink))
                    .put("user", liftieResortDataResponse.twitter.user)
                    .format().toString())
            val intent = Intent(Intent.ACTION_VIEW, twitterDeeplink)
            startActivity(intent)
        }
    }

    private fun setTemperatureText(temp: Temperature) {
        temperature_text.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_high_temperature))
                .put("temperature",temp.max).format().toString()

        if (!temp.min.isEmpty()) {
            low_temperature_text.visibility = View.VISIBLE
            low_temperature_text.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_low_temperature))
                    .put("temperature",temp.min).format().toString()
        }
    }

    private fun setCurrentWeatherIcon(weather: Weather, weatherIconImageView: ImageView) {
        if (!weather.text.isEmpty()) {
            weatherIconImageView.setImageResource(WeatherToIconConverter.convertWeatherTextToIcon(weather.text))
        } else if (!weather.conditions.isEmpty()) {
            weatherIconImageView.setImageResource(WeatherToIconConverter.convertWeatherTextToIcon(weather.conditions))
        }
    }
}
