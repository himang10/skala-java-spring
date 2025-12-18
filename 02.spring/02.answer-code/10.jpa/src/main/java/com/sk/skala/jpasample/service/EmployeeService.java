package com.sk.skala.jpasample.service;

import com.sk.skala.jpasample.model.Department;
import com.sk.skala.jpasample.model.Employee;
import com.sk.skala.jpasample.repository.DepartmentRepository;
import com.sk.skala.jpasample.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // 모든 직원 조회
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // 특정 부서의 직원 조회
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    // 새 직원 추가
    @Transactional
    public Employee addEmployee(Employee employee, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다: " + departmentId));

        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    // 직원 정보 업데이트
    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails, Long departmentId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("직원을 찾을 수 없습니다: " + id));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다: " + departmentId));

        employee.setName(employeeDetails.getName());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    // 샘플 데이터 초기화 - 단방향 관계만 설정
    @PostConstruct
    @Transactional
    public void initData() {
        // 부서 생성
        Department it = Department.builder().name("IT").build();
        Department hr = Department.builder().name("HR").build();
        Department sales = Department.builder().name("SALES").build();

        departmentRepository.save(it);
        departmentRepository.save(hr);
        departmentRepository.save(sales);

        // 직원 생성 - 직원에서 부서로의 참조만 설정
        Employee emp1 = Employee.builder().name("김철수").department(it).build();
        Employee emp2 = Employee.builder().name("이영희").department(it).build();
        Employee emp3 = Employee.builder().name("박병구").department(hr).build();
        Employee emp4 = Employee.builder().name("홍길동").department(hr).build();
        Employee emp5 = Employee.builder().name("최미나").department(sales).build();
        Employee emp6 = Employee.builder().name("정민수").department(sales).build();

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        employeeRepository.save(emp3);
        employeeRepository.save(emp4);
        employeeRepository.save(emp5);
        employeeRepository.save(emp6);

        System.out.println("샘플 데이터가 초기화되었습니다.");
    }
}
