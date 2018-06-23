package hawaiianmoose.gnarlift

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.phrase.Phrase
import com.squareup.picasso.Picasso
import data.Constants
import data.ResortDataItemResponse
import data.StaticResortDataItem
import io.reactivex.Observer
import service.LiftieService
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.SpannableStringBuilder



class ResortDetailActivity : AppCompatActivity() {

    private var staticData: StaticResortDataItem = StaticResortDataItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resort_detail)
        staticData = intent.extras[Constants.favoritesData] as StaticResortDataItem
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener { this.onBackPressed() }

        bindStaticData()
        getLiftieDataForResort()
    }

    private fun bindStaticData(){
        val textView = findViewById<TextView>(R.id.resort_name_title)
        textView.text = staticData.name
    }

    private fun getLiftieDataForResort() {
        val liftieSubject = PublishSubject.create<ResortDataItemResponse>()
        liftieSubject.subscribe(object : Observer<ResortDataItemResponse> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(liftieResortDataResponse: ResortDataItemResponse) {
                bindLiftieData(liftieResortDataResponse)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })

        staticData.resortId?.let { LiftieService().fetchResortInfo(it, liftieSubject) }
    }

    private fun bindLiftieData(liftieResortDataResponse: ResortDataItemResponse) {
        val liftOpenBar = findViewById<ProgressBar>(R.id.liftStatusBar)
        val heroImage = findViewById<ImageView>(R.id.hero_image)
        val openClosedText = findViewById<TextView>(R.id.resort_open_closed_text)
        val recycler = findViewById<RecyclerView>(R.id.lift_status)
        val viewManager = LinearLayoutManager(this.baseContext)
        val viewAdapter = ResortDetailRecyclerViewAdapter(liftieResortDataResponse)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = viewManager
        recycler.adapter = viewAdapter

        setResortStatusText(liftieResortDataResponse, openClosedText)


        Picasso.get().load(staticData.imageUrl).fit().into(heroImage)
        liftOpenBar.progress = liftieResortDataResponse.lifts?.stats?.percentage?.open?.toBigDecimal()?.toInt()!!
    }

    private fun setResortStatusText(liftieResortDataResponse: ResortDataItemResponse, openClosedText: TextView) {
        val resortStatusText = Phrase.from(this.resources.getString(R.string.TEMPLATE_resort_open_close)).put("resort", liftieResortDataResponse.name).format()
        val stringBuilder = SpannableStringBuilder()
        var resortStatusString = SpannableString(this.resources.getString(R.string.closed))

        val resortNameString = SpannableString(resortStatusText)
        stringBuilder.append(resortNameString)

        if (liftieResortDataResponse.open != null && liftieResortDataResponse.open) {
            resortStatusString = SpannableString(this.resources.getString(R.string.open))
            resortStatusString.setSpan(ForegroundColorSpan(Color.GREEN), 0, resortStatusString.length, 0)
            stringBuilder.append(resortStatusString)
        }
        else {
            resortStatusString.setSpan(ForegroundColorSpan(Color.RED), 0, resortStatusString.length, 0)
            stringBuilder.append(resortStatusString)
        }

        openClosedText.setText(stringBuilder, TextView.BufferType.SPANNABLE)
    }
}
