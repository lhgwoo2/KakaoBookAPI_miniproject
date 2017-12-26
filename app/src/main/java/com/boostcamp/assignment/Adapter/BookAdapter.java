package com.boostcamp.assignment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.assignment.DTO.DocumentDTO;
import com.boostcamp.assignment.Interface.RecyclerViewClickListener;
import com.boostcamp.assignment.R;
import com.boostcamp.assignment.Util.GlideApp;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @title BookAdapter
 * @detail 검색 리사이클러뷰를 호출하기 위한 어댑터
 * @author 이현기
 */

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected boolean showLoader;
    private List<DocumentDTO> books = new ArrayList<>();
    private Context context;

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

    private RecyclerViewClickListener mListener;

    public BookAdapter(Context context, RecyclerViewClickListener mListener) {
        super();
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.loader_item_layout, parent, false);
            return new LoaderViewHolder(view);
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item, parent, false), mListener);
    }

    /**
     * @title onBindViewHolder
     * @detail 뷰홀더 타입에 따라 로딩 뷰와 아이템 뷰 생성
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Load ViewHolder
        if (holder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) holder;
            if (showLoader) {
                loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.progressBar.setVisibility(View.GONE);
            }
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            GlideApp.with(context.getApplicationContext()).load(books.get(position).getThumbnail()).
                    transition(DrawableTransitionOptions.withCrossFade()).placeholder(R.drawable.book_empty).into(itemViewHolder.coverImageView);

            itemViewHolder.titleTextView.setText(books.get(position).getTitle());
            itemViewHolder.publisherTextView.setText(books.get(position).getPublisher());
            itemViewHolder.authorTextView.setText(authorSum(books.get(position).getAuthors()));
            itemViewHolder.dateTextView.setText(dateSpliter(books.get(position).getDatetime()));
            itemViewHolder.isbnTextView.setText(getFirstISBN(books.get(position).getIsbn()));
            itemViewHolder.descriptionTextView.setText(books.get(position).getContents());
        }
    }

    public DocumentDTO getDocumentDTO(int position) {
        return books.get(position);
    }

    private String authorSum(String[] authors) {
        StringBuilder sb = new StringBuilder();
        for (String s : authors) {
            sb.append(s + " ");
        }
        return sb.toString();
    }

    private String dateSpliter(String dateString) {
        return dateString.split("T")[0];
    }

    private String getFirstISBN(String isbn) {
        String s = "";
        if (isbn.equals("") || isbn == null || isbn.equals(" ")) {
        } else {
            s = "ISBN. " + isbn.split(" ")[0];
        }
        return s;
    }

    public void setBookList(List<DocumentDTO> books) {
        this.books = books;
    }

    public void updateBooks(List<DocumentDTO> books) {
        List<DocumentDTO> list = this.books;
        list.addAll(books);
        this.books = list;
    }

    public int getBookSize() {
        return books.size();
    }

    @Override
    public int getItemCount() {
        return books.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        // loader는 0이 아닌경우와 마지막에서만 생성
        if (position != 0 && position == getItemCount() - 1) {
            return -1;  //loader id는 -1
        }
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }
        return VIEWTYPE_ITEM;
    }

    /**
     * @title showLoading
     * @detail 플래그전환으로 뷰 변경 - 외부에서 호출
     */
    public void showLoading(boolean status) {
        showLoader = status;
    }

}
