package jowl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tr.com.jowl.model.Employee;
import tr.com.jowl.service.EmployeeService;

@RestController
@EnableAutoConfiguration
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id) {
        logger.info("Getting employee with id " + id);

        try {
            Employee employee = employeeService.find(Long.parseLong(id));
            return "Employee: " + employee.getName();
        } catch (Exception e) {
            logger.error("Error occurred while trying to find an employee", e);
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String post(@RequestBody String name) {
        logger.info("New employee created " + name);

        try {
            Employee employee = new Employee(name);
            employeeService.save(employee);
            return "Added employee with id: " + employee.getId();
        } catch (Exception e) {
            logger.error("Error occurred while creating a new employee", e);
            return e.getMessage();
        }
    }
}