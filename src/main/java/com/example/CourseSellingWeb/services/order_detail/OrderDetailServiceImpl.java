package com.example.CourseSellingWeb.services.order_detail;

import com.example.CourseSellingWeb.dtos.OrderDetailDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderDetailServiceImpl {
    OrderDetail create(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    Optional<OrderDetail> getById(int id) throws DataNotFoundException;

    List<OrderDetail> getByOrderId(int orderId) throws DataNotFoundException;

    OrderDetail update(int idId, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;


    void delete(int id) throws DataNotFoundException;
}
