package service

import io.reactivex.Observable
import data.Constants
import data.ResortDataItemResponse
import com.google.gson.Gson
import data.Lift
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

class LiftieService {

    private fun getResortData(resortId: String): Observable<ResortDataItemResponse> {
        val client = OkHttpClient()

        val urlBuilder = HttpUrl.parse(Constants.liftieEndpoint)?.newBuilder()
        urlBuilder?.addPathSegment(resortId)
        val url = urlBuilder?.build().toString()

        return Observable.create {
            subscriber ->

            val request = okhttp3.Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val gson = Gson()
            val results = gson.fromJson<ResortDataItemResponse>(response.body()?.string(), ResortDataItemResponse::class.java)

            subscriber.onNext(buildLiftStatusObject(results))
            subscriber.onComplete()
        }
    }

    private fun buildLiftStatusObject(results: ResortDataItemResponse): ResortDataItemResponse {
        results.lifts?.status?.forEach { lift ->
            var boolVal = false
            if (lift.value == "open") { boolVal = true }

            results.liftStatus.add(Lift(lift.key, boolVal))
        }

        return results
    }

    fun fetchResortInfo(resortId: String, liftieListener: PublishSubject<ResortDataItemResponse>) {
        getResortData(resortId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(liftieListener)
    }
}