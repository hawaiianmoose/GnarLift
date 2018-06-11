package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import data.Constants
import data.ResortDataItemResponse
import data.StaticResortDataItem
import io.reactivex.Observer
import service.LiftieService
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

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
    }
}
