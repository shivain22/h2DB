package tr.com.jowl.dao.impl;

import org.springframework.stereotype.Repository;
import tr.com.jowl.dao.EmployeeDao;
import tr.com.jowl.model.Employee;

@Repository
public class EmployeeDaoImpl extends GenericDaoImpl<Employee, Long> implements EmployeeDao {

}