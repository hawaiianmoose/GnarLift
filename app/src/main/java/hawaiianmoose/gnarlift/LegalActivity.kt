package hawaiianmoose.gnarlift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hawaiianmoose.gnarlift.databinding.ActivityLegalBinding

class LegalActivity : AppCompatActivity() {
    private var _binding: ActivityLegalBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        setContentView(R.layout.activity_legal)

        binding.legalBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}
