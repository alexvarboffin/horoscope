package com.walhalla.horolib.ui.fragment.tab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.walhalla.MBaseApplication;
import com.walhalla.abcsharedlib.Share;
import com.walhalla.horolib.CoreUtil;
import com.walhalla.horolib.FragmentPresenter;

import com.walhalla.horolib.R;
import com.walhalla.horolib.databinding.FragmentDefaultPrognosisBinding;
import com.walhalla.horolib.utils.DateExtractor;
import com.walhalla.horolib.beans.ForecastType;
import com.walhalla.horolib.beans.Prognosis;
import com.walhalla.horolib.beans.SignWrapper;
import com.walhalla.horolib.beans.ZodiacSignAstro;

import com.walhalla.horolib.rest.ForecastResponse;
import com.walhalla.horolib.helper.ParentFragmentCallback;
import com.walhalla.ui.DLog;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class TabForecastFragment extends Fragment implements FragmentPresenter.PresenterView {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "@@@";

    private static final String ARG_OBJ = "key_sign";

    private FragmentDefaultPrognosisBinding binding;

    private ForecastType forecastType;

    private FragmentPresenter presenter;
    private ZodiacSignAstro obj;


    public TabForecastFragment() {
        MBaseApplication.getAppComponent().inject(this);
    }


    //Current zodiac
    //private ZodiacSign zSign;


    private ParentFragmentCallback mListener;

    private PopupMenu popup;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popup != null) {
            popup.dismiss();
        }
    }

    public static TabForecastFragment newInstance(ForecastType type, ZodiacSignAstro sign) {
        TabForecastFragment fragment = new TabForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, type);
        args.putSerializable(ARG_OBJ, sign);
        fragment.setArguments(args);
        return fragment;
    }


    // In the child fragment.
    private void onAttachToParentFragment(Fragment fragment) {
        try {
            mListener = (ParentFragmentCallback) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement ParentFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FragmentPresenter(getActivity(), this);
        onAttachToParentFragment(getParentFragment());
        if (getArguments() != null) {
            forecastType = (ForecastType) getArguments().getSerializable(ARG_PARAM1);
            obj = (ZodiacSignAstro) getArguments().getSerializable(ARG_OBJ);
        }

        //TAG = forecastType.toString();
        DLog.d("@@@");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefaultPrognosisBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buildGUI();
//        if (zSign != null) {
//            updateData(zSign);
//        }
        binding.btnRefresh.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.refreshData();
            }
        });

        //@@@   mBinding.btnShare.setOnClickListener(v -> mListener.shareData(mBinding.forecast.getText().toString()));


        //Child fragment init dataload!
        mListener.onFinish(TAG, true);
        binding.success.llBackground.setOnClickListener(presenter);
        binding.success.left.setOnClickListener(presenter);
        binding.success.right.setOnClickListener(presenter);

        binding.success.rQuoteSave.setOnClickListener(v -> {
            presenter.onSave(getActivity(), binding.success.watermark, binding.success);
        });
        binding.success.rCopyQuote.setOnClickListener(v -> {
            presenter.copyStatus(getActivity(), binding.success.description.getText().toString());
        });
        binding.success.llQuoteShare.setOnClickListener(v -> popup());
    }



    @SuppressLint("NonConstantResourceId")
    private void popup() {
        popup = new PopupMenu(getActivity(), binding.success.llQuoteShare);
        popup.setOnMenuItemClickListener(menuItem -> {

            int itemId = menuItem.getItemId();
            if (itemId == R.id.sub_text) {
                popup.dismiss();
                String extra = makeExtra();
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT, extra);
                intent1.putExtra("com.pinterest.EXTRA_DESCRIPTION", extra);
                intent1.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                startActivity(Intent.createChooser(intent1, "Share Quote"));
                Toast.makeText(getActivity(), "Share as Text", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_share_image) {
                String extra;
                popup.dismiss();
                //Toast.makeText(getActivity(), "Share as Image", Toast.LENGTH_SHORT).show();
                extra = makeExtra();
                binding.success.watermark.setVisibility(View.VISIBLE);
                String aa = _title(forecastType);
                binding.success.watermark.setText(
                        forecastType.name() + (aa == null ? "" : " (" + aa + ")") + "\n "
                                + getActivity().getPackageName()
                );

                Bitmap bitmap = Bitmap.createBitmap(
                        binding.success.llBackground.getWidth(),
                        binding.success.llBackground.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                binding.success.llBackground.draw(canvas);

                Uri uri = CoreUtil.getLocalBitmapUri(getActivity(), bitmap);
                binding.success.watermark.setVisibility(View.INVISIBLE);

                String appName = getString(R.string.app_name);
                Intent intent = Share.makeImageShare(extra);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, appName);

                //BugFix
                //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider
                Intent chooser = Intent.createChooser(intent, appName);
                List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                        .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(chooser);

//                    String rr = rr();
//                    Intent sharePintrestIntent = new Intent(Intent.ACTION_SEND);
//                    sharePintrestIntent.setPackage("com.pinterest");
//                    sharePintrestIntent.putExtra("com.pinterest.EXTRA_DESCRIPTION", rr);
//                    sharePintrestIntent.putExtra("com.pinterest.EXTRA_URL", UConst.GOOGLE_PLAY_CONSTANT + getActivity().getPackageName());
//                    sharePintrestIntent.putExtra("com.pinterest.EXTRA_WEB_TITLE_STRING", UConst.GOOGLE_PLAY_CONSTANT + getActivity().getPackageName());
//
//                    sharePintrestIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    sharePintrestIntent.setType("image/*");
//                    startActivityForResult(sharePintrestIntent, 443);
                return true;
            }
            return false;
        });
        popup.inflate(R.menu.menu_item);
        popup.show();
    }

    private String makeExtra() {
        String aa = _title(forecastType);
        return obj.name
                + (aa == null ? "" : ", " + aa + "") + "\n "
                + (date0 == null ? "" : ", (" + date0 + ")") + "\n "
                + getActivity().getPackageName()
                + "\n "
                // +"https://play.google.com/store/apps/details?id="
                + getActivity().getPackageName()
                + "\n "
                + binding.success.description.getText().toString();

    }


    @MainThread
    private void buildGUI() {

        if (getActivity() != null && isAdded()) {
            successResult0(null);
            getActivity().runOnUiThread(() -> {
                String title = _title(forecastType);
                if (title == null) {
                    setForecast(null);
                }
                binding.success.imgIcon.setImageResource(obj.getSignIcon());
                binding.success.horoTitle.setText(title);
            });
        }
    }

    private String _title(ForecastType type) {
        switch (type) {
            case WEEK:
                return getString(R.string.horo_week);

            case MONTH:
                return getString(R.string.horo_month);
            case YEAR:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                return String.format(getString(R.string.horo_year), year);


//            case YESTERDAY:
//                return getString(R.string.horo_yesterday);
//            case TOMORROW:
//                return getString(R.string.horo_tomorrow);
//            case TODAY:
//                return getString(R.string.horo_today);

            default:
                return null;
        }
    }

    private String date0;

    @MainThread
    public void updateData(@NonNull final SignWrapper wrapper) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                ForecastResponse dataset = wrapper.forecast;

                Prognosis prognosis;
                switch (forecastType) {

                    case YESTERDAY:
                        prognosis = dataset.yesterday;
                        if (!TextUtils.isEmpty(prognosis.date)) {
                            date0 = DateExtractor.fullData(prognosis.date);
                        }
                        setForecast(dataset.yesterday);
                        break;
                    case TOMORROW:
                        if (!TextUtils.isEmpty(dataset.tomorrow.date)) {
                            date0 = DateExtractor.fullData(dataset.tomorrow.date);
                        }
                        setForecast(dataset.tomorrow);
                        break;
                    case TODAY:
                        if (!TextUtils.isEmpty(dataset.today.date)) {
                            date0 = DateExtractor.fullData(dataset.today.date);
                        }
                        setForecast(dataset.today);
                        break;

                    case WEEK:
                        //@@@
                        if (!TextUtils.isEmpty(dataset.week.date)) {
                            date0 = DateExtractor.fullData(dataset.week.date);
                        }
                        setForecast(dataset.week);
                        break;

                    case MONTH:
                        //@@@
                        if (!TextUtils.isEmpty(dataset.month.date)) {
                            date0 = DateExtractor.fullData(dataset.month.date);
                        }
                        prognosis = dataset.month;
                        setForecast(prognosis);
                        break;
                    case YEAR:

                        prognosis = (dataset.year.isEmpty()) ? new Prognosis() : dataset.year.get(0);
                        //@@@
                        if (!TextUtils.isEmpty(prognosis.date)) {
                            date0 = DateExtractor.fullData(prognosis.date);
                        }
                        setForecast(prognosis);
                        break;

                    default:
                        setForecast(null);
                        break;
                }

                if (TextUtils.isEmpty(date0)) {
                    binding.success.horoDate.setVisibility(View.GONE);
                } else {
                    binding.success.horoDate.setVisibility(View.VISIBLE);
                    binding.success.horoDate.setText(date0);
                }
            });
        }
    }


    public void setForecast(Prognosis prognosis) {
        if (prognosis == null) {
            showError0(getString(R.string.connection_error));
            return;
        }

        String content = prognosis.text;
//        String date = "";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
//        try {
//            java.util.Date newDate = dateFormat.parse(prognosis.getDate());
//            dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
//            date = dateFormat.format(newDate);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        date + "|" +

        successResult0(content);
    }

    private void successResult0(String content) {
        binding.success.description.setText(content);
        binding.error.setVisibility(View.GONE);
        binding.success.getRoot().setVisibility(View.VISIBLE);
//@@@        mBinding.btnShare.setVisibility(View.VISIBLE);
    }


    public void showError0(String error) {
        if (isAdded()) {
            binding.success.description.setText(null);
            binding.error.setVisibility(View.VISIBLE);
            binding.errorMessage.setText(error);
            binding.success.getRoot().setVisibility(View.GONE);
//@@@        mBinding.btnShare.setVisibility(View.GONE);
        }
    }

    public void shareContent() {
        mListener.shareData(binding.success.description.getText().toString());
    }

    @Override
    public void setBackgroundResource(int image) {
        try {
            binding.success.llBackground.setBackgroundResource(image);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }
}
