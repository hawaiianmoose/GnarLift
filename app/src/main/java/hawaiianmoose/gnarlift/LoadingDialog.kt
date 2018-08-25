package hawaiianmoose.gnarlift

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

class LoadingDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.loading_dialog_fragment, null)

        alertDialogBuilder.setView(view)
        alertDialogBuilder.getContext().getTheme().applyStyle(R.style.Theme_Window_NoMinWidth, true)

        val dialog = alertDialogBuilder.create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }
}