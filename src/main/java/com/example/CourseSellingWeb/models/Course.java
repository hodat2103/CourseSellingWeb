package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
@Builder
public class Course extends BaseEntity implements ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image_url", length = 300, unique = true)
    private String imageUrl;

    @Column(name = "demo_video_url", length = 300, unique = true)
    private String demoVideoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_type")
    private VideoType videoType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CourseVideo> courseVideos;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "for_free")
    private boolean forFree;

    @OneToMany(mappedBy = "course")
    private List<CouponCondition> couponConditions;

    @Override
    public Object[] toExcelRow() {
        return new Object[]{id,title,mentor.getName(),description,price,videoType,field.getName(),language.getName(),isForFree(),employee.getName()};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Tiêu đề","Người hướng dẫn","Mô tả","Giá","Dạng video","Mảng","Ngôn ngữ","Khóa miễn phí","Nhân viên tạo"};
    }
}
