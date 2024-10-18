package com.example.CourseSellingWeb.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@Builder
public class Order extends BaseEntity implements ExcelExportable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private OrderDetail orderDetail;

    @Override
    public Object[] toExcelRow() {
        String status = "";

        if(paymentMethod == "vnpay"){
            status = "Đã thanh toán";
        }else if(paymentMethod == "other"){
            status = "Chưa thanh toán";
        }
        return new Object[]{id, user.getName(), orderDate,finalPrice,status,coupon.getCode(),paymentMethod,isActive(),employee.getName()};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"ID","Học viên","Ngày mua","Thành tiền","Tình trạng","Mã khuyến mãi","Phương thức thanh toán","Kích hoạt","Nhân viên"};
    }
}
