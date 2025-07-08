package com.walhalla.horolib.ui.fragment.main;

import static com.walhalla.ui.plugins.Module_U.shareText;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.walhalla.MBaseApplication;
import com.walhalla.horolib.R;
import com.walhalla.horolib.databinding.FragmentTabForecastsBinding;
import com.walhalla.horolib.db.ZodiacDatabase;
import com.walhalla.horolib.db.ZodiacSign;
import com.walhalla.horolib.rest.NetworkService;
import com.walhalla.horolib.ui.fragment.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.appbar.AppBarLayout;


import com.walhalla.horolib.ui.fragment.tab.TabForecastFragment;
import com.walhalla.horolib.utils.Utils;
import com.walhalla.horolib.beans.ForecastType;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.beans.SignWrapper;

import com.walhalla.horolib.rest.ForecastResponse;
import com.walhalla.horolib.adapter.ForecastPagerAdapter;
import com.walhalla.horolib.helper.ParentFragmentCallback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.ui.DLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;



public class RootForecastsFragment extends BaseFragment
        implements ParentFragmentCallback, ZodiacPresenter.View //Callback from child fragments
{
    private FragmentTabForecastsBinding binding;
    @Inject
    NetworkService networkService;

    private static String ARG_PAGE_STEP;
    private static final String ARG_SELECTED_INSTRUCTION = "selected";

    private ValueAnimator colorAnim;
    private static final int SHARE_ACTION_ID = 998;
    private ZodiacSignAstro o;

    private int countLoad = 0;
    private Call<ForecastResponse> call;
    private ForecastPagerAdapter adapter;
    private ZodiacPresenter presenter;


    public RootForecastsFragment() {
        MBaseApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTabForecastsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public static RootForecastsFragment newInstance(@NonNull ZodiacSignAstro zodiacSignAstro) {
        RootForecastsFragment fragment = new RootForecastsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_SELECTED_INSTRUCTION, zodiacSignAstro);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        mCallback.setActionBarTitle(o.name);

        setupViewPager(binding.include.viewPager);


        binding.swiperefresh.setOnRefreshListener(() -> loadData());
        if (Utils.isUnlock(getContext())) {
            binding.include2.container.setOnClickListener(v ->
                    mCallback.rootMoreFragment(o)
            );
        }


        binding.appBar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                // Collapsed
            } else {
                // Not collapsed
            }
        });


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams();
        params.setBehavior(new AppBarLayout.Behavior());
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
        }
    }


    private void loadData() {
        if (Utils.isNetworkAvailable0(getContext())) {
            loadDataFromInternet(o);
        } else {
            presenter.getZodiacForecast(o.id);//asynk db call
        }
    }

    @Override
    public void showZodiacForecast(ZodiacSign var0) {
        String weekForecast = var0.text;
        try {
            Gson gson = new GsonBuilder().create();
            ForecastResponse forecast = gson.fromJson(weekForecast, ForecastResponse.class);
            DLog.d("@@@@@@@@@@@" + weekForecast);
            SignWrapper wrapper = new SignWrapper(o, forecast, null);
            updateData0(wrapper);

        } catch (Exception e) {
            DLog.handleException(e);
            showConnectionError0();
        }
    }

    @Override
    public void showNoForecastAvailable() {
        DLog.d("@@");
        showConnectionError0();
    }

    private void showConnectionError0() {
        showError(R.string.connection_error);
    }

    private void setupViewPager(ViewPager viewPager) {
        binding.tabs.setupWithViewPager(viewPager);


        String[] range = getResources().getStringArray(R.array.range);

        if (adapter == null) {
            adapter = new ForecastPagerAdapter(getChildFragmentManager());
            adapter.addFragment(TabForecastFragment.newInstance(ForecastType.WEEK, o), range[3]);


            adapter.addFragment(TabForecastFragment.newInstance(ForecastType.MONTH, o), range[4]);
            adapter.addFragment(TabForecastFragment.newInstance(ForecastType.YEAR, o), range[5]);
        }

//        adapter.addFragment(TabForecastFragment.newInstance(ForecastType.YESTERDAY, o), range[0]);
//        adapter.addFragment(TabForecastFragment.newInstance(ForecastType.TODAY, o), range[1]);
//        adapter.addFragment(TabForecastFragment.newInstance(ForecastType.TOMORROW, o), range[2]);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                //Log.d(TAG, "@@@@@@@@@ onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                //Log.d(TAG, "@@@@@@@@@ onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);

            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);//adapter.getCount()
        viewPager.setCurrentItem(1);


    }

    public void animateImageView(final ImageView v, int icon, int color) {

        ColorFilter filter = new ColorMatrixColorFilter(
                new float[]{
                        0, 0, 0, 0, 255,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 1, 0
                }
        );////v.getColorFilter();
        v.setImageResource(icon);
        if (getContext() != null) {
            final int orange = ContextCompat.getColor(getContext(), color);
            colorAnim = ObjectAnimator.ofFloat(0f, 1f);
            colorAnim.addUpdateListener(animation -> {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(orange, mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
//                    v.setColorFilter(filter);//null
                    v.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white
                    ), PorterDuff.Mode.SRC_IN);
                }
            });

            colorAnim.setDuration(1500);
            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
            colorAnim.setRepeatCount(1);//-1
            colorAnim.start();
        }
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (binding.swiperefresh != null) {
            binding.swiperefresh.setEnabled(enable);
        }
    }

    private void setupToolbar() {
//        Toolbar toolbar = new Toolbar(getContext());
//        toolbar.setId(R.id.toolbar);
//
//        ViewGroup.LayoutParams lp = new CollapsingToolbarLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                R.attr.actionBarSize
//        );
//        toolbar.setLayoutParams(lp);
//        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

            final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            //supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setTitle(o.name);
        }


        animateImageView(binding.include2.imageGrey, o.icon, o.color);

        //CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        //mCollapsingToolbar.setTitle("Меню");

        SpannableString content = new SpannableString(o.name);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.include2.tvSignName.setText(content);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        attachExtendedMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void attachExtendedMenu(Menu menu) {
        MenuItem item = menu.add(0, R.id.action_gallery, 0, getString(R.string.action_gallery));
        item.setIcon(R.drawable.ic_gallery);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        item = menu.add(0, SHARE_ACTION_ID, 0, getString(R.string.action_share));
        item.setIcon(R.drawable.ic_share);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        int count = o.getSteps();
//        for (int i = 0; i <= count; i++) {
//            MenuItem edit_item = menu.add(0, i, 0, "Step " + i);
//            //edit_item.setIcon(R.drawable.arrow_right);
//            //edit_item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (itemId == R.id.action_gallery) {
            if (DLog.nonNull(mCallback)) {
                mCallback.showGallery();
            }
            return true;
        } else if (itemId == SHARE_ACTION_ID) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            for (final Fragment fragment : fragments) {
                if (fragment != null) {
                    if (fragment.getUserVisibleHint()) {
                        //((TabForecastFragment) fragment).showError(error);
                        ((TabForecastFragment) fragment).shareContent();
                        break;
                    }
                }
            }
            return true;
        }
