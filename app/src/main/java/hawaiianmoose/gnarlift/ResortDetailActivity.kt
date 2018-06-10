package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import data.Constants
import data.StaticResortDataItem

class ResortDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resort_detail)

        val staticData = intent.extras[Constants.favoritesData] as StaticResortDataItem

        val textView = findViewById<TextView>(R.id.resort_name_title)
        textView.text = staticData.name
    }
}
