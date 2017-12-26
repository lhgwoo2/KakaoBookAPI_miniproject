package com.boostcamp.assignment.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boostcamp.assignment.Adapter.BookAdapter;
import com.boostcamp.assignment.DTO.BookDTO;
import com.boostcamp.assignment.DTO.DocumentDTO;
import com.boostcamp.assignment.Interface.RecyclerViewClickListener;
import com.boostcamp.assignment.R;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 *  @title  AssignActivity
 *  @detail 카카오 restAPI에서 가져온 정보를 뷰 목록으로 보여주고, 웹뷰로 상세정보를 보여주는 메인 액티비티
 *  @author 이현기
 */
public class AssignActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private EditText tvSearch;
    private TextView tvNoItem;
    private ImageView searchButtonImg;
    private RecyclerView mainRv;
    private TextView tvSearchItem;
    private TextView tvSearchNum;
    private RelativeLayout layoutResult;
    private ProgressBar searchProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BookAdapter bookAdapter;
    private LinearLayoutManager linearLayoutManager;
    private InputMethodManager imm;
    private RecyclerViewClickListener mListener;

    private Handler mHandler;
    private static Gson gson = new Gson();

    private int page = 1;
    private boolean is_end = false;
    private boolean synchLoad = false;

    private String searchKeyword;
    public static final String GET_URL = "positionURL";
    public static final String GET_TITLE = "positionTITLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        mHandler = new Handler();

        initView();
        initListener();
        presentViewMode();
    }

    /**
     *  @title initView
     *  @detail 뷰들을 초기화 하는 메소드
     * */
    public void initView() {
        tvSearch = (EditText) findViewById(R.id.main_tv_search);
        searchButtonImg = (ImageView) findViewById(R.id.main_img_search_button);
        tvNoItem = (TextView) findViewById(R.id.main_tv_noitem);
        mainRv = (RecyclerView) findViewById(R.id.main_rv);
        tvSearchItem = (TextView) findViewById(R.id.main_tv_search_item);
        tvSearchNum = (TextView) findViewById(R.id.main_tv_search_num);
        layoutResult = (RelativeLayout) findViewById(R.id.main_ly_result);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        bookAdapter = new BookAdapter(this, mListener);
        searchProgressBar = (ProgressBar) findViewById(R.id.main_search_progressbar);
        searchProgressBar.setVisibility(View.GONE);
    }

    /**
     *  @title initListener
     *  @detail 검색버튼 리스너, 검색텍스트뷰리스너, 리사이클러뷰 클릭리스너를 초기화한다
     * */
    public void initListener() {
        searchButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNoItem.setVisibility(View.GONE);
                searchProgressBar.setVisibility(View.VISIBLE);
                searchProgressBar.bringToFront();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        searchBooks(getKeyword());
                    }
                }, 1000);


            }
        });

        tvSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tvNoItem.setVisibility(View.GONE);
                    searchProgressBar.setVisibility(View.VISIBLE);
                    searchProgressBar.bringToFront();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page = 1;
                            searchBooks(getKeyword());
                        }
                    }, 1000);
                    return true;
                }
                return false;
            }
        });

        mListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                DocumentDTO documentDTO = bookAdapter.getDocumentDTO(position);
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra(GET_TITLE, documentDTO.getTitle());
                intent.putExtra(GET_URL, documentDTO.getUrl());
                startActivity(intent);
            }
        };

        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {
                    if (!is_end) {
                        if (!synchLoad) {
                            synchLoad = true;
                            loadMoreData();
                        }
                    }
                }

            }
        });


    }

    @Override
    public void onRefresh() {
        page = 1;
        searchBooks(searchKeyword);
    }

    /**
     *  @title presentViewMode
     *  @detail 검색내용이 없을 경우 정보없음 페이지, 있을 경우 컨텐츠를 표시한다
     * */
    public void presentViewMode() {
        if (bookAdapter.getBookSize() == 0) {
            mainRv.setVisibility(View.GONE);
            tvSearchItem.setVisibility(View.GONE);
            tvSearchNum.setVisibility(View.GONE);
            layoutResult.setVisibility(View.GONE);
            tvNoItem.setVisibility(View.VISIBLE);
            mainRv.setAdapter(null);

        } else {
            mainRv.setVisibility(View.VISIBLE);
            tvSearchItem.setVisibility(View.VISIBLE);
            tvSearchNum.setVisibility(View.VISIBLE);
            layoutResult.setVisibility(View.VISIBLE);
            tvNoItem.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mainRv.setLayoutManager(linearLayoutManager);
            mainRv.setAdapter(bookAdapter);
        }
    }

    /**
     *  @title loadMoreData
     *  @detail 어댑터의 플래그를 변환하여 뷰타입을 변경한다
     * */
    private void loadMoreData() {
        bookAdapter.showLoading(true);
        bookAdapter.notifyItemChanged(bookAdapter.getItemCount() - 1);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchBooks(searchKeyword);
            }
        }, 1000);

    }

    // 가상키보드 숨기기
    private void keyboardHide() {
        imm.hideSoftInputFromWindow(tvSearch.getWindowToken(), 0);
    }

    private String getKeyword() {
        return searchKeyword = tvSearch.getText().toString();
    }



    /**
     *  @title searchBooks
     *  @detail kakao Books API에서 정보를 가져와 어댑터에 전달하여 리사이클러뷰로 뿌려주는 역할을 한다.
     *           AsyncTask를 기본으로하여 비동기처리한다.
     * */
    private void searchBooks(final String keyword) {

        AsyncTask asyncTask = new AsyncTask<String, Void, BookDTO>() {
            @Override
            protected BookDTO doInBackground(String... strings) {
                BookDTO bookDTO = null;
                HttpsURLConnection myConnection = null;
                try {
                    URL kakaoURL = new URL(getResources().getString(R.string.bookSearchBaseURL) + strings[0] + "&size=20" + "&page=" + page);
                    myConnection = (HttpsURLConnection) kakaoURL.openConnection();
                    myConnection.setRequestProperty("Authorization", getResources().getString(R.string.apiKey));
                    myConnection.setDoInput(true);

                    if (myConnection.getResponseCode() == 200) {

                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        bookDTO = gson.fromJson(jsonReader, BookDTO.class);

                        return bookDTO;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(BookDTO bookDTO) {
                super.onPostExecute(bookDTO);
                searchProgressBar.setVisibility(View.GONE);
                if (bookDTO == null) return;

                is_end = bookDTO.getMeta().is_end();

                List<DocumentDTO> books = new ArrayList<DocumentDTO>();
                Collections.addAll(books, bookDTO.getDocuments());
                page++;
                mSwipeRefreshLayout.setRefreshing(false);
                if (page == 2) {
                    keyboardHide();
                    bookAdapter.setBookList(books);
                    tvSearchItem.setText(keyword + getResources().getString(R.string.search_item));
                    tvSearchNum.setText(bookDTO.getMeta().getTotal_count() + getResources().getString(R.string.book_num));
                    tvSearch.clearFocus();
                    presentViewMode();

                } else {
                    bookAdapter.showLoading(false);
                    bookAdapter.updateBooks(books);
                    bookAdapter.notifyDataSetChanged();
                    synchLoad = false;
                }
            }
        }.execute(keyword);
    }


}
