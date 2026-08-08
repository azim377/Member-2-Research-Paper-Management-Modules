/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.papermanager;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller layer: validates input coming from the GUI and delegates
 * actual persistence work to PaperManager. GUI forms should only talk
 * to this class, never to PaperManager directly.
 */
public class PaperController {

    private final PaperManager manager;

    public PaperController() {
        this.manager = new PaperManager();
    }

    public List<Paper> getAllPapers() {
        return manager.getAllPapers();
    }

    public Paper getPaperById(int id) {
        return manager.getPaperById(id);
    }

    /** Returns null on success, or an error message on validation/IO failure. */
    public String addPaper(String title, String author, String year, String category,
                            String keywords, String abstractText, File pdf) {
        String error = validate(title, author);
        if (error != null) return error;
        try {
            manager.addPaper(title.trim(), author.trim(), safe(year), safe(category),
                    safe(keywords), safe(abstractText), pdf);
            return null;
        } catch (IOException e) {
            return "Failed to save PDF file: " + e.getMessage();
        }
    }

    /** Returns null on success, or an error message on validation/IO failure. */
    public String editPaper(int id, String title, String author, String year, String category,
                             String keywords, String abstractText, File pdf) {
        String error = validate(title, author);
        if (error != null) return error;
        try {
            boolean ok = manager.editPaper(id, title.trim(), author.trim(), safe(year), safe(category),
                    safe(keywords), safe(abstractText), pdf);
            return ok ? null : "Paper not found (it may have been deleted).";
        } catch (IOException e) {
            return "Failed to save PDF file: " + e.getMessage();
        }
    }

    public boolean deletePaper(int id) {
        return manager.deletePaper(id);
    }

    /** Attempts to open the paper's PDF with the system's default viewer. */
    public boolean openPdf(int id) {
        Paper p = manager.getPaperById(id);
        if (p == null || !p.hasPdf()) return false;
        File pdfFile = new File(p.getPdfPath());
        if (!pdfFile.exists()) return false;
        try {
            if (java.awt.Desktop.isDesktopSupported()
                    && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                java.awt.Desktop.getDesktop().open(pdfFile);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Failed to open PDF: " + e.getMessage());
        }
        return false;
    }

    private String validate(String title, String author) {
        if (title == null || title.trim().isEmpty()) return "Title is required.";
        if (author == null || author.trim().isEmpty()) return "Author is required.";
        return null;
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
