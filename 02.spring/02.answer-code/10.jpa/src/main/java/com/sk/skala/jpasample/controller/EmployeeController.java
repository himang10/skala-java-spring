package com.sk.skala.jpasample.controller;

import com.sk.skala.jpasample.model.Department;
import com.sk.skala.jpasample.model.Employee;
import com.sk.skala.jpasample.service.DepartmentService;
import com.sk.skala.jpasample.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;


    // 모든 직원 조회
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // 모든 부서 조회
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // 부서별 직원 조회
    @GetMapping("/departments/{departmentId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    // 새 직원 추가
    @PostMapping("/departments/{departmentId}/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee,
                                                @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.addEmployee(employee, departmentId));
    }

    // 직원 정보 업데이트
    @PutMapping("/employees/{id}/department/{departmentId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                   @RequestBody Employee employee,
                                                   @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee, departmentId));
    }
}
