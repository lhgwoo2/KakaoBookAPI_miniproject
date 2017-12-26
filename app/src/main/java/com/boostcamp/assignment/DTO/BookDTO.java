package com.boostcamp.assignment.DTO;

/**
 * @title BookDTO
 * @detail 검색된 책 전체 아이템
 * @author 이현기
 */
public class BookDTO {

    private MetaDTO meta;
    private DocumentDTO[] documents;

    public MetaDTO getMeta() {
        return meta;
    }

    public void setMeta(MetaDTO meta) {
        this.meta = meta;
    }

    public DocumentDTO[] getDocuments() {
        return documents;
    }

    public void setDocuments(DocumentDTO[] documents) {
        this.documents = documents;
    }
}
