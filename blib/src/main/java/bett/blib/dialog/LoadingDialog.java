package bett.blib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;

import bett.blib.R;

/**
 * Created by bett-pc on 4/15/2016.
 */
public class LoadingDialog {
    private static Dialog mDialog;

    public static void show(Activity mActivity) {
        if (mActivity != null) {
            if (mDialog != null)
                mDialog.dismiss();
            mDialog = new Dialog(mActivity);
//            mDialog = new Dialog(mActivity, android.R.style.Theme_Black);
        }

        if (mDialog != null)
            LoadingDialog.showDialogLoading(mDialog, "");
    }

    public static void show(Context mActivity) {
        if (mActivity != null) {
            if (mDialog != null)
                mDialog.dismiss();
            mDialog = new Dialog(mActivity);
        }

        if (mDialog != null)
            LoadingDialog.showDialogLoading(mDialog, "");
    }

    public static void close() {
        try {
            if (mDialog != null)
                mDialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showDialogLoading(Dialog mDialog, String msg) {

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mDialog.setContentView(R.layout.loading_dialog);
//        Window window = mDialog.getWindow();
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);

//        Loading loader = (Loading) mDialog.findViewById(R.id.loader);
//        loader.start();

        mDialog.setCancelable(false);
        mDialog.show();
    }
}
