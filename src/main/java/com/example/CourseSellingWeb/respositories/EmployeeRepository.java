package com.example.CourseSellingWeb.respositories;

import com.example.CourseSellingWeb.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE :roleId IS NULL OR :roleId = 0 OR e.account.role.id = :roleId")
    List<Employee> getEmployeesByRoleId(int roleId);
}
