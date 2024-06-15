package tr.com.jowl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.jowl.dao.EmployeeDao;
import tr.com.jowl.model.Employee;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public void save(Employee employee) {
        employeeDao.save(employee);
    }

    @Override
    public Employee find(Long id) {
        return employeeDao.find(Employee.class, id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll(Employee.class);
    }
}
