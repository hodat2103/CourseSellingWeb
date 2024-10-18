package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.models.*;
import com.example.CourseSellingWeb.responses.order.OrderResponse;
import com.example.CourseSellingWeb.services.account.AccountService;
import com.example.CourseSellingWeb.services.account.AccountServiceImpl;
import com.example.CourseSellingWeb.services.coupon.CouponService;
import com.example.CourseSellingWeb.services.coupon.CouponServiceImpl;
import com.example.CourseSellingWeb.services.employee.EmployeeService;
import com.example.CourseSellingWeb.services.employee.EmployeeServiceImpl;
import com.example.CourseSellingWeb.services.excel.ExcelService;
import com.example.CourseSellingWeb.services.excel.ExcelServiceImpl;
import com.example.CourseSellingWeb.services.order.OrderService;
import com.example.CourseSellingWeb.services.order.OrderServiceImpl;
import com.example.CourseSellingWeb.services.user.UserService;
import com.example.CourseSellingWeb.services.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/excel")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExcelController {

    private final UserServiceImpl userService;
    private final AccountServiceImpl accountService;
    private final CouponServiceImpl couponService;
    private final EmployeeServiceImpl employeeService;
    private final OrderServiceImpl orderService;
    private final ExcelServiceImpl excelService;

    @GetMapping("/export/{data_name}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportExcel(@Valid @PathVariable("data_name") String dataName, HttpServletResponse response) throws Exception {

        if (dataName.equals("users")) {

            List<User> users = userService.getAll();
            excelService.exportToExcel(users, "user_list", response);

        } else if (dataName.equals("accounts")) {

            List<Account> accounts = accountService.getAll();
            excelService.exportToExcel(accounts, "account_list", response);

        } else if (dataName.equals("employees")) {

            List<Employee> employees = employeeService.getAllEmployees();
            excelService.exportToExcel(employees, "employee_list", response);

        } else if (dataName.equals("coupons")) {

            List<Coupon> coupons = couponService.getAllCoupon();
            excelService.exportToExcel(coupons, "coupon_list", response);

        } else if (dataName.equals("orders")) {

            List<Order> orders = orderService.getAllOrders();
            excelService.exportToExcel(orders, "order_list", response);
        }

    }
}
