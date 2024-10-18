package com.example.CourseSellingWeb.services.order_detail;

import com.example.CourseSellingWeb.dtos.OrderDetailDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.Order;
import com.example.CourseSellingWeb.models.OrderDetail;
import com.example.CourseSellingWeb.respositories.OrderDetailRepository;
import com.example.CourseSellingWeb.respositories.OrderRepository;
import com.example.CourseSellingWeb.services.course.CourseServiceImpl;
import com.example.CourseSellingWeb.services.order.OrderServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements OrderDetailServiceImpl{
    private final OrderDetailRepository orderDetailRepository;

    private final OrderServiceImpl orderService;

    private final CourseServiceImpl courseService;

    @Override
    public OrderDetail create(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

        Optional<Order> existsOrder = orderService.getById(orderDetailDTO.getCourseId());
        Course existsCourse = courseService.getCourseById(orderDetailDTO.getCourseId());

        OrderDetail orderDetail = OrderDetail.builder()
                .order(existsOrder.get())
                .course(existsCourse)
                .price(existsCourse.getPrice())
                .build();

//        orderDetail.setOrder(existsOrder.get());
//        orderDetail.setPrice(orderDetailDTO.getPrice());
//        orderDetail.setCourse(existsCourse);

        return orderDetailRepository.save(orderDetail);
    }



    @Override
    public Optional<OrderDetail> getById(int id) throws DataNotFoundException {
        return Optional.ofNullable(orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id)));
    }

    @Override
    public List<OrderDetail> getByOrderId(int orderId) throws DataNotFoundException {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail update(int id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

        Optional<OrderDetail> existsOrderDetail = getById(id);

        Optional<Order> existsOrder = orderService.getById(orderDetailDTO.getCourseId());
        Course existsCourse = courseService.getCourseById(orderDetailDTO.getCourseId());

        existsOrderDetail.get().setOrder(existsOrder.get());
        existsOrderDetail.get().setCourse(existsCourse);
        existsOrderDetail.get().setPrice(existsCourse.getPrice());

        return null;
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        if (orderDetailRepository.existsById(id)) {
            orderDetailRepository.deleteById(id);
        } else {
            throw new DataNotFoundException(MessageKeys.NOT_FOUND + id);
        }
    }
}
