package com.sk.skala.jpasample.service;

import com.sk.skala.jpasample.model.Department;
import com.sk.skala.jpasample.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {

        log.debug("#### getAllDepartments() called");
        return departmentRepository.findAll();
    }

}
