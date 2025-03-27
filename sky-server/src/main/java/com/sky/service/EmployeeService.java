package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * @title: save
     * @param: employeeDTO
     * @description: 注册新员工
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * @title: getList
     * @param: employeePageQueryDTO
     * @description: 用来对员工列表进行分页查询
     */
    PageResult getList(EmployeePageQueryDTO employeePageQueryDTO);
    
    /**
     * @description: 开启或禁用员工账号
     * @title: startOrStop
     * @param: status, id
     */
    void startOrStop(Integer status, Long id);

    /**
     * @description: 根据id查询用户
     * @title: getById
     * @param: id
     */
    Employee getById(Integer id);

    /**
     * @description: 修改员工信息
     * @title: update
     * @param: employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
