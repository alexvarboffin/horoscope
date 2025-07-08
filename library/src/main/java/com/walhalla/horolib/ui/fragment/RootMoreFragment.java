package com.walhalla.horolib.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.walhalla.horolib.R;
import com.walhalla.horolib.databinding.FragmentTabMoreBinding;
import com.walhalla.ui.plugins.Module_U;

import com.walhalla.horolib.beans.ZDescription;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.beans.SignWrapper;
import com.walhalla.horolib.beans.DescriptionType;

import com.walhalla.horolib.presentation.view.LoaderView;
import com.walhalla.horolib.adapter.MoreInfoPagerAdapter;
import com.walhalla.horolib.ui.fragment.main.RootForecastsFragment;
import com.walhalla.horolib.ui.fragment.tab.TabChildFragment;
import com.walhalla.horolib.helper.ParentFragmentCallback;

import java.io.InputStream;
import java.util.List;


public class RootMoreFragment extends BaseFragment
        implements LoaderView, ParentFragmentCallback {
    public static final String TAG = "@@@";

    private static final int SHARE_ACTION_ID = 998;
    private FragmentTabMoreBinding mBinding;

    private ZodiacSignAstro mZodiacSignAstro;

//    @Inject
//    NetworkService networkService;

//    @BindView(R.id.iv_step_image)
//    SquareLayout stepImage;
    //BigImageView stepImage;


//    @BindView(R.id.tv_nav_label)
//    TextView navLabel;


//    @BindView(R.id.ib_arrow_right)
//    ImageButton btnNext;
//
//    @BindView(R.id.ib_arrow_left)
//    ImageButton btnPrev;

//    @BindView(R.id.step_description)
//    TextView tvStepDescription;

    private static String ARG_PAGE_STEP;
    private static final String ARG_SELECTED_SIGN = "selected";


    //All data
    private SignWrapper wrapper;
    private int countLoad = 0;


    public RootMoreFragment() {
        //InjHandler.getAppComponent().inject(this);
    }

    public static RootMoreFragment newInstance(@NonNull ZodiacSignAstro zodiacSignAstro) {
        RootMoreFragment fragment = new RootMoreFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SELECTED_SIGN, zodiacSignAstro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        mBinding = FragmentTabMoreBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        mCallback.setActionBarTitle(mZodiacSignAstro.name);

        setupViewPager(mBinding.content.viewPager);
        mBinding.tabs.setupWithViewPager(mBinding.content.viewPager);

        mBinding.swiperefresh.setOnRefreshListener(() -> loadHoroscopeDescription(mZodiacSignAstro));


        mBinding.logo.container.setOnClickListener(v ->
                mCallback.replaceFragmentRequest(RootForecastsFragment.newInstance(mZodiacSignAstro)));

//        appBarLayout.addOnOffsetChangedListener(this);


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mBinding.appBar.getLayoutParams();
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

    private void loadHoroscopeDescription(ZodiacSignAstro o) {
        showLoading();
//        Call<ZDescription> call = networkService.getHoroscopeDescription(o.getId());
//        call.enqueue(new Callback<ZDescription>() {
//            @Override
//            public void onResponse(
//                    @NonNull Call<ZDescription> call,
//                    @NonNull Response<ZDescription> response) {
//
//                hideLoading();
//
//                //getViewState().showError(response.code()+"");
//
//                ZDescription description = response.body();
//                if (description == null) {
//                    showError(R.string.error_loading_data);
//                    return;
//                }
//                ZWrapper wrapper = new ZWrapper(o, null, description);
//                updateData(wrapper);
//            }
//
//            @Override
//            public void onFailure(
//                    @NonNull Call<ZDescription> call,
//                    @NonNull Throwable t) {
//                hideLoading();
//                showError(R.string.error_loading_data);
//            }
//        });


        if (getActivity() != null) {
            String json;
            try {
                Log.d(TAG, "loadHoroscopeDescription: "+"a/" + o.id + ".json");
                InputStream inputStream = getActivity().getAssets().open("a/" + o.id + ".json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, "UTF-8");

                if (!json.isEmpty()) {
                    Gson gson = new Gson();
                    ZDescription description = gson.fromJson(json, ZDescription.class);
                    if (description == null) {
                        showError(R.string.connection_error);
                        return;
                    }
                    wrapper = new SignWrapper(o, null, description);
                    updateData(wrapper);
                }

            } catch (Exception e) {
                Log.i(TAG, "loadHoroscopeDescription: " + e.getLocalizedMessage());
            }
        }
        hideLoading();
    }


    private void setupViewPager(ViewPager viewPager) {
        MoreInfoPagerAdapter adapter = new MoreInfoPagerAdapter(getChildFragmentManager());
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.DESCRIPTION, mZodiacSignAstro), getString(R.string.tab_about_sign));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.KIDS, mZodiacSignAstro), getString(R.string.tab_kids));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.MAN, mZodiacSignAstro), getString(R.string.tab_man));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.WOMAN, mZodiacSignAstro), getString(R.string.tab_woman));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.LOVE, mZodiacSignAstro), getString(R.string.tab_love));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.CAREER, mZodiacSignAstro), getString(R.string.tab_career));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.SEXUALITY, mZodiacSignAstro), getString(R.string.tab_sexuality));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.HEALTH, mZodiacSignAstro), getString(R.string.tab_health));
        adapter.addFragment(TabChildFragment.getInstance(DescriptionType.COMPATIBILITY, mZodiacSignAstro), getString(R.string.tab_compatibility));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(9);
