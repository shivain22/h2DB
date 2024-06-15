package tr.com.jowl.service;

import tr.com.jowl.model.Employee;

import java.util.List;

public interface EmployeeService {

    void save(Employee employee);

    Employee find(Long id);

    List<Employee> findAll();
}