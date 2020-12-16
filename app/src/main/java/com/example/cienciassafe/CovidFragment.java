package com.example.cienciassafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CovidFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CovidFragment extends Fragment {
    private View v = null;
    public WebView mWebView;

    public CovidFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CovidFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CovidFragment newInstance(String param1, String param2) {
        CovidFragment fragment = new CovidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_covid, container, false);

        if (v != null) {
            mWebView = (WebView) v.findViewById(R.id.webview);
            mWebView.loadUrl("https://ciencias.ulisboa.pt/pt/covid-19-relatorio-situacao-ciencias");

            // Enable Javascript
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // Force links and redirects to open in the WebView instead of in a browser
            mWebView.setWebViewClient(new WebViewClient());
        }

        return v;
    }
}