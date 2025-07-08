package com.walhalla.horoscope.activity;

import static com.walhalla.horolib.adapter.NavigationAdapter.RC_CHANGE_THEME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.ftinc.scoop.Scoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.walhalla.horoscope.TApp;
import com.walhalla.horolib.Constants;

import com.walhalla.horolib.activity.ScoopSettingsActivity;
import com.walhalla.horolib.databinding.ActivityMainBinding;
import com.walhalla.horolib.ui.fragment.main.RootForecastsFragment;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.walhalla.horolib.adapter.NavigationAdapter;
import com.walhalla.horolib.ui.fragment.GalleryFragment;
import com.walhalla.horolib.ui.fragment.RootMoreFragment;

import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.boilerplate.threading.MainThreadImpl;
import com.walhalla.domain.interactors.AdvertInteractor;
import com.walhalla.domain.interactors.impl.AdvertInteractorImpl;
import com.walhalla.horoscope.R;
import com.walhalla.library.activity.GDPR;
import com.walhalla.ui.DLog;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.core.IOnFragmentInteractionListener;

import com.walhalla.horolib.presentation.view.MainView;
import com.walhalla.horolib.ui.fragment.HomeFragment;


public class MainActivity extends AppCompatActivity
        implements MainView, IOnFragmentInteractionListener {

    private final AdvertInteractor.Callback<View> callback = new AdvertInteractor.Callback<>() {
        @Override
        public void onMessageRetrieved(int id, View message) {
            //DLog.d(message.getClass().getName() + " --> " + message.hashCode());

            if (content != null) {
                //DLog.d("@@@@@@@@@@" + content.getClass().getName());
                try {
                    //content.removeView(message);
                    if (message.getParent() != null) {
                        ((ViewGroup) message.getParent()).removeView(message);
                    }
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER;
                    message.setLayoutParams(params);


                    ViewTreeObserver vto = message.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("ObsoleteSdkInt")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < 16) {
                                message.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            //int width = message.getMeasuredWidth();
                            //int height = message.getMeasuredHeight();
                            //DLog.i("@@@@" + height + "x" + width);
                            //setSpaceForAd(height);
                        }
                    });
                    content.addView(message);

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        }

        @Override
        public void onRetrievalFailed(String error) {
            DLog.d("---->" + error);
        }
    };
    /**
     * Toolbar for activity injected from child fragments...
     * Like ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.include.toolbar);
     */


    private ActivityMainBinding binding;


    //@BindView(R.id.toolbar_title)
    ////    TextView toolbarTitle;
    //
    ////    @BindView(R.id.tv_build)
    ////    TextView build;
    //
    //    //@private InterstitialAd mInterstitialAd;
    //    //private RewardedVideoAd rewardedVideoAd;
    //
    //
    private boolean doubleBackToExitPressedOnce;


 //   private RateAppModule mRateAppModule;

    //private AdView adView;
    private NavigationAdapter navigationAdapter;
    private FrameLayout content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        Scoop.getInstance().apply(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        content = binding.include.bottomButton;

        //Scoop.getInstance().bind(this);
        //setSupportActionBar(mBinding.include.container.);


        navigationAdapter = new NavigationAdapter(this);
        navigationAdapter.init(this);
        //mRateAppModule = new RateAppModule(this);
        //WhiteScreen
        //getLifecycle().addObserver(mRateAppModule);
        ___attach_ads();


        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            //container.post(new Runnable() {
            //@Override
            //public void run() {


//            RootForecastsFragment fragment = RootForecastsFragment.newInstance(
//                    new HoroscopeRepositoryImpl(this).query().get(0)
//            );


            homeScreen();

            //}
            //});
            //@ binding.drawerLayout.post(() -> Module_U.checkUpdate(MainActivity.this));
        }

        /**
         * Admovable
         */


        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //Reward video
        // Use an activity context to get the rewarded video instance.
//        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
//        rewardedVideoAd.setRewardedVideoAdListener(mPresenter);


        //InterstitialAd
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.inter_id));


        //build ads
        interstialBuild();
        //build ads
        rewardedBuild();

//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mPresenter.onInterstitialClose();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int var1) {
//
//                //interstialBuild();
//            }
//        });


        //build.setText(AboutBox.versionName(this));

//                AdRequest.DEVICE_ID_EMULATOR,
//                "D835BCD2872E5FA7FB21AB05AB396F5C"


