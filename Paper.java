/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class representing a single research paper record.
 */
public class Paper implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String author;
    private String year;
    private String category;
    private String abstractText;
    private String pdfPath;      // path to stored PDF file, may be null
    private String dateAdded;    // ISO date string, set automatically on creation

    public Paper(int id, String title, String author, String year,
                 String category, String abstractText, String pdfPath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.abstractText = abstractText;
        this.pdfPath = pdfPath;
        this.dateAdded = LocalDate.now().toString();
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public String getDateAdded() { return dateAdded; }

    public boolean hasPdf() {
        return pdfPath != null && !pdfPath.isEmpty();
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }
}
