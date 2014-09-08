package com.elementum.yw.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.elementum.yw.R;

public class ProgressFragment extends DialogFragment {

    public final static String TAG = "progress_fragment";

    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        fragment.setCancelable(false);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage(getString(R.string.loading) + "\u2026");

        dialog.setIndeterminate(true);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isVisible())
            super.show(manager, tag);
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    @Override
    public void dismiss() {
        if (isAdded() && !isHidden()) {
            super.dismiss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (isAdded() && !isHidden()) {
            super.dismissAllowingStateLoss();
        }
    }
}
