package com.elibrary.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int copies;
    private String description;

    public Book() {}

    public Book(int id, String title, String author, String isbn, int copies, String description){
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
        this.description = description;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getAuthor(){ return author; }
    public void setAuthor(String author){ this.author = author; }

    public String getIsbn(){ return isbn; }
    public void setIsbn(String isbn){ this.isbn = isbn; }

    public int getCopies(){ return copies; }
    public void setCopies(int copies){ this.copies = copies; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
}
