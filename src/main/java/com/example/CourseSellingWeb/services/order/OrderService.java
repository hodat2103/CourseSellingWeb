package com.example.CourseSellingWeb.services.order;

import com.example.CourseSellingWeb.dtos.OrderDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.*;
import com.example.CourseSellingWeb.respositories.OrderDetailRepository;
import com.example.CourseSellingWeb.respositories.OrderRepository;
import com.example.CourseSellingWeb.services.coupon.CouponServiceImpl;
import com.example.CourseSellingWeb.services.course.CourseServiceImpl;
import com.example.CourseSellingWeb.services.employee.EmployeeServiceImpl;
import com.example.CourseSellingWeb.services.user.UserServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceImpl{
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final CourseServiceImpl courseService;
    private final CouponServiceImpl couponService;
    private final EmployeeServiceImpl employeeService;
    private final OrderDetailRepository orderDetailRepository;
    public Order create(OrderDTO orderDTO) throws DataNotFoundException {
        User existsUser = userService.getById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDTO.getUserId()));
        Employee existsEmployee = employeeService.getEmployeeById(orderDTO.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDTO.getEmployeeId()));
        Order order = Order.builder()
                .user(existsUser)
                .orderDate(LocalDateTime.now())
                .totalPrice(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .finalPrice(BigDecimal.ZERO)
                .status(orderDTO.getStatus())
                .paymentMethod(orderDTO.getPaymentMethod())
                .active(orderDTO.isActive())
                .employee(existsEmployee)
                .build();

        orderRepository.save(order);

        Course course = Optional.ofNullable(orderDTO.getOrderDetail())
                .map(detail -> {
                    int courseId = detail.getCourseId();
                    try {
                        return courseService.getCourseById(courseId);
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException());

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .course(course)
                .price(course.getPrice())
                .build();

        orderDetailRepository.save(orderDetail);

        BigDecimal totalPrice = course.getPrice();
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalPrice = totalPrice;

        if (orderDTO.getCouponId() != null) {
            Optional<Coupon> coupon = couponService.getById(orderDTO.getCouponId());
            if (coupon.isPresent()) {
                BigDecimal discountValue = coupon.get().getCouponCondition().getDiscountValue();
                discountAmount = totalPrice.multiply(discountValue);
                finalPrice = totalPrice.subtract(discountAmount);
            }
            order.setCoupon(coupon.get());

        }
        order.setOrderDetail(orderDetail);
        order.setTotalPrice(totalPrice);
        order.setDiscountAmount(discountAmount);
        order.setFinalPrice(finalPrice);

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order update(int id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));

        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setTotalPrice(orderDTO.getTotalPrice());
        existingOrder.setDiscountAmount(orderDTO.getDiscountAmount());
        existingOrder.setFinalPrice(orderDTO.getFinalPrice());
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setActive(orderDTO.isActive());

        if (orderDTO.getCouponId() != null) {
            Coupon coupon = couponService.getById(orderDTO.getCouponId())
                    .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDTO.getCouponId()));
            existingOrder.setCoupon(coupon);
        }

        if (orderDTO.getEmployeeId() > 0) {
            Employee employee = employeeService.getEmployeeById(orderDTO.getEmployeeId())
                    .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + orderDTO.getEmployeeId()));
            existingOrder.setEmployee(employee);
        }

        return orderRepository.save(existingOrder);
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Object[]> calculateRevenueByMonthAndYear(int year, int month) {
        return orderRepository.calculateRevenueByMonthAndYear(year,month);
    }


    @Override
    public List<Object[]> calculateRevenueForQuarters(int year) {
        return orderRepository.calculateQuarterlyRevenueByYear(year);
    }


    public Page<Order> searchOrders(String keyword, Integer employeeId, Integer fieldId, Integer languageId, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        return orderRepository.findOrdersByUserAndCourseTitle(keyword, employeeId, fieldId, languageId, startDateTime, endDateTime, pageable);
    }


    @Override
    public Optional<Order> getById(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        return Optional.ofNullable(order);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        orderOptional.ifPresent(orderRepository :: delete);
    }
}
