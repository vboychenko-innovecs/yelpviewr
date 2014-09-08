package com.elementum.yw.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.elementum.yw.R;
import com.elementum.yw.application.YWApplication;
import com.elementum.yw.services.network.YelpRestService;
import com.elementum.yw.model.Business;
import com.elementum.yw.model.SearchResult;
import com.elementum.yw.services.network.Cache;
import com.elementum.yw.ui.adapters.SearchResultAdapter;
import com.elementum.yw.ui.fragments.OfflineDialogFragment;
import com.elementum.yw.ui.fragments.ProgressFragment;
import com.elementum.yw.ui.fragments.SearchDialogFragment;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ListActivity implements SearchDialogFragment.OnSearchListener, OfflineDialogFragment.OnDisplayOfflineDataListener {

    YelpRestService restService;

    ProgressFragment progressFragment;
    SearchDialogFragment searchDialogFragment;
    SearchResultAdapter adapter;

    Callback<SearchResult> searchResultCallback = new Callback<SearchResult>() {
        @Override
        public void success(SearchResult searchResult, Response response) {
            progressFragment.dismissAllowingStateLoss();
            updateAdapterData(searchResult);
        }

        @Override
        public void failure(RetrofitError error) {
            progressFragment.dismissAllowingStateLoss();
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        restService = YWApplication.getInstance().getRestService();
        progressFragment = ProgressFragment.newInstance();
        searchDialogFragment = SearchDialogFragment.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            if(YelpRestService.isOnline(this))
                searchDialogFragment.show(getFragmentManager(), SearchDialogFragment.TAG);
            else {
                OfflineDialogFragment offlineDialogFragment;
                if(restService.hasOfflineData()){
                    Cache cache = restService.getOfflineData();
                    offlineDialogFragment = OfflineDialogFragment.newInstance(cache.getTerm(), cache.getLocation());
                }
                else
                    offlineDialogFragment = OfflineDialogFragment.newInstanceNoCachedData();

                offlineDialogFragment.show(getFragmentManager(), OfflineDialogFragment.TAG);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Business item = adapter.getItem(position);
        Intent viewIntent = new Intent();
        viewIntent.setAction(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse(item.getMobileUrl()));
        startActivity(viewIntent);
    }

    private void updateAdapterData(SearchResult searchResult) {
        if(adapter == null){
            adapter = new SearchResultAdapter(searchResult);
            getListView().setAdapter(adapter);
        } else
            adapter.setData(searchResult);
    }

    @Override
    public void onSearch(String term, String location) {
        progressFragment.show(getFragmentManager());
        restService.search(term, location, searchResultCallback);
    }

    @Override
    public void onDisplayOfflineData() {
        Cache cache = restService.getOfflineData();
        updateAdapterData(cache.getSearchResult());
    }
}
