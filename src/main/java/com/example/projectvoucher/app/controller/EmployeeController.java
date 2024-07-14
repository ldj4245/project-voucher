package com.example.projectvoucher.app.controller;


import com.example.projectvoucher.app.controller.request.EmployeeCreateRequest;
import com.example.projectvoucher.app.controller.response.EmployeeResponse;
import com.example.projectvoucher.domain.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    //회원 생성
    @PostMapping("/api/v1/employee")
    public Long create(@RequestBody final EmployeeCreateRequest request){
        return employeeService.create(request);
    }


    //회원 조회
   @GetMapping("/api/v1/employee/{no}")
    public EmployeeResponse get(@PathVariable final Long no) {
        return employeeService.get(no);
    }


}
