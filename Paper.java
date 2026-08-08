/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class representing a single research paper record.
 */
public class Paper implements Serializable {
    private static final long serialVersionUID = 2L;

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private int id;
    private String title;
    private String author;
    private String year;
    private String category;   // displayed as "Venue"
    private String keywords;
    private String abstractText;
    private String pdfPath;    // path to stored PDF file, may be null
    private LocalDateTime dateAdded;
    private LocalDateTime dateUpdated;

    public Paper(int id, String title, String author, String year, String category,
                 String keywords, String abstractText, String pdfPath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.keywords = keywords;
        this.abstractText = abstractText;
        this.pdfPath = pdfPath;
        this.dateAdded = LocalDateTime.now();
        this.dateUpdated = this.dateAdded;
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

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public LocalDateTime getDateAdded() { return dateAdded; }
    public LocalDateTime getDateUpdated() { return dateUpdated; }

    public String getFormattedDateAdded() {
        return dateAdded == null ? "-" : dateAdded.format(DISPLAY_FORMAT);
    }

    public String getFormattedDateUpdated() {
        return dateUpdated == null ? "-" : dateUpdated.format(DISPLAY_FORMAT);
    }

    /** Marks this paper as modified right now. Called by PaperManager on every edit. */
    public void touch() {
        this.dateUpdated = LocalDateTime.now();
    }

    public boolean hasPdf() {
        return pdfPath != null && !pdfPath.isEmpty();
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }
}
