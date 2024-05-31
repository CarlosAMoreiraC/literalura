package com.literalura.entity;

import com.literalura.model.Author;
import jakarta.persistence.*;

import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "book")
public class BookEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String title;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "language", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language", nullable = false)
    private List<String> language;

    private int downloadCount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AuthorEntity author;

    public BookEntity() {
    }

    public BookEntity(String title, Author a, List<String> language, int downloadCount) {
        this.title = title;
        this.language = language;
        this.downloadCount = downloadCount;
        this.author = new AuthorEntity(a.name(), a.birthYear(), a.deathYear());

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}
