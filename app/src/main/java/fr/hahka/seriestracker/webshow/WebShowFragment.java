package fr.hahka.seriestracker.webshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import fr.hahka.seriestracker.utilitaires.Config;

/**
 * Created by thibautvirolle on 03/03/15.
 * Fragment gérant la WebView des shows
 */
public class WebShowFragment extends WebViewFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = super.onCreateView(inflater, container, savedInstanceState);

        WebView webView = getWebView();
        webView.loadUrl("http://series-tracker.herokuapp.com/shows/"+getArguments().getString(Config.USER_ID));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        return onCreateView;
    }
}
