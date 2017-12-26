package com.boostcamp.assignment.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boostcamp.assignment.R;


/**
 *      @author 현기
 *      @title DetailActivity
 *      @detail 책 검색 상세페이지로서 웹뷰가 포함된 액티비티
 */

public class DetailActivity extends AppCompatActivity {

    private String title;
    private String url;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        url = intent.getStringExtra(AssignActivity.GET_URL);
        title = intent.getStringExtra(AssignActivity.GET_TITLE);

        TextView tvTitle = (TextView) findViewById(R.id.detail_tv_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        progressBar = (ProgressBar) findViewById(R.id.web_progress);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText(title);


        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

