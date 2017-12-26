package com.boostcamp.assignment.DTO;

/**
 *      @author 현기
 *      @title MetaDTO
 *      @detail 검색된 책 전체의 메타 정보
 */
public class MetaDTO {

    private int total_count;
    private int pageable_count;
    private boolean is_end;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getPageable_count() {
        return pageable_count;
    }

    public void setPageable_count(int pageable_count) {
        this.pageable_count = pageable_count;
    }

    public boolean is_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }
}
