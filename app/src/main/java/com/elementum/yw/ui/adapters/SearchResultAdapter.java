package com.elementum.yw.ui.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elementum.yw.R;
import com.elementum.yw.model.Business;
import com.elementum.yw.model.SearchResult;
import com.squareup.picasso.Picasso;


public class SearchResultAdapter extends BaseAdapter {
    SearchResult mSearchResult;

    public SearchResultAdapter(SearchResult searchResult){
        mSearchResult = searchResult;
    }

    public void setData(SearchResult searchResult){
        mSearchResult = searchResult;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSearchResult.getBusinesses().size();
    }

    @Override
    public Business getItem(int i) {
        return mSearchResult.getBusinesses().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Resources res = viewGroup.getResources();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        Business item = getItem(i);

        View itemView = (convertView == null) ?
                inflater.inflate(R.layout.listitem_business, viewGroup, false) : convertView;

        ImageView mapImageView = (ImageView) itemView.findViewById(R.id.mapImageView);
        TextView infoTextView = (TextView) itemView.findViewById(R.id.info);
        TextView nameTextView = (TextView) itemView.findViewById(R.id.name);
        TextView phoneTextView = (TextView) itemView.findViewById(R.id.phone);

        nameTextView.setText(res.getString(R.string.listitem_name, i + 1, item.getName()));
        if(item.getLocation().hasDisplayAddress()){
            infoTextView.setText(item.getLocation().getDisplayAddress());
            infoTextView.setVisibility(View.VISIBLE);
        } else {
            infoTextView.setVisibility(View.GONE);
        }

        if(item.hasPhone()){
            phoneTextView.setText(item.getPhone());
            phoneTextView.setVisibility(View.VISIBLE);
        } else {
            phoneTextView.setVisibility(View.GONE);
        }

        Picasso.with(viewGroup.getContext()).load(item.getStaticMapUrl(res)).into(mapImageView);

        return itemView;
    }
}