//                if (item.getItemId() <= o.getSteps()) {
//                    mForecastPresenter.onClickStepPosition(item.getItemId());
//                }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (colorAnim != null && colorAnim.isRunning()) {
            colorAnim.cancel();
        }
        if (call != null && call.isExecuted()) {
            call.cancel();
            call = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ZodiacDatabase zodiacDatabase = Room.databaseBuilder(getActivity().getApplicationContext(), ZodiacDatabase.class, "zodiac-db").build();
        ThreadExecutor threadExecutor = new ThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        presenter = new ZodiacPresenter(this, zodiacDatabase, threadExecutor, handler);


        DLog.d("@@@@@@@@@@@@@@@");
        countLoad = 0;

        if (null != getArguments()) {
            o = (ZodiacSignAstro) getArguments().getSerializable(ARG_SELECTED_INSTRUCTION);
        }

        //BigImageViewer.initialize(GlideImageLoader.with(getContext()/*appContext*/));

    }

    @Override
    public void onResume() {
        super.onResume();

        DLog.d("@@@@@@@@@@@@@@@");
        countLoad = 0;

    }

    public void updateData0(@NonNull SignWrapper data) {

        // Сохранение в базу данных
        presenter.insertZodiacSign(data.sign, data.forecast);

//        Log.d(TAG, "updateData: " + getChildFragmentManager().getFragments().size());

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (final Fragment fragment : fragments) {
            if (fragment != null) {
                ((TabForecastFragment) fragment).updateData(data);
            }
        }
        //final int id = o.getId();
//        final int total = o.getSteps();

        //String url = ASSET_SCHEME + String.format(Locale.CANADA, getString(R.string.lesson_step_icon_format), id, step);

//        try {


//            InputStream stream = getContext().getAssets().open(
//                    //String.format(Locale.CANADA, getString(R.string.lesson_step_icon_format), id, step)
//            );
//            Bitmap b = BitmapFactory.decodeStream(stream);
//            Bitmap bitmap = Bitmap.createScaledBitmap(b, 777, 777, false);


//            stepImage.setImageBitmap(bitmap);

//            Glide.with(getContext())
//                    .load(Uri.parse( lessonImage))
//                    //.error(R.mipmap.ic_launcher)
//                    //.centerCrop()
//                    .into(stepImage);


//            stepImage.showImage(Uri.parse(url));
//            stepImage.setProgressIndicator(new ProgressPieIndicator());


//            stepImage.setImage(ImageSource.resource(R.drawable.monkey));
        // ... or ...

        //SCALE_TYPE_CENTER_CROP
        //SCALE_TYPE_CENTER_INSIDE
        //stepImage.setMinimumScaleType(2);//SubsamplingScaleImageView.SCALE_TYPE_CUSTOM


//        ImageSource imageSource = ImageSource.resource(R.drawable.rb_star);
//            imageSource.getSWidth()

//            stepImage.setScaleType(ImageView.ScaleType.FIT_XY);
//            stepImage.getBi
//            stepImage.setImage(ImageSource.bitmap(bitmap));

        ///)))
//            stepImage.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//            stepImage.setImage(
//                    ImageSource.asset(
//                            //"lesson21/icon.png"
//                            String.format(Locale.CANADA, getString(R.string.lesson_step_icon_format), id, step)
//                    )
////                    imageSource
//
//            );
        //stepImage.////android:adjustViewBounds="true"

        // ... or ...
        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));


