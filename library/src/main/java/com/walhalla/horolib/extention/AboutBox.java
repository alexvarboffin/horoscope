package com.walhalla.horolib.extention;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.walhalla.horolib.R;


/**
 * Created by combo on 14.04.2017.
 */

public class AboutBox extends DialogFragment {

//    private String timeStamp(@NonNull Context context) {
//        try {
//            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
//            ZipFile zf = new ZipFile(ai.sourceDir);
//            ZipEntry ze = zf.getEntry("classes.dex");
//            long time = ze.getTime();
//            String s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
//            zf.close();
//            return s;
//        } catch (Exception e) {
//            return "Unknown";
//        }
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use a Spannable to allow for links highlighting
//        String aboutText = String.format(getString(R.string.d_about_info),
//                versionName(getContext()), timeStamp(getContext()));


        String aboutText = "Версия: 1.0";
        //getString(R.string.d_about_info),
//        versionName(getContext()), timeStamp(getContext())
        //Generate views to pass to AlertDialog.Builder and to set the text

        TextView title;
        View view = getActivity().getLayoutInflater().inflate(R.layout.about_layout, null);
        title = view.findViewById(R.id.d_about_title);
        //Set the view text
        title.setText(aboutText);
        // Now Linkify the text
        Linkify.addLinks(title, Linkify.ALL);

        //Build and show the dialog
        return new AlertDialog.Builder(getActivity())
                //.setTitle(getContext().getString(R.string.app_name))
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.ok, null)
                .setView(view)
                .create();    //Builder method returns allow for method chaining
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//
//        return about;
//    }

    public static AboutBox newInstance() {
        return new AboutBox();
    }
}