//        // Start loading the ad in the background.
//        this.adView.setAdListener(new AdListener(adView));
//        //this.adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
//        this.adView.loadAd(new AdRequest.Builder().build());

        GDPR gdpr = new GDPR();
        gdpr.init(this);

        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), TApp.repository);
        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor.selectView(binding.include.bottomButton, callback);

//        AdvertAdmobRepository repository = AdvertAdmobRepository.getInstance(new AdvertConfig() {
//            @Override
//            public String application_id() {
//                return null;
//            }
//
//            @Override
//            public SparseArray<String> banner_ad_unit_id() {
//                SparseArray<String> arr = new SparseArray<>();
//                arr.put(R.id.bottom_container, getString(R.string.b1));
//                return arr;
//            }
//
//            @Override
//            public String interstitial_ad_unit_id() {
//                return null;
//            }
//        });
//
//        this.getLifecycle().addObserver(repository);
//
//        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
//                BackgroundExecutor.getInstance(),
//                MainThreadImpl.getInstance(), repository);
//        interactor.selectView(mBinding.include.bottomContainer, new AdvertInteractor.Callback<View>() {
//            @Override
//            public void onMessageRetrieved(int id, View message) {
//                try {
//                    //viewGroup.removeView(message);
//                    if (message.getParent() != null) {
//                        ((ViewGroup) message.getParent()).removeView(message);
//                    }
//                    mBinding.include.bottomContainer.addView(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRetrievalFailed(String error) {
//                Log.i(TAG, "onRetrievalFailed: ");
//            }
//        });
    }

    private void homeScreen() {

//        final HomeFragment fragment = HomeFragment.newInstance();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, HomeFragment.newInstance())
                //.add(R.id.container, SearchFragment.newInstance(DictionaryType.TAG_ALL))
                .commit();
    }


    @Override
    public void setActionBarTitle(int title) {

        getSupportActionBar().setTitle(title);

        //toolbarTitle.setText(title);

//@        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        try {
//            InputStream is = getAssets().open("");
//            Bitmap bitmap = BitmapFactory.decodeStream(is);
//            toolbar.setNavigationIcon(bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    private void ___attach_ads() {
        /*

        com.google.android.gms.ads.MobileAds.initialize(this, getString(R.string.app_id));
        AdvertConfig c = new AdvertConfig() {
            @Override
            public String application_id() {
                return getString(R.string.app_id);
            }

            @Override
            public String[] banner_ad_unit_id() {
                return new String[]{
                        getString(R.string.banner_ad_unit_id)
                };
            }

            @Override
            public String interstitial_ad_unit_id() {
                return null;
            }
        };

        AdvertAdmobRepository repository = AdvertAdmobRepository.getInstance(c);
        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
                BackgroundExecutor.getInstance(//        handler
                ),
                MainThreadImpl.getInstance(), repository);
        interactor.selectView(mBinding.include.llFooter, new AdvertInteractor.Callback<View>() {
            @Override
            public void onMessageRetrieved(int id, View message) {
                try {
                    //viewGroup.removeView(message);
                    if (message.getParent() != null) {
                        ((ViewGroup) message.getParent()).removeView(message);
                    }
                    mBinding.include.llFooter.addView(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetrievalFailed(String error) {
                Log.i(TAG, "onRetrievalFailed: ");
            }
        });
        getLifecycle().addObserver(repository);*/
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Синхронизировать состояние переключения после того, как
        // возникнет onRestoreInstanceState
//        if (toggle != null) {
//            toggle.syncState();
//        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Передать любые изменения конфигурации переключателям
        // drawer'а

//        if (toggle != null) {
//            toggle.onConfigurationChanged(newConfig);
//        }
    }

    public ActionBarDrawerToggle setupDrawerToggle() {

        // Примечание: Убедитесь, что вы передаёте допустимую ссылку
        // на toolbar
        // ActionBarDrawToggle() не предусматривает в ней
        // необходимости и не будет отображать иконку гамбургера без
        // неё

        return new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
    }

//2
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        noinspection SimplifiableIfStatement
        int itemId = item.getItemId();