//            stepImage.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
//                @Override
//                public void onReady() {
//                }
//
//                @Override
//                public void onImageLoaded() {
//
//                }
//
//                @Override
//                public void onPreviewLoadError(Exception e) {
//
//                }
//
//                @Override
//                public void onImageLoadError(Exception e) {
//                    //Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    stepImage.setImage(ImageSource.asset(
//                            String.format(Locale.CANADA, "o%02d/icon.png", id)
//                    ));
//                }
//
//                @Override
//                public void onTileLoadError(Exception e) {
//
//                }
//
//                @Override
//                public void onPreviewReleased() {
//
//                }
//            });
//
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//            stepImage.setImage(ImageSource.asset(
//                    String.format(Locale.CANADA, "o%02d/icon.png", id)
//            ));
//        }

//        List<Description> descriptions = o.getDescription();
//        if (null != descriptions && descriptions.size() >= step) {
//            StringBuilder sb = new StringBuilder();
//            for (Description d : descriptions) {
//                if (d.getId() == step) {
//                    sb.append(d.getText());
//                    sb.append("\n");
//                }
//            }
//            tvStepDescription.setText(sb.toString());
//        }

        //int b = (step > 0) ? View.VISIBLE : View.GONE;


//        btnPrev.setVisibility(View.VISIBLE);
//        navLabel.setVisibility(View.VISIBLE);


