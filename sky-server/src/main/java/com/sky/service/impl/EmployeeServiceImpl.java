package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.ThreadLocalUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 将employeeDTO对象转化为employee对象
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // 赋值初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 给员工状态进行赋值,1是允许使用
        employee.setStatus(StatusConstant.ENABLE);
        // 创建时间
        // employee.setCreateTime(LocalDateTime.now());
        // // 修改时间
        // employee.setUpdateTime(LocalDateTime.now());
        // // 创建该员工的用户
        // Map map = (Map) ThreadLocalUtil.get();
        // employee.setCreateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));
        // // 修改该员工的用户
        // employee.setUpdateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));
        employeeMapper.insetEmployee(employee);
    }

    @Override
    public PageResult getList(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        // 这里其实是可以直接返回Page<Employee>
        List<Employee> employees = employeeMapper.getList(employeePageQueryDTO);

        Page<Employee> p = (Page<Employee>) employees;

        PageResult pageResult = new PageResult();
        pageResult.setTotal(p.getTotal());
        pageResult.setRecords(p.getResult());

        return pageResult;
    }

    /**
     * @description: 用来实现员工的禁用和开启
     * @title: startOrStop
     * @param: status, id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * @description: 根据id查询数据
     * @title: getById
     * @param: id
     */
    @Override
    public Employee getById(Integer id) {
        Employee employee = employeeMapper.getEmployee(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * @description: 进行员工信息的修改
     * @title: update
     * @param: employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        // 将employeeDTO对象转化为employee对象
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // Map map = (Map) ThreadLocalUtil.get();
        //
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setUpdateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));

        employeeMapper.update(employee);
    }


}





















