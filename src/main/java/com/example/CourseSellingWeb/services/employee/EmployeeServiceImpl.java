package com.example.CourseSellingWeb.services.employee;

import com.example.CourseSellingWeb.dtos.EmployeeDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeServiceImpl {
    Employee create(EmployeeDTO employeeDTO) throws InvalidParamException, DataNotFoundException;

    Employee update(int id, EmployeeDTO employeeDTO) throws DataNotFoundException;

    Optional<Employee> getEmployeeById(int employeeId) throws DataNotFoundException;

    List<Employee> getEmployeeByRoleId(int roleId) throws DataNotFoundException;

    List<Employee> getAllEmployees();

    void delete(int employeeId) throws DataNotFoundException;

}
