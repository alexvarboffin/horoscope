package com.walhalla.horolib.ui.fragment.tab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.walhalla.abcsharedlib.Share;

import com.walhalla.horolib.CoreUtil;
import com.walhalla.horolib.FragmentPresenter;

import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZDescription;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.beans.SignWrapper;
import com.walhalla.horolib.beans.DescriptionType;

import com.walhalla.horolib.databinding.FragmentDefaultPrognosisBinding;
import com.walhalla.horolib.ui.fragment.BaseFragment;
import com.walhalla.horolib.helper.ParentFragmentCallback;

import com.walhalla.ui.DLog;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class TabChildFragment extends BaseFragment implements FragmentPresenter.PresenterView {

    private static final String ARG_PARAM1 = "arg_type";
    private static final String ARG_PARAM2 = "arg_name";
    private static final String ARG_PARAM3 = "arg_data";
    private static final String ARG_PARAM4 = "icon";

    private FragmentDefaultPrognosisBinding binding;

    private String name;
    private String date;
    private DescriptionType type;
    private int icon;

    private FragmentPresenter presenter;
    private PopupMenu popup;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popup != null) {
            popup.dismiss();
        }
    }

    public static TabChildFragment getInstance(DescriptionType type, ZodiacSignAstro zodiacSignAstro) {
        TabChildFragment fragment = new TabChildFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, zodiacSignAstro.name);
        args.putString(ARG_PARAM3, zodiacSignAstro.date);
        args.putInt(ARG_PARAM4, zodiacSignAstro.getSignIcon());

        fragment.setArguments(args);
        return fragment;
    }

    public TabChildFragment() {
    }

    // In the child fragment.
    private void onAttachToParentFragment(Fragment fragment) {
        try {
            mListener = (ParentFragmentCallback) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }

        Log.d(TAG, "onAttachToParentFragment: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FragmentPresenter(getActivity(), this);

        onAttachToParentFragment(getParentFragment());

        Bundle args = getArguments();
        if (args != null) {
            type = (DescriptionType) getArguments().getSerializable(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            date = getArguments().getString(ARG_PARAM3);
            icon = getArguments().getInt(ARG_PARAM4);
        }
    }

    private ParentFragmentCallback mListener;


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDefaultPrognosisBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String tmp = String.format(Locale.CANADA, "(%1$s)", date);

        buildGUI();
        binding.success.horoDate.setText(tmp);
//        if (zSign != null) {
//            updateData(zSign);
//        }
        binding.btnRefresh.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.refreshData();
            }
        });

        //@@@       mBinding.btnShare.setOnClickListener(v -> mListener.shareData(mBinding.forecast.getText().toString()));


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
                binding.success.watermark.setText(name + " (" + date + ")" + "\n "
                        + getActivity().getPackageName());

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
        String e = name + " (" + date + ")"
                + "\n "
                // +"https://play.google.com/store/apps/details?id="
                + getActivity().getPackageName()
                + "\n "
                + binding.success.description.getText().toString();

        DLog.d("@@@");
        return e;
    }




    @MainThread
    private void buildGUI() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                String title = "";

                if (Objects.requireNonNull(type) == DescriptionType.DESCRIPTION) {
                    title = String.format(getString(R.string.tab_description), name);
                } else if (type == DescriptionType.MAN) {
                    title = name + " - " + getString(R.string.tab_man);
                } else if (type == DescriptionType.WOMAN) {
                    title = name + " - " + getString(R.string.tab_woman);
                } else if (type == DescriptionType.KIDS) {
                    title = getString(R.string.tab_kids) + " - " + name;
                } else if (type == DescriptionType.LOVE) {
                    title = String.format(getString(R.string.title_in_love), name);
                } else if (type == DescriptionType.CAREER) {
                    title = getString(R.string.title_money_and_career);
                } else if (type == DescriptionType.SEXUALITY) {
                    title = String.format(getString(R.string.title_sexuality), name);
                } else if (type == DescriptionType.HEALTH) {
                    title = getString(R.string.title_health);
                } else if (type == DescriptionType.COMPATIBILITY) {
                    title = getString(R.string.title_compatibility);
                }
                binding.success.horoTitle.setText(title);
                binding.success.imgIcon.setImageResource(icon);
            });
        }
    }


    //@MainThread
    public void updateData(@NonNull SignWrapper wrapper) {
        Log.i(TAG, "updateData: ");
        if (getActivity() != null) {
            //getActivity().runOnUiThread(() -> {
            String text = null;
            ZDescription description = wrapper.description;

            switch (type) {
                case DESCRIPTION:
                    text = description.description;
                    break;
                case MAN:
                    text = description.man;
                    break;

                case WOMAN:
                    text = description.woman;
                    break;

                case KIDS:
                    text = description.kids;
                    break;

                case LOVE:
                    text = description.love;
                    break;

                case CAREER:
                    text = description.career;
                    break;
                case SEXUALITY:
                    text = description.sexuality;
                    break;
                case HEALTH:
                    text = description.health;
                    break;
                case COMPATIBILITY:
                    text = description.compatibility;
                    break;
            }

            if (text == null) {
                binding.success.description.setText(R.string.error_data_load);
                return;
            }


            if (binding != null) {
                binding.error.setVisibility(View.GONE);
                binding.success.getRoot().setVisibility(View.VISIBLE);
                binding.success.description.setText(text);
            }
            //});
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