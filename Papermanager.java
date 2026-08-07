/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.papermanager;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles persistence and CRUD operations for Paper objects. Papers are
 * serialized to disk; uploaded PDFs are copied into a local repository folder.
 */
public class PaperManager {

    private List<Paper> papers;
    private int nextId;

    private static final String DATA_DIR = "paper_data";
    private static final String DATA_FILE = DATA_DIR + File.separator + "papers.dat";
    private static final String REPO_DIR = DATA_DIR + File.separator + "repository";

    public PaperManager() {
        papers = new ArrayList<>();
        nextId = 1;
        ensureDirs();
        loadPapers();
        if (papers.isEmpty()) {
            seedSampleData();
        }
    }

    /** Adds a few sample papers on first run so the app isn't empty. Safe to remove later. */
    private void seedSampleData() {
        try {
            addPaper("Deep Learning for NLP Applications", "J. Smith, A. Lee", "2023", "IEEE Access", "", null);
            addPaper("A Survey on Blockchain Technology", "R. Kumar, P. Singh", "2022", "Springer", "", null);
            addPaper("Machine Learning in Healthcare", "M. Brown, T. White", "2021", "Elsevier", "", null);
            addPaper("Quantum Computing: An Overview", "L. Johnson", "2020", "ACM Computing Surveys", "", null);
            addPaper("Internet of Things Security Challenges", "S. Ahmad, K. Khan", "2023", "IEEE IoT Journal", "", null);
        } catch (IOException e) {
            System.err.println("Failed to seed sample data: " + e.getMessage());
        }
    }

    private void ensureDirs() {
        new File(DATA_DIR).mkdirs();
        new File(REPO_DIR).mkdirs();
    }

    /**
     * Adds a new paper. sourcePdf may be null if no file was chosen.
     */
    public Paper addPaper(String title, String author, String year, String category,
            String abstractText, File sourcePdf) throws IOException {
        String storedPath = null;
        if (sourcePdf != null) {
            storedPath = copyPdfToRepo(sourcePdf, nextId);
        }
        Paper p = new Paper(nextId, title, author, year, category, abstractText, storedPath);
        papers.add(p);
        nextId++;
        savePapers();
        return p;
    }

    /**
     * Edits an existing paper. newPdf may be null to keep the existing file.
     */
    public boolean editPaper(int id, String title, String author, String year, String category,
            String abstractText, File newPdf) throws IOException {
        Paper p = getPaperById(id);
        if (p == null) {
            return false;
        }

        p.setTitle(title);
        p.setAuthor(author);
        p.setYear(year);
        p.setCategory(category);
        p.setAbstractText(abstractText);

        if (newPdf != null) {
            String storedPath = copyPdfToRepo(newPdf, id);
            p.setPdfPath(storedPath);
        }
        savePapers();
        return true;
    }

    public boolean deletePaper(int id) {
        Paper p = getPaperById(id);
        if (p == null) {
            return false;
        }
        if (p.hasPdf()) {
            File f = new File(p.getPdfPath());
            if (f.exists()) {
                f.delete();
            }
        }
        papers.remove(p);
        savePapers();
        return true;
    }

    public Paper getPaperById(int id) {
        for (Paper p : papers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Paper> getAllPapers() {
        return papers;
    }

    private String copyPdfToRepo(File source, int id) throws IOException {
        String safeName = "paper_" + id + "_" + source.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = Paths.get(REPO_DIR, safeName);
        Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    @SuppressWarnings("unchecked")
    private void loadPapers() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            papers = (List<Paper>) ois.readObject();
            for (Paper p : papers) {
                if (p.getId() >= nextId) {
                    nextId = p.getId() + 1;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load paper data: " + e.getMessage());
        }
    }

    private void savePapers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(papers);
        } catch (IOException e) {
            System.err.println("Failed to save paper data: " + e.getMessage());
        }
    }
}
