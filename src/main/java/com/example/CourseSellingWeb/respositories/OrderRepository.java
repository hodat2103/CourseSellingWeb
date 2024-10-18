package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT YEAR(o.orderDate) as year, MONTH(o.orderDate) as month, SUM(o.finalPrice) as totalRevenue " +
            "FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "AND (:month IS NULL OR MONTH(o.orderDate) = :month) " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY year, month")
    List<Object[]> calculateRevenueByMonthAndYear(@Param("year") int year, @Param("month") Integer month);



    @Query("SELECT YEAR(o.orderDate) as year, QUARTER(o.orderDate) as quarter, SUM(o.finalPrice) as totalRevenue " +
            "FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "GROUP BY YEAR(o.orderDate), QUARTER(o.orderDate) " +
            "ORDER BY quarter")
    List<Object[]> calculateQuarterlyRevenueByYear(@Param("year") int year);



    @Query("SELECT o FROM Order o " +
            "JOIN o.orderDetail od " +
            "JOIN od.course c " +
            "WHERE (:keyword IS NULL OR o.user.name LIKE %:keyword% OR c.title LIKE %:keyword%) " +
            "AND (:employeeId IS NULL OR :employeeId = 0 OR o.employee.id = :employeeId) " +
            "AND (:fieldId IS NULL OR :fieldId = 0 OR c.field.id = :fieldId) " +
            "AND (:languageId IS NULL OR :languageId = 0 OR c.language.id = :languageId) " +
            "AND (:startDateTime IS NULL OR o.orderDate >= :startDateTime) " +
            "AND (:endDateTime IS NULL OR o.orderDate <= :endDateTime)")
    Page<Order> findOrdersByUserAndCourseTitle(
            @Param("keyword") String keyword,
            @Param("employeeId") Integer employeeId,
            @Param("fieldId") Integer fieldId,
            @Param("languageId") Integer languageId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable);



}
