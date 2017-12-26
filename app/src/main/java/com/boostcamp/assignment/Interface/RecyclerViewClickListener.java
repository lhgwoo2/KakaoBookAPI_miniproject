package com.boostcamp.assignment.Interface;

import android.view.View;

/**
 *  @title RecyclerViewClickListener
 *  @detail 리사이클러뷰 아이템을 클릭할 때 정보를 액티비티로 보내기 위한 인터페이스
 *  @author 이현기
 */
public interface RecyclerViewClickListener {
    void onClick(View view, int position);
}
