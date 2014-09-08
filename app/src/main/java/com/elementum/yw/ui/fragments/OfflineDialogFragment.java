package com.elementum.yw.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.elementum.yw.R;

public class OfflineDialogFragment extends DialogFragment {
    public static final String TAG = "offline_fragment";
    public static final String FLAG_HAS_CACHED_DATA = "has_cache_data";
    public static final String FLAG_TERM = "term";
    public static final String FLAG_LOCATION = "location";

    private boolean hasCachedData;
    private String term, location;
    private OnDisplayOfflineDataListener offlineListener;

    public static OfflineDialogFragment newInstanceNoCachedData() {
        OfflineDialogFragment fragment = new OfflineDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FLAG_HAS_CACHED_DATA, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OfflineDialogFragment newInstance(String term, String location) {
        OfflineDialogFragment fragment = new OfflineDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FLAG_HAS_CACHED_DATA, true);
        bundle.putString(FLAG_TERM, term);
        bundle.putString(FLAG_LOCATION, location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            offlineListener = (OnDisplayOfflineDataListener) activity;
        } catch (ClassCastException cce) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            hasCachedData = args.getBoolean(FLAG_HAS_CACHED_DATA, false);
            term = args.getString(FLAG_TERM, "");
            location = args.getString(FLAG_LOCATION, "");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.device_is_offline));
        String message;

        if(hasCachedData){
            message = String.format(getString(R.string.offline_has_data), term, location);
            builder.setPositiveButton(R.string.display_cached, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    offlineListener.onDisplayOfflineData();
                }
            });
            builder.setNegativeButton(R.string.cancel, dismissListener);
        }
        else {
            message = getString(R.string.offline_no_data);
            builder.setNeutralButton(R.string.sad, dismissListener);
        }
        builder.setMessage(message);
        return builder.create();
    }

    private DialogInterface.OnClickListener dismissListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    public interface OnDisplayOfflineDataListener{
        public void onDisplayOfflineData();
    }
}
