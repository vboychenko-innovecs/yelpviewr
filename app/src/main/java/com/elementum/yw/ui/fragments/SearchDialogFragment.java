package com.elementum.yw.ui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elementum.yw.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchDialogFragment extends DialogFragment {
    public static final String TAG = "search_fragment";

    private static final String LOCATION_PATTERN = "([a-zA-Z\\s]+)[,]?\\s*[a-zA-Z]{0,2}";
    Pattern locationPattern;

    EditText etTerm, etLocation;
    Button btnSearch;

    OnSearchListener listener;

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment fragment = new SearchDialogFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSearchListener) activity;
        } catch (ClassCastException cce) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPattern = Pattern.compile(LOCATION_PATTERN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(getString(R.string.search_title));
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etTerm = (EditText) view.findViewById(R.id.term);

        etLocation = (EditText) view.findViewById(R.id.location);
        etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                 @Override
                                                 public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                                                     if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                                             actionId == EditorInfo.IME_ACTION_DONE ||
                                                             event.getAction() == KeyEvent.ACTION_DOWN &&
                                                                     event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                                         InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                         imm.hideSoftInputFromWindow(etLocation.getWindowToken(), 0);
                                                         checkAndPerformSearch();
                                                         return true;
                                                     }
                                                     return false;
                                                 }
                                             }

        );

        btnSearch = (Button) view.findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndPerformSearch();
            }
        });

        return view;
    }

    private void checkAndPerformSearch() {
        String term = etTerm.getText().toString();
        String location = etLocation.getText().toString();

        if (TextUtils.isEmpty(term)) {
            etTerm.requestFocus();
            etTerm.setSelected(true);
            etTerm.setError(getString(R.string.please_enter_term));
            return;
        }

        Matcher matcher = locationPattern.matcher(location);
        if (!matcher.matches()) {
            etLocation.requestFocus();
            etLocation.setSelected(true);
            etLocation.setError(getString(R.string.please_enter_correct_location));
            return;
        }

        if (listener != null) {
            listener.onSearch(term, location);
            dismiss();
        }
    }

    public interface OnSearchListener {
        public void onSearch(String term, String location);
    }
}
