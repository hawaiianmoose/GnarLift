package hawaiianmoose.gnarlift

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
import utils.WeatherToIconConverter
import java.util.*
import android.text.style.StyleSpan
import android.view.animation.AnimationUtils
import android.widget.Toast
import hawaiianmoose.gnarlift.databinding.ActivityResortDetailBinding
import hawaiianmoose.gnarlift.databinding.FragmentInfoBinding
import service.FavoriteService
import java.text.SimpleDateFormat

class ResortDetailActivity : AppCompatActivity() {
    private var _binding: ActivityResortDetailBinding? = null
    private val binding get() = _binding!!
    private var staticData: StaticResortDataItem = StaticResortDataItem()
    private var liftieData: ResortDataItemResponse = ResortDataItemResponse()
    private lateinit var favoritesData: MutableSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResortDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        setContentView(R.layout.activity_resort_detail)
        staticData = intent.extras?.get(Constants.favoritesData) as StaticResortDataItem
        liftieData = intent.extras?.get(Constants.resortDetailData) as ResortDataItemResponse
        favoritesData = FavoriteService.getInstance(this).getSavedFavorites() as MutableSet<String>
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener { this.onBackPressed() }
        bindStaticData()
        bindLiftieData(liftieData)

        if (favoritesData.contains(staticData.resortId)) {
            binding.favoriteTitleButton.setImageResource(R.drawable.ic_sharp_star_24px)
        } else {
            binding.favoriteTitleButton.setImageResource(R.drawable.ic_sharp_star_border_24px)
        }

        binding.favoriteTitleButton.setOnClickListener {
            if (favoritesData.contains(staticData.resortId)) {
                removeFavoriteResort(staticData.resortId!!, staticData.name!!)
            } else {
                val image = binding.favoriteTitleButton as ImageView
                val iconBounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
                image.startAnimation(iconBounce)
                addFavoriteResort(staticData.resortId!!, staticData.name!!)
            }
        }
    }

    private fun addFavoriteResort(resortId: String, resortName: String) {
        binding.favoriteTitleButton.setImageResource(R.drawable.ic_sharp_star_24px)
        FavoriteService.getInstance(this).saveFavorite(resortId)
        favoritesData.add(resortId)
        Toast.makeText(this, makeToast(resortName, R.string.toast_added), Toast.LENGTH_SHORT).show()
    }

    private fun removeFavoriteResort(resortId: String, resortName: String) {
        binding.favoriteTitleButton.setImageResource(R.drawable.ic_sharp_star_border_24px)
        FavoriteService.getInstance(this).removeFavorite(resortId)
        favoritesData.remove(resortId)
        Toast.makeText(this, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_SHORT).show()
    }

    private fun makeToast(resortName: String, stringId: Int): String {
        return String.format(this.getString(stringId), resortName)
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
        binding.liftStatus.setHasFixedSize(true)
        binding.liftStatus.layoutManager = viewManager
        binding.liftStatus.adapter = viewAdapter
        binding.liftStatus.isNestedScrollingEnabled = false

        setCurrentWeatherIcon(liftieResortDataResponse.weather, binding.currentWeatherIconImageView)
        setTemperatureText(liftieResortDataResponse.weather.temperature)
        setupTwitter(liftieResortDataResponse)
        //setupMap(liftieResortDataResponse)
        setupLifts(liftieResortDataResponse)
        setupPhone()

        binding.snowBaseText.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_snow_base_inches))
                .put("snow",liftieResortDataResponse.weather.snow).format().toString()
    }

    private fun setupPhone() {
        binding.resortPhone.text = staticData.phone
        binding.resortPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_phone_dialer))
                    .put("phone_number", staticData.phone).format().toString())
            startActivity(intent)
        }
    }

    private fun setupLifts(liftieResortDataResponse: ResortDataItemResponse) {
        binding.liftStatusBar.progress = liftieResortDataResponse.lifts.stats.percentage.open.toBigDecimal().toInt()

        val stringBuilder = SpannableStringBuilder()
        val percentage = SpannableString(Phrase.from(this.resources.getString(R.string.TEMPLATE_lift_percent))
                .put("lift_percent", binding.liftStatusBar.progress.toString()).format().toString())
        stringBuilder.append(percentage)
        stringBuilder.append(resources.getString(R.string.of_lifts_open))
        val boldStyle = StyleSpan(android.graphics.Typeface.BOLD)
        stringBuilder.setSpan(boldStyle, 0, percentage.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.liftPercentText.text = stringBuilder
    }

    private fun setupMap(liftieResortDataResponse: ResortDataItemResponse) {
//        map_image.setOnClickListener {
//            val gmapsIntentUri = Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_map_nav))
//                    .put("lat", liftieResortDataResponse.ll.get(1))
//                    .put("lon", liftieResortDataResponse.ll.get(0))
//                    .format().toString())
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmapsIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)
//        }
//
//        Picasso.get().load(Uri.parse(Phrase.from(this.resources.getString(R.string.TEMPLATE_resort_map_image_uri))
//                .put("lat", liftieResortDataResponse.ll.get(1))
//                .put("lon", liftieResortDataResponse.ll.get(0))
//                .format().toString())).fit().into(map_image)
    }

    @SuppressLint("NewApi")
    private fun setupTwitter(liftieResortDataResponse: ResortDataItemResponse) {
        if (liftieResortDataResponse.twitter.tweets.any()) {
            val latestTweet = liftieResortDataResponse.twitter.tweets.first()
            val stringBuilder = SpannableStringBuilder()
            val twitterHandle = SpannableString(Phrase.from(resources.getString(R.string.TEMPLATE_twitter_handle))
                    .put("twitter_handle", liftieResortDataResponse.twitter.user).format().toString())
            twitterHandle.setSpan(ForegroundColorSpan(resources.getColor(R.color.tweet_color)), 0, twitterHandle.length, 0)
            stringBuilder.append(twitterHandle)
            stringBuilder.append(Html.fromHtml(latestTweet.text))
            binding.tweetTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE)

            val parser = SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val date = parser.parse(latestTweet.created_at)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            binding.tweetDateTextView.text = formatter.format(date)

            if (!latestTweet.entities.media.any() || latestTweet.entities.media.first().media_url.isEmpty()) {
                binding.tweetImageView.visibility = View.GONE
            } else {
                Picasso.get().load(latestTweet.entities.media.first().media_url_https).fit().centerCrop().into(binding.tweetImageView)
            }

            binding.tweetCardView.setOnClickListener {
                val twitterDeeplink = Uri.parse(Phrase.from(resources.getString(R.string.TEMPLATE_twitter_deeplink))
                        .put("user", liftieResortDataResponse.twitter.user)
                        .format().toString())
                val intent = Intent(Intent.ACTION_VIEW, twitterDeeplink)
                startActivity(intent)
            }
        } else {
            Picasso.get().load(staticData.imageUrl).fit().centerCrop().into(binding.defaultImageView)
            binding.tweetCardView.visibility = View.INVISIBLE
            binding.defaultImageView.visibility = View.VISIBLE
        }
    }

    private fun setTemperatureText(temp: Temperature) {

        if (!temp.max.isNullOrEmpty()) {
            binding.temperatureText.visibility = View.VISIBLE
            binding.temperatureText.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_high_temperature))
                    .put("temperature",temp.max ?: "-").format().toString()
        } else {
            binding.temperatureText.visibility = View.GONE
        }

        if (!temp.min.isNullOrEmpty()) {
            binding.lowTemperatureText.visibility = View.VISIBLE
            binding.lowTemperatureText.text = Phrase.from(this.resources.getString(R.string.TEMPLATE_low_temperature))
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
