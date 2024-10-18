package com.example.CourseSellingWeb.services.employee;

import com.example.CourseSellingWeb.dtos.EmployeeDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.Employee;
import com.example.CourseSellingWeb.models.Role;
import com.example.CourseSellingWeb.respositories.AccountRepository;
import com.example.CourseSellingWeb.respositories.EmployeeRepository;
import com.example.CourseSellingWeb.respositories.RoleRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService implements EmployeeServiceImpl {
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Override
    public Employee create(EmployeeDTO employeeDTO) throws InvalidParamException, DataNotFoundException {

        boolean existsEmployee = employeeRepository.existsEmployeeByEmail(employeeDTO.getEmail());

        if(existsEmployee){
            throw new InvalidParamException("Employee existed");
        }

        Account existsAccount = accountRepository.findById(employeeDTO.getAccount())
                .orElseThrow(() -> new DataNotFoundException("Cannot find account with id: " + employeeDTO.getAccount() ));

        Employee newEmployee = new Employee();

        newEmployee.setName(employeeDTO.getName());
        newEmployee.setEmail(employeeDTO.getEmail());
        newEmployee.setPhone(employeeDTO.getPhone());
        newEmployee.setPosition(employeeDTO.getPosition());
        newEmployee.setAccount(existsAccount);

        return  employeeRepository.save(newEmployee);
    }

    @Override
    public Employee update(int id, EmployeeDTO employeeDTO) throws DataNotFoundException {

        Employee existsEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
        if (existsEmployee != null) {
            existsEmployee.setName(employeeDTO.getName());
            existsEmployee.setEmail(existsEmployee.getEmail());
            existsEmployee.setPhone(existsEmployee.getPhone());
            existsEmployee.setPosition(existsEmployee.getPosition());
            employeeRepository.save(existsEmployee);
        }
        return existsEmployee;
    }

    @Override
    public Optional<Employee> getEmployeeById(int employeeId) throws DataNotFoundException {
        Employee existsEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + employeeId ));
        return Optional.ofNullable(existsEmployee);
    }

    @Override
    public List<Employee> getEmployeeByRoleId(int roleId) throws DataNotFoundException {

        Role existsRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + roleId));

        List<Employee> employees = employeeRepository.getEmployeesByRoleId(roleId);

        return employees;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(int employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        optionalEmployee.ifPresent(employeeRepository :: delete);
    }
}
