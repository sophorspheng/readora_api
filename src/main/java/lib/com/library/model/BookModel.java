package lib.com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ebooktb")
@Setter 
@Getter 
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String subtitle;

    private String isbn;

    private String description;

    private String category;

    private String publisher;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    private String language;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "pdf_file_name")
    private String pdfFileName;

    @Column(name = "pdf_content_type")
    private String pdfContentType;

    @Lob
    @Column(name = "pdf_base64")
    private String pdfBase64;

    @Column(name = "pdf_size_bytes")
    private Long pdfSizeBytes;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    private BigDecimal rating;

    @Column(name = "total_ratings")
    private Integer totalRatings;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected BookModel() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public String getPdfContentType() {
        return pdfContentType;
    }

    public String getPdfBase64() {
        return pdfBase64;
    }

    public Long getPdfSizeBytes() {
        return pdfSizeBytes;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