//        btnNext.setVisibility((step < total) ? View.VISIBLE : View.GONE);
//        navLabel.setText(
//                String.format(Locale.CANADA, "%d/%d", step, total)
//        );
//
    }

    @Override
    public void onFinish(String tag, boolean state) {
        if (state) {
            countLoad++;
            DLog.d("--->: " + tag + "\t" + state + "\t" + countLoad);
        }

        if (countLoad == 3) {
            loadData();
        }
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void shareData(String text) {
//        Intent sendIntent = new Intent();
////        sendIntent.setAction(Intent.ACTION_SEND);
////        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
////        sendIntent.setType("text/plain");
////        startActivity(sendIntent);
        shareText(getContext(), text, null);
    }


    /**
     * this.getView() != null
     * isAdded
     */
    private void loadDataFromInternet(ZodiacSignAstro zodiacSignAstro) {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        showLoading();

        //String lang = Locale.getDefault().getLanguage().toLowerCase();
        String lang = "ru";

        //DLog.d("loadDataFromInternet: !!!" + time_zone + lang + "!!!!!");

        Map<String, String> map = new HashMap<>();
        map.put("sign_id", zodiacSignAstro.id + "");
        map.put("time_zone", Utils.getTimezone());

        map.put("content_language", lang);
        map.put("mp", "android");
        map.put("mpp", "horo");
        map.put("udid", Utils.uuid());//"b76e1f10a49edb5540605eb934c9e098"
        map.put("DeviceId", Utils.uuid());//"b76e1f10a49edb5540605eb934c9e098"


        /*client=mobile
noMsg=Y
os=Android
os_version=4.4.2
ver=5.0(629)
device_vendor=DOOV
model=DOOV%20S2y
device_type=Smartphone
country=RU
language=ru_RU
timezone=GMT%2B03%3A00
first=googleplay
current=googleplay
device_id=b76e1f10a49edb5540605eb934c9e098*/


        call = networkService.getHoroscope(map);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call,
                                   @NonNull Response<ForecastResponse> response) {
                hideLoading();

                ForecastResponse forecast = response.body();
                if (forecast == null) {
                    showConnectionError0();
                    return;
                }

                SignWrapper wrapper = new SignWrapper(o, forecast, null);
                if (RootForecastsFragment.this.isAdded()) {
                    updateData0(wrapper);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                hideLoading();
                showConnectionError0();
            }
        });
    }


    public void showLoading() {
        binding.swiperefresh.post(() -> binding.swiperefresh.setRefreshing(true));
    }


    public void hideLoading() {
        if (isAdded()) {
            binding.swiperefresh.post(() -> binding.swiperefresh.setRefreshing(false));
        }
    }

    public void showError(int error0) {
        if (isAdded()) {
            String error = getString(error0);
            DLog.d("@@@@" + error + " " + isAdded());
            if (DLog.nonNull(mCallback)) {
                mCallback.makeToast(error);
            }
            errorData0(error);
        }
    }


    //Fragment not attached...
    private void errorData0(String error) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        //DLog.d("<<<@@@@>>>" + error + " " + fragments.size());

        for (final Fragment fragment : fragments) {
            if (nonNulla(fragment) && fragment instanceof TabForecastFragment) {
                //DLog.d("<<<@@@@>>>" + error + " " + fragment.isAdded() + " " + fragment.getClass().getSimpleName());
                ((TabForecastFragment) fragment).showError0(error);
            }
        }
    }

    private boolean nonNulla(Object o) {
        boolean r = o != null;
        DLog.d("~~" + (o != null ? o.getClass().getSimpleName() : null) + " ~~ " + r);
        return r;
    }


}
