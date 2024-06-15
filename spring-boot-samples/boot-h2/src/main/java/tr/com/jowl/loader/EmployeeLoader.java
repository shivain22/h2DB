package tr.com.jowl.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tr.com.jowl.model.Employee;
import tr.com.jowl.service.EmployeeService;

import java.util.List;

@Component
@Order(2)
public class EmployeeLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeLoader.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading Employee");
        employeeService.save(new Employee("ibrahim"));
        employeeService.save(new Employee("Ali"));
        employeeService.save(new Employee("Deniz"));
        employeeService.save(new Employee("Deren"));
        employeeService.save(new Employee("Ceren"));
        List<Employee> employeeList = employeeService.findAll();
        logger.info("Employee Size: {}", employeeList.size());
        employeeList.forEach(employee -> logger.info("Employee Id {} name  {}", employee.getId(), employee.getName()));
    }
}
