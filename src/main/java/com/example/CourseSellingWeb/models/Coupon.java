package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "coupons")
@Builder
@Data
public class Coupon extends BaseEntity implements ExcelExportable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "active")
    private boolean active;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne(mappedBy = "coupon")
    @JsonManagedReference
    private CouponCondition couponCondition;

    @Override
    public Object[] toExcelRow() {
        return new Object[]{id, code, description,couponCondition.getDiscountValue(),couponCondition.getExpirationDate(),couponCondition.getMinimumOrderValue(),active,employee.getName()};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Mã khuyến mãi","Mô tả","Giảm","Hết hạn","Điều kiện giảm","Kích hoạt","Tên nhân viên"};
    }
}
