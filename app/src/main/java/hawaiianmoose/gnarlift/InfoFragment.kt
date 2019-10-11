package hawaiianmoose.gnarlift

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info.*
import android.content.Intent
import android.net.Uri
import android.content.ActivityNotFoundException

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rate_card_view.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + context?.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)

            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context?.packageName)))
            }
        }

        request_card_view.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gnarlift@gmail.com", null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Resort Request for Gnarlift")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I would love to see {RESORT_NAME} in Gnarlift!")
            startActivity(Intent.createChooser(emailIntent, "Submit resort request email..."))
        }

        legal_card_view.setOnClickListener {
            startActivity(Intent(this.context, LegalActivity::class.java))
        }
    }

}