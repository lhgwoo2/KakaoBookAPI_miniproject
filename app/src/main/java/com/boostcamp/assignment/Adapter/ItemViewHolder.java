package com.boostcamp.assignment.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boostcamp.assignment.Interface.RecyclerViewClickListener;
import com.boostcamp.assignment.R;

/**
 * @title ItemViewHolder
 * @detail 리사이클러뷰 아이템 홀더
 * @author 이현기
 */

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout itemLayout;
    public ImageView coverImageView;
    public TextView titleTextView;
    public TextView authorTextView;
    public TextView publisherTextView;
    public TextView dateTextView;
    public TextView isbnTextView;
    public TextView descriptionTextView;

    private RecyclerViewClickListener recyclerViewClickListener;

    public ItemViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
        super(itemView);
        this.recyclerViewClickListener = recyclerViewClickListener;

        itemLayout = (LinearLayout) itemView.findViewById(R.id.bookitem_layout_item);
        coverImageView = (ImageView) itemView.findViewById(R.id.bookitem_img_cover);
        titleTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_title);
        authorTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_author);
        publisherTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_publisher);
        isbnTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_isbn_number);
        dateTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_publication_date);
        descriptionTextView = (TextView) itemView.findViewById(R.id.bookitem_tv_description);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            recyclerViewClickListener.onClick(view, getAdapterPosition());
    }
}