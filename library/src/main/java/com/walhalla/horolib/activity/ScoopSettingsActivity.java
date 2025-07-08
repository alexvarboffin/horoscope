package com.walhalla.horolib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ftinc.scoop.Flavor;
import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.ui.FlavorRecyclerAdapter;
import com.walhalla.horolib.R;
import com.walhalla.horolib.databinding.ActivityScoopSettingsBinding;
import com.walhalla.ui.plugins.Module_U;

import com.walhalla.horolib.SimpleDividerItemDecoration;

public class ScoopSettingsActivity
        extends AppCompatActivity implements FlavorRecyclerAdapter.OnItemClickListener {

    /***********************************************************************************************
     *
     * Config
     *
     */

    private static final String EXTRA_TITLE = "com.ftinc.scoop.intent.EXTRA_TITLE";
    private static final String TAG = "@@@";
    /***********************************************************************************************
     *
     * Variables
     *
     */
    private ActivityScoopSettingsBinding mbinding;
    private FlavorRecyclerAdapter mAdapter;
    private String mTitle;

    /***********************************************************************************************
     *
     * Intent Factories
     *
     */

    public static Intent createIntent(Context context) {
        return new Intent(context, ScoopSettingsActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    /***********************************************************************************************
     *
     * Lifecycle Methods
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scoop.getInstance().apply(this);
        mbinding = ActivityScoopSettingsBinding.inflate(getLayoutInflater());
        setContentView(mbinding.getRoot());
        // Scoop.getInstance().bind(this);

        //parseExtras(savedInstanceState);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(mbinding.toolbar);
            //mBinding.toolbar.setTitle(R.string.title_color_scheme);
            //mBinding.toolbar.setVisibility(View.VISIBLE);
            mbinding.toolbar.setNavigationOnClickListener(v -> finish());
        } else {
            // Show the Up button in the action bar.
            //fail actionBar.setTitle(R.string.title_color_scheme);
            actionBar.setTitle(R.string.title_color_scheme);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toolbarTitle.setText(R.string.title_color_scheme);
        }


        setupRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TITLE, mTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_settings).setVisible(false);
        //menu.findItem(R.id.action_about).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
//        if (itemId == R.id.action_exit || itemId == android.R.id.home) {
//            finish();
//            return true;
//        } else
            if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        mAdapter = new FlavorRecyclerAdapter(this);
        mAdapter.setItemClickListener(this);
        mAdapter.addAll(Scoop.getInstance().getFlavors());
        mAdapter.setCurrentFlavor(Scoop.getInstance().getCurrentFlavor());
        mbinding.recycler.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mbinding.recycler.setAdapter(mAdapter);
        mbinding.recycler.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /***********************************************************************************************
     *
     * Listener Methods
     *
     */

    @Override
    public void onItemClicked(View view, Flavor item, int position) {

        // Update Scoops
        Scoop.getInstance().choose(item);

        // Update adapter
        mAdapter.setCurrentFlavor(item);

        // Restart this activity
        //Intent restart = new Intent(this, ScoopSettingsActivity.class);
        Intent restart = createIntent(this/*, mShare*/);

        setResult(RESULT_OK);
        finish();
        startActivity(restart);
        overridePendingTransition(0, 0);

    }

}
