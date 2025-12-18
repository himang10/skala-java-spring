package com.sk.skala.jpasample.controller;

import com.sk.skala.jpasample.model.Department;
import com.sk.skala.jpasample.model.Employee;
import com.sk.skala.jpasample.service.DepartmentService;
import com.sk.skala.jpasample.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employee API", description = "직원 및 부서 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Operation(summary = "모든 직원 조회", description = "전체 직원 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 직원 목록을 조회함")
    })
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(summary = "모든 부서 조회", description = "전체 부서 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 부서 목록을 조회함")
    })
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @Operation(summary = "부서별 직원 조회", description = "특정 부서에 속한 직원 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 부서별 직원 목록을 조회함"),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
    })
    @GetMapping("/departments/{departmentId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
            @Parameter(description = "부서 ID", example = "1") @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    @Operation(summary = "새 직원 추가", description = "특정 부서에 새 직원을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 직원 추가됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/departments/{departmentId}/employees")
    public ResponseEntity<Employee> addEmployee(
            @RequestBody Employee employee,
            @Parameter(description = "부서 ID", example = "1") @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.addEmployee(employee, departmentId));
    }

    @Operation(summary = "직원 정보 업데이트", description = "직원의 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 직원 정보 업데이트됨"),
            @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음")
    })
    @PutMapping("/employees/{id}/department/{departmentId}")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "직원 ID", example = "1") @PathVariable Long id,
            @RequestBody Employee employee,
            @Parameter(description = "부서 ID", example = "1") @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee, departmentId));
    }
}