//        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                Log.d(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);

            }
        });
    }


    private void enableDisableSwipeRefresh(boolean enable) {
        if (mBinding.swiperefresh != null) {
            mBinding.swiperefresh.setEnabled(enable);
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
            ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);

            final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            //supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setTitle(mZodiacSignAstro.name);
        }

        animateImageView(mBinding.logo.imageGrey, mZodiacSignAstro.icon, mZodiacSignAstro.color);

        //CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        //mCollapsingToolbar.setTitle("Меню");

        SpannableString content = new SpannableString(mZodiacSignAstro.name);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mBinding.logo.tvSignName.setText(content);
    }

    public void animateImageView(final ImageView v, int icon, int color) {

        ColorFilter filter = new ColorMatrixColorFilter(new float[]{
                0, 0, 0, 0, 255,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 1, 0});////v.getColorFilter();
        v.setImageResource(icon);

        final int orange = ContextCompat.getColor(getContext(), color);
        colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(animation -> {
            float mul = (Float) animation.getAnimatedValue();
            int alphaOrange = adjustAlpha(orange, mul);
            v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
            if (mul == 0.0) {
//                    v.setColorFilter(filter);//null
                v.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white
                ), PorterDuff.Mode.SRC_IN);
            }
        });

        colorAnim.setDuration(1500);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(1);//-1
        colorAnim.start();
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }


//    @OnClick(R.id.ib_arrow_left)
//    void onBackButtonPressed(View view) {
//        mForecastPresenter.prevStepRequest();
//    }
//
//
//    @OnClick(R.id.ib_arrow_right)
//    void onNextButtonPressed(View view) {
//        mCallback.showBottomBanner(true);
//        mForecastPresenter.nextStepRequest();
//    }


    public void onBackPressed() {
        getActivity().onBackPressed();
    }

//    @Override
//    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//        if (offset == 0) {
//            // Collapsed
//        } else {
//            // Not collapsed
//        }
//    }

    @Override
    public void onFinish(String tag, boolean state) {
        if (state) {
            countLoad++;
            Log.i(TAG, "--->: " + tag);
        }

        if (countLoad == 9) {
            loadHoroscopeDescription(mZodiacSignAstro);
        }
    }

    @Override
    public void refreshData() {
        loadHoroscopeDescription(mZodiacSignAstro);
    }

    @Override
    public void shareData(String text) {
//        Intent sendIntent = new Intent();
////        sendIntent.setAction(Intent.ACTION_SEND);
////        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
////        sendIntent.setType("text/plain");
////        startActivity(sendIntent);
        Module_U.shareText(getContext(), text, null);
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
//        int count = mZodiacSign.getSteps();
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
            showGallery(mZodiacSignAstro);
            return true;
        } else if (itemId == SHARE_ACTION_ID) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            for (final Fragment fragment : fragments) {
                if (fragment != null) {
                    if (fragment.getUserVisibleHint()) {
                        //((TabForecastFragment) fragment).showError(error);
                        ((TabChildFragment) fragment).shareContent();
                        break;
                    }
                }
            }
            return true;
        }
//                if (item.getItemId() <= mZodiacSign.getSteps()) {
//                    mMorePresenter.onClickStepPosition(item.getItemId());
//                }
        return super.onOptionsItemSelected(item);
    }


    private ValueAnimator colorAnim;

    @Override
    public void onDetach() {
        super.onDetach();
        if (colorAnim != null && colorAnim.isRunning()) {
            colorAnim.cancel();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (null != getArguments()) {
            mZodiacSignAstro = (ZodiacSignAstro) getArguments().getSerializable(ARG_SELECTED_SIGN);
        }
    }

    @Override
    public void showLoading() {
        mBinding.swiperefresh.post(() -> mBinding.swiperefresh.setRefreshing(true));
    }

    @Override
    public void hideLoading() {
        mBinding.swiperefresh.post(() -> mBinding.swiperefresh.setRefreshing(false));
    }

    @Override
    public void showError(String error) {
        mCallback.makeToast(error);
        errorData(error);
    }

    @Override
    public void showError(int error) {
        showError(getString(error));
    }

    private void errorData(String error) {
//        List<Fragment> fragments = getChildFragmentManager().getFragments();
//        for (final Fragment fragment : fragments) {
//            if (fragment instanceof TabChildFragment) {
//                ((TabChildFragment) fragment).showError(error);
//            }
//        }
    }

    public void showGallery(ZodiacSignAstro zodiacSignAstro) {
//        Intent intent = GalleryActivity.createIntent(getContext(), mZodiacSign);
//        getContext().startActivity(intent);
        mCallback.showGallery();
    }

    private void updateData(@NonNull SignWrapper data) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (final Fragment fragment : fragments) {
            if (fragment instanceof TabChildFragment) {
                ((TabChildFragment) fragment).updateData(data);
            }
        }
        //final int id = mZodiacSign.getId();
//        final int total = mZodiacSign.getSteps();

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
//                            String.format(Locale.CANADA, "mZodiacSign%02d/icon.png", id)
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
//                    String.format(Locale.CANADA, "mZodiacSign%02d/icon.png", id)
//            ));
//        }

//        List<Description> descriptions = mZodiacSign.getDescription();
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


}
