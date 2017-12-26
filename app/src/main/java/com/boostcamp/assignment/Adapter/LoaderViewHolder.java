package com.boostcamp.assignment.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.boostcamp.assignment.R;


/**
 *      @author 현기
 *      @title LoaderViewHolder
 *      @detail 리사이클러뷰 프로그래스바 아이템 홀더
 */
public class LoaderViewHolder extends RecyclerView.ViewHolder{

    ProgressBar progressBar;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
    }
}
