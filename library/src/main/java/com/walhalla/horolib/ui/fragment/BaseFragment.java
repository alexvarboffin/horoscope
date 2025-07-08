package com.walhalla.horolib.ui.fragment;

import android.content.Context;

import com.walhalla.horolib.core.IOnFragmentInteractionListener;

import java.util.Locale;

import androidx.fragment.app.Fragment;



/**
 * Created by ponch on 09.03.17.
 */

public abstract class BaseFragment extends
        //MvpAppCompatFragment
        Fragment
{
    protected String TAG = String.format(Locale.CANADA, "%1$s %2$s", getClass().getSimpleName(), hashCode());
    protected IOnFragmentInteractionListener mCallback;



    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//    inflater.inflate(R.menu.menu_calls_fragment, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//        Log.d(TAG, "onCreateOptionsMenu: " + vb());
//    }
//
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        Log.d(TAG, "onPrepareOptionsMenu" + vb());
//    }

//    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        //Log.d(TAG, "onAttach: 1" + vb());

        if (context instanceof IOnFragmentInteractionListener) {
            mCallback = (IOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnFragmentInteractionListener");
        }
    }

//    private String vb() {
//        return " :: isVisible -> " + this.isVisible();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: 2"+ vb());
//    }
//
//
//
//    //From back-stack
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: 3"+ vb());
//        return super.onCreateView(inflater, container, savedInstanceState);
//
//    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d(TAG, "onCreate: 4"+ vb());
//        //....initInstances();
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart: 5"+ vb());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume: 6"+ vb());
//    }
//
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause: 7"+ vb());
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop: 8"+ vb());
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d(TAG, "onDestroyView: 9"+ vb());
//    }
}