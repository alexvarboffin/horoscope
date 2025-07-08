package com.walhalla.horolib.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.walhalla.MBaseApplication;
import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.core.ChildItemClickListener;

import com.walhalla.horolib.databinding.FragmentHomeBinding;
import com.walhalla.horolib.presentation.view.LessonView;
import com.walhalla.horolib.repository.HoroscopeRepository;
import com.walhalla.horolib.adapter.SignAdapter;

import java.util.List;

import javax.inject.Inject;


public class HomeFragment extends BaseFragment implements LessonView {

    public static final String TAG = "@@@";
    private FragmentHomeBinding mBinding;


    private SignAdapter mAdapter;
    private List<ZodiacSignAstro> mZodiacSignAstros;

    @Inject
    HoroscopeRepository mHoroscopeRepository;

    public HomeFragment() {
        MBaseApplication.getAppComponent().inject(this);
    }

//Optional
//    @BindView(R.id.tv_id)
//    TextView tvId;
//    @BindView(R.id.tv_name)
//    TextView tvName;
//    @BindView(R.id.tv_steps)
//    TextView tvSteps;
//    @BindView(R.id.tv_rate)
//    TextView tvRate;
//    @BindView(R.id.tv_lock)
//    TextView tvLock;
//    @BindView(R.id.tv_order)
//    TextView tvOrder;
//    @BindView(R.id.tv_black)
//    TextView tvBlack;
//    @BindView(R.id.tv_diamond)
//    TextView tvDiamond;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SetupToolbar
        // Установить Toolbar для замены ActionBar'а.
        //toolbar.setTitle("Гороскоп");
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.include.toolbar);
            getActivity().setTitle(R.string.app_name);
        }
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(R.mipmap.ic_launcher);

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, mBinding.include.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
//        //==========================================================================


        // use this if you want the RecyclerView to look like a vertical list view
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        // use this if you want the RecyclerView to look like a grid view
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columns));
        mAdapter = new SignAdapter(getContext());
        mAdapter.setListener((view1, position) -> {
            switch (view1.getId()) {
//            case R.id.iv_lock:
//                getViewState().loadRewardedVideo();
//                break;

                default:
//                mCallback.showBottomBanner(false);
//                ZodiacSign current = mZodiacSigns.get(position);
//                if (current.getLock() == HoroscopeRepositoryImpl.LOCK) {
//                    mCallback.loadRewardedVideo(current.getId());
//                }
//                String json = gson.toJson(current);
//                mCallback.replaceFragment(ScreenInstruction.newInstance(json, 0));


                    ZodiacSignAstro current = mZodiacSignAstros.get(position);
                    getLessonRequest(current);
                    break;
            }
        });
//        mRecyclerView.addItemDecoration(
//                new SimpleDividerItemDecoration(getResources())
//                //new ItemOffsetDecoration(20)
//                //new PrettyItemDecoration()
//        );
        mBinding.recyclerView.setAdapter(mAdapter);


        //Load data
        mZodiacSignAstros = mHoroscopeRepository.query(getContext());
        loadData(mZodiacSignAstros);
        showBottomAds(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
//        MenuItem item = menu.add(0, GALLERY_ACTION_ID, 0, getString(R.string.action_gallery));
//        item.setIcon(R.drawable.ic_gallery);
//        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gallery) {
            showGallery();
            return true;
        }
        //                if (item.getItemId() <= mZSign.getSteps()) {
//                    mForecastPresenter.onClickStepPosition(item.getItemId());
//                }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBottomAds(boolean b) {
        mCallback.showBottomBanner(b);
    }

    @Override
    public void loadData(@NonNull List<ZodiacSignAstro> data) {
        Log.d(TAG, "updateData: " + data);
        mAdapter.swap(data);
    }


    @Override
    public void getLessonRequest(ZodiacSignAstro o) {
        mCallback.onClickGetLessonRequest(o);
    }

    @Override
    public void showGallery() {
        mCallback.showGallery();
    }


//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        Log.d(TAG, "onPostCreate: ");
//
//        // Синхронизировать состояние переключения после того, как
//        // возникнет onRestoreInstanceState
//        if (toggle != null) {
//            toggle.syncState();
//        }
//    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Передать любые изменения конфигурации переключателям
        // drawer'а

        Log.d(TAG, "onConfigurationChanged: ");

//        if (toggle != null) {
//            toggle.onConfigurationChanged(newConfig);
//        }
    }
}
