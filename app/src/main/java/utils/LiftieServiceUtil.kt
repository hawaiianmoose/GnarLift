package utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import data.ResortDataItemResponse
import data.StaticResortDataItem
import data.Constants
import hawaiianmoose.gnarlift.FavoritesResortRecyclerViewAdapter
import hawaiianmoose.gnarlift.ResortDetailActivity
import hawaiianmoose.gnarlift.ResortRecyclerViewAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.resort_card_view.view.*
import service.LiftieService

object LiftieServiceUtil {

    fun getLiftieDataForResort(staticData: StaticResortDataItem, viewHolder: ResortRecyclerViewAdapter.ViewHolder, parentContext: Context, resortRecyclerViewAdapter: ResortRecyclerViewAdapter) {

        val liftieSubject = PublishSubject.create<ResortDataItemResponse>()
        liftieSubject.subscribe(object : Observer<ResortDataItemResponse> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(liftieResortDataResponse: ResortDataItemResponse) {
                val intent = Intent(parentContext, ResortDetailActivity::class.java).apply {
                    putExtra(Constants.favoritesData, staticData)
                    putExtra(Constants.resortDetailData, liftieResortDataResponse)
                }



                ContextCompat.startActivity(parentContext, intent, null)


                Handler().postDelayed({
                    viewHolder.cardView.resort_card_view.alpha = 1f
                    viewHolder.cardView.loading_animation_view.visibility = View.GONE
                    resortRecyclerViewAdapter.isLoading = false
                },300)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })

        staticData.resortId?.let { LiftieService().fetchResortInfo(it, liftieSubject) }
    }

    fun getLiftieDataForResort(staticData: StaticResortDataItem, viewHolder: ResortRecyclerViewAdapter.ViewHolder, parentContext: Context, resortRecyclerViewAdapter: FavoritesResortRecyclerViewAdapter) {

        val liftieSubject = PublishSubject.create<ResortDataItemResponse>()
        liftieSubject.subscribe(object : Observer<ResortDataItemResponse> {
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(liftieResortDataResponse: ResortDataItemResponse) {
                val intent = Intent(parentContext, ResortDetailActivity::class.java).apply {
                    putExtra(Constants.favoritesData, staticData)
                    putExtra(Constants.resortDetailData, liftieResortDataResponse)
                }

                ContextCompat.startActivity(parentContext, intent, null)

                Handler().postDelayed({
                    viewHolder.cardView.resort_card_view.alpha = 1f
                    viewHolder.cardView.loading_animation_view.visibility = View.GONE
                    resortRecyclerViewAdapter.isLoading = false
                },300)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })

        staticData.resortId?.let { LiftieService().fetchResortInfo(it, liftieSubject) }
    }
}