//            case R.id.action_about:
//                AboutBox.Show(MainActivity.this);
//                return true;
        if (itemId == R.id.action_about) {
            ___about_();
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
            return true;
        } else if (itemId == R.id.action_rate_app) {
            Launcher.rateUs(this);
            return true;
        } else if (itemId == R.id.action_share_app) {
            Module_U.shareThisApp(this);
            return true;
        } else if (itemId == R.id.action_discover_more_app) {
            Module_U.moreApp(this);
            return true;


//            case R.id.action_testers:
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                // Create and show the dialog.
//                DialogFragment newFragment = new TestersDialogFragment();
//                newFragment.show(ft, "dialog");
//                return true;

//            case R.id.action_exit:
//                this.finish();
//                //System.exit(0);
//                return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;
        } else if (itemId == R.id.action_settings) {
            startActivityForResult(ScoopSettingsActivity.createIntent(this), RC_CHANGE_THEME);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ___about_() {
        Module_U.aboutDialog(this);

                /*
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("tag");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                AboutBox newFragment = AboutBox.newInstance();
                newFragment.show(ft, "tag");
                */
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        menu.add("Crash").setOnMenuItemClickListener(v->{
//            throw new RuntimeException("Test Crash"); // Force a crash
//        });
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHANGE_THEME) {
                recreate();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void rewardedBuild() {
//        if (Utils.isNetworkAvailable(this)) {
//            Log.i(TAG, "rewardedBuild: ");
//            rewardedVideoAd.loadAd(getString(R.string.rewarded_video_id), AdUtil.buildAdRequest());
//        }
    }

//    @Override
//    public void AddToEndStrategy() {
//        Log.d(TAG, "AddToEndStrategy: ");
//    }
//
//    @Override
//    public void AddToEndSingleStrategy() {
//        Log.d(TAG, "AddToEndSingleStrategy: ");
//    }
//
//    @Override
//    public void SingleStateStrategy() {
//        Log.d(TAG, "SingleStateStrategy: ");
//    }
//
//    @Override
//    public void SkipStrategy() {
//        Log.d(TAG, "SkipStrategy: ");
//    }
//
//    @Override
//    public void OneExecuteStrategy() {
//        Log.d(TAG, "OneExecuteStrategy: ");
//    }

    public void interstialBuild() {
        DLog.d("interstialBuild: ");
        //@mInterstitialAd.loadAd(AdUtil.buildAdRequest());
    }


    /**
     * @param b banner is visible
     */
    @Override
    public void showBottomBanner(boolean b) {
        //mBinding.include.banner.setVisibility((b) ? View.VISIBLE : View.GONE);
    }

    /**
     * @param zodiacSignAstro - selected item from HomeFragment
     */
    @Override
    public void onClickGetLessonRequest(ZodiacSignAstro zodiacSignAstro) {
//        this.selectedZodiacSign = zodiacSign;
//        if (zodiacSign.getLock() == LessonState.LOCK.getValue()) {
//            if (rewardedVideoAd.isLoaded()) {
//                rewardedVideoAd.show();
//            } else if (mInterstitialAd.isLoaded()) {
//                rewardedBuild();
//                mInterstitialAd.show();
//            } else {
//                rewardedBuild();
//                interstialBuild();
//                updateData(zodiacSign);
//            }
//        } else {
        showForecast(zodiacSignAstro);
//        }
    }

    @Override
    public void makeToast(String error) {
        Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void replaceFragmentRequest(Fragment fragment) {
        navigationAdapter.replaceFragment00(fragment);
    }

    @Override
    public void rootMoreFragment(ZodiacSignAstro o) {
        RootMoreFragment fragment = RootMoreFragment.newInstance(o);
        replaceFragment(fragment, R.id.container);
//        FragmentManager fm = getSupportFragmentManager();
//        try {
//            String fragmentTag = mm.getClass().getName();
//
//            boolean fragmentPopped = fm
//                    .popBackStackImmediate(fragmentTag, 0); //popBackStackImmediate - some times crashed
//
//            if (!fragmentPopped && fm.findFragmentByTag(fragmentTag) == null) {
//
//                FragmentTransaction ftx = fm.beginTransaction();
//                ftx.addToBackStack(null);//mm.getClass().getSimpleName()
////                ftx.setCustomAnimations(
// R.anim.slide_in_right,
////                        R.anim.slide_out_left, R.anim.slide_in_left,
////                        R.anim.slide_out_right
// );
//                ftx.replace(R.id.container, mm);
//                ftx.commit();
//            }
//        } catch (IllegalStateException e) {
//            DLog.handleException(e);
//        }

//        try {
//            //CURRENT_TAG = fragment.getClass().getSimpleName();
//
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(
//                    //R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit
//
//                     R.anim.slide_in_right,
//                        R.anim.slide_out_left, R.anim.slide_in_left,
//                        R.anim.slide_out_right
//            );
//            //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//            fragmentTransaction.replace(R.id.container, fragment);
//            //fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        } catch (IllegalStateException e) {
//            DLog.d(e.getMessage());
//        }
    }

    @Override
    public void showGallery() {
        GalleryFragment fragment = GalleryFragment.newInstance();
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            //transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.addToBackStack(null);

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commitAllowingStateLoss();//<-- not use commit [java.lang.RuntimeException: Unable to pause activity {com.hat.cap/com.google.android.gms.ads.AdActivity}:
            //java.lang.IllegalStateException:
            //Can not perform this action after onSaveInstanceState  ]
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public void showForecast(@NonNull ZodiacSignAstro zodiacSignAstro) {
//        Gson gson = new GsonBuilder().create();
//        String json = gson.toJson(zodiacSign);
//        replaceFragment();

        RootForecastsFragment fragment = RootForecastsFragment.newInstance(zodiacSignAstro);
        //replaceFragment(fragment, R.id.container);

        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            //transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.addToBackStack(null);

            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commitAllowingStateLoss();//<-- not use commit [java.lang.RuntimeException: Unable to pause activity {com.hat.cap/com.google.android.gms.ads.AdActivity}:
            //java.lang.IllegalStateException:
            //Can not perform this action after onSaveInstanceState  ]
        } catch (Exception e) {
            DLog.handleException(e);
        }


    }

    /**
     * NOT CLEAR STACK
     */
    public void replaceFragment(Fragment fragment, int container) {
        DLog.d("no_clear");
        FragmentManager fm = getSupportFragmentManager();
        try {
            String fragmentTag = fragment.getClass().getName();

            boolean fragmentPopped = fm
                    .popBackStackImmediate(fragmentTag, 0); //popBackStackImmediate - some times crashed

            if (!fragmentPopped && fm.findFragmentByTag(fragmentTag) == null) {

                FragmentTransaction ftx = fm.beginTransaction();
                ftx.addToBackStack(fragment.getClass().getSimpleName());
//                ftx.setCustomAnimations(R.anim.slide_in_right,
//                        R.anim.slide_out_left, R.anim.slide_in_left,
//                        R.anim.slide_out_right);
                ftx.replace(container, fragment);
                ftx.commit();
            }
        } catch (IllegalStateException e) {
            DLog.handleException(e);
        }
    }

    /*
     *
     *
     *
     * */
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        // super.onSaveInstanceState(outState);
        // No call for super(). Bug on API Level > 11.
        //     mAppRate.appReloadTrigger();
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
//        if (mRateAppModule != null) {
//            mRateAppModule.appReloadedHandler();
//        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            final FragmentManager fm = getSupportFragmentManager();
            //Pressed back => return to home screen
            int count = fm.getBackStackEntryCount();
            DLog.d("onBackPressed: " + count);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(count > 0);
            }
            if (count > 0) {
//                fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//fm.getBackStackEntryAt()
                super.onBackPressed();
            } else {//count == 0


//                Dialog
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Leaving this App?")
//                        .setMessage("Are you sure you want to close this application?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
                //super.onBackPressed();


                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);

            }


            /*
            //Next/Prev Navigation
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Leaving this App?")
                        .setMessage("Are you sure you want to close this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else
            {
            super.onBackPressed();
            }
            */

        }
    }

    /**
     * Called when leaving the activity
     */
//    @Override
//    public void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
//        //rewardedVideoAd.pause(this);
//        super.onPause();
//    }

    /**
     * Called when returning to the activity
     */
    //3
//    @Override
//    public void onResume() {
//        super.onResume();
////        if (rewardedVideoAd != null) {//<--- slow
////            rewardedVideoAd.resume(this);
////        }
////        mAppRate = new AppRate(this);
////        mAppRate.resume(this);
//        if (adView != null) {
//            adView.resume();
//        }
//    }

    /**
     * Called before the activity is destroyed
     */
//    @Override
//    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
//        //rewardedVideoAd.destroy(this);
//        super.onDestroy();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permission_received, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.permission_not_allow, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

