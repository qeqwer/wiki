package com.niko.wiki.req;

public class EbookQueryReq extends PageReq{
    private Long id;

    private Long categoryId2;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }


    @Override
    public String toString() {
        return "EbookQueryReq{" +
                "id=" + id +
                ", categoryId2=" + categoryId2 +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}