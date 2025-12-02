package com.elibrary.model;

public class BookDetail {
    private String title;
    private String[] authors;
    private String isbn;
    private String description;
    private String publisher;
    private Integer pageCount;
    private String thumbnail;
    private Double price; // estimated or provided
    private String source; // google / openlibrary / estimated

    public BookDetail() {}

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }
    public String[] getAuthors(){ return authors; }
    public void setAuthors(String[] authors){ this.authors = authors; }
    public String getIsbn(){ return isbn; }
    public void setIsbn(String isbn){ this.isbn = isbn; }
    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
    public String getPublisher(){ return publisher; }
    public void setPublisher(String publisher){ this.publisher = publisher; }
    public Integer getPageCount(){ return pageCount; }
    public void setPageCount(Integer pageCount){ this.pageCount = pageCount; }
    public String getThumbnail(){ return thumbnail; }
    public void setThumbnail(String thumbnail){ this.thumbnail = thumbnail; }
    public Double getPrice(){ return price; }
    public void setPrice(Double price){ this.price = price; }
    public String getSource(){ return source; }
    public void setSource(String source){ this.source = source; }
}
