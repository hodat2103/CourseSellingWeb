package com.example.CourseSellingWeb.services.order;

import com.example.CourseSellingWeb.dtos.OrderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface OrderServiceImpl {

    Order create(OrderDTO orderDTO) throws DataNotFoundException;

    Order update(int id, OrderDTO orderDTO) throws DataNotFoundException;

    List<Order> getAllOrders();

    List<Object[]> calculateRevenueByMonthAndYear(int year, int month);

    List<Object[]> calculateRevenueForQuarters(int year);


    Page<Order> searchOrders(String keyword,
                             Integer employeeId,
                             Integer fieldId,
                             Integer languageId,
                             LocalDateTime startDateTime,
                             LocalDateTime endDateTime,
                             Pageable pageable);

    Optional<Order> getById(int id) throws DataNotFoundException;

    void delete(int id) throws DataNotFoundException;
}
