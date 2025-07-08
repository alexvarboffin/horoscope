package com.walhalla.horolib.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.walhalla.horolib.R;
import com.walhalla.horolib.databinding.FragmentGalleryBinding;
import com.walhalla.horolib.utils.Utils;
import com.walhalla.horolib.beans.ZodiacSignAstro;

import com.walhalla.horolib.repository.HoroscopeRepository;
import com.walhalla.horolib.repository.HoroscopeRepositoryImpl;
import com.walhalla.horolib.adapter.GalleryAdapter;
import com.walhalla.horolib.adapter.ItemOffsetDecoration;
import com.walhalla.horolib.ui.fragment.main.RootForecastsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by combo on 10/1/2017.
 */

public class GalleryFragment extends BaseFragment {

    private static final String KEY_LESSON_OBJ = "lesson_obj";

    private FragmentGalleryBinding mBinding;
    private GalleryAdapter mAdapter;

    private List<ZodiacSignAstro> signs = new ArrayList<>();
    private String lessonFormat;
    private String TAG = this.getClass().getSimpleName();

    /**
     * Current zSign
     */
    //private ZodiacSign zSign;
    public GalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    public static GalleryFragment newInstance() {
        GalleryFragment galleryFragment = new GalleryFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(KEY_LESSON_OBJ, zSign);
//        galleryFragment.setArguments(bundle);
        return galleryFragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gallery, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentGalleryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setTitle("ppppppppppppppppppppp");
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.app_name);
                //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
                actionBar.setDisplayHomeAsUpEnabled(true);
                //>>>>>>>>>>>>>>actionBar.setDisplayShowTitleEnabled(false);
                //actionBar.setTitle(R.string.app_name);
            }

        }


        //        lessonFormat = getString(R.string.lesson_step_icon_format);


//        parseExtras(getArguments());//Куда


        HoroscopeRepository repository = new HoroscopeRepositoryImpl(getContext());
        signs = repository.query(getContext());

        mAdapter = new GalleryAdapter(getContext(), signs);
        mAdapter.setPresenter((view1, lessonPosition) ->
        {
            ZodiacSignAstro o = signs.get(lessonPosition);
            if (Utils.isUnlock(getContext())) {
                mCallback.rootMoreFragment(o);
            } else {
                mCallback.replaceFragmentRequest(RootForecastsFragment.newInstance(o));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.gallery_columns));
        mBinding.recyclerView.setLayoutManager(mLayoutManager);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(4);
        mBinding.recyclerView.addItemDecoration(itemDecoration);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


//    private void parseExtras(Bundle arguments) {
//        if (arguments != null) {
//            this.zSign = (ZodiacSign) arguments.getSerializable(KEY_LESSON_OBJ);
//
////            Log.d(TAG, "parseExtras: " + "000" + zSign);
//
////            if (zSign != null) {
//
////                int stepsCount = zSign.getSteps();
////                int id = zSign.getId();
////
////                for (int i = 0; i < stepsCount; i++) {
////                    String lessonImage = String.format(Locale.CANADA, lessonFormat, id, i);
//////                    Log.d(TAG, "parseExtras: " + lessonImage);
////                    strings.add(lessonImage);
////                }
////            }
//        }
//    }
}
