package com.walhalla.horolib.adapter;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;


import com.walhalla.horolib.R;
import com.walhalla.horolib.activity.ScoopSettingsActivity;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.repository.HoroscopeRepositoryImpl;

import com.walhalla.horolib.ui.fragment.main.RootForecastsFragment;

import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Module_U;

import java.util.List;

public class NavigationAdapter implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity a;
    private Runnable runnable;
    private List<ZodiacSignAstro> zodiacs;


    public static final int RC_CHANGE_THEME = 778;


    public NavigationAdapter(AppCompatActivity activity) {
        this.a = activity;
    }


    private Drawable setTint(AppCompatActivity activity, int d, int color) {
        Drawable raw = AppCompatResources.getDrawable(activity, d);
        Drawable wrappedDrawable = null;
        if (raw != null) {
            int ccc = ContextCompat.getColor(activity, color);
            wrappedDrawable = DrawableCompat.wrap(raw);
            DrawableCompat.setTint(wrappedDrawable, /*color*/ccc);
            return wrappedDrawable;
        } else {
            return raw;
        }
    }

    public void init(AppCompatActivity activity) {
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null); //enabled colored icons

        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.build)).setText(DLog.getAppVersion(activity));

        //menu
        //menu.add
        // Roboto-Regular.ttf


//        MenuItem item = menu.add(null);

        ///MenuItem item = menu.getItem(1);
        //.getSubMenu()

        //совместимость
        HoroscopeRepositoryImpl data = new HoroscopeRepositoryImpl(activity);
        zodiacs = data.query(activity);
        for (ZodiacSignAstro sign : zodiacs) {

            Drawable ic0 = setTint(activity, sign.getSignIcon(), sign.color);

            menu.add(R.id.menu_group_sign, sign.id, Menu.FIRST, sign.name).setIcon(ic0);
            //.setIconTintList(/.............../);
            //subMenu.add(0, sign.getId(), Menu.FIRST, sign.getName()).setIcon(sign.getSignIcon());
            //subMenu.add(1, R.string.sign_02, Menu.FIRST, getString(R.string.sign_01)).setIcon(R.drawable.ic_aries);
            DLog.d("" + sign.color);
        }

        //Добавить аккаунт
//        menu.add(0, 88, Menu.FIRST, "Написать разработчику").setIcon(R.drawable.ic_menu_manage);
//        menu.add(0, 89, Menu.FIRST, "Оценить приложение").setIcon(R.drawable.ic_menu_manage);

        //SubMenu subMenu = menu.addSubMenu("0000000000");

        menu.add(1, R.id.action_settings, Menu.FIRST + 1, R.string.action_settings);
        menu.add(1, R.id.action_about, Menu.FIRST + 1, R.string.action_about);
        //menu.add(1, R.id.action_exit, Menu.FIRST + 1, R.string.action_exit);
    }

    public void replaceFragment00(Fragment fragment) {
        String __CURRENT_TAG__ = fragment.getClass().getSimpleName();

        // update the main content by replacing fragments
        FragmentManager fragmentManager = a.getSupportFragmentManager();
        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
//        FragmentTransaction ftx = fragmentManager.beginTransaction();
//        ft.replace(R.id.container, fragment);
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

//        try {
//            String fragmentTag = fragment.getClass().getName();
//            boolean fragmentPopped = fragmentManager
//                    .popBackStackImmediate(fragmentTag, 0); //popBackStackImmediate - some times crashed
//
//            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {
//
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.addToBackStack(null);//fragment.getClass().getSimpleName()
//                fragmentTransaction.setCustomAnimations(
//                        R.anim.slide_in_right, R.anim.slide_out_left,
//                        R.anim.slide_in_left, R.anim.slide_out_right
//                );
//                fragmentTransaction.replace(R.id.container, fragment);
//                fragmentTransaction.commit();
//            }
//        } catch (IllegalStateException ignored) {
//            // There's no way to avoid getting this if saveInstanceState has already been called.
//        }

        //Clear back stack
        //final int count = getSupportFragmentManager().getBackStackEntryCount();
        try {
            a.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
        }

        // update the main content by replacing fragments
        //Fragment fragment = getHomeFragment(currentTag);
        FragmentTransaction fragmentTransaction = a.getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment, __CURRENT_TAG__);
//                    fragmentTransaction.commitAllowingStateLoss();

//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    fragmentTransaction.replace(R.id.container, fragment);
//                    fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        runnable = null;
        int id = item.getItemId();
        // Set action bar title
//        setTitle(menuItem.getTitle());

        DrawerLayout drawerLayout = a.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);


        int itemId = item.getItemId();
//            case R.id.action_about:
//                AboutBox.Show(MainActivity.this);
//                return true;
        if (itemId == R.id.action_settings) {
            runnable = () -> a.startActivityForResult(ScoopSettingsActivity.createIntent(a), RC_CHANGE_THEME);
        } else if (itemId == R.id.action_about) {
            runnable = () -> {
                //___about_();
                Module_U.aboutDialog(a);
            };

//            case R.id.action_exit:
//                this.finish();
//                return true;
        } else {
            if (id > 0 && id <= 12) {
                runnable = () -> {
                    RootForecastsFragment fragment = RootForecastsFragment.newInstance(zodiacs.get(id - 1));
                    replaceFragment00(fragment);
                };
            }
        }


        if (runnable != null) {
            item.setChecked(true);
            //mDrawerLayout.closeDrawers();
            Handler handler = new Handler();
            handler.postDelayed(() -> runnable.run(), 350);
        }

        return true;
    }
}
