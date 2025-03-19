package com.sky.aspectj;

import com.sky.annotation.AutoAnnotation;
import com.sky.constant.AutoFillConstant;
import com.sky.constant.JwtClaimsConstant;
import com.sky.enumeration.OperationType;
import com.sky.utils.ThreadLocalUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Insert;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import static com.sky.enumeration.OperationType.INSERT;
import static com.sky.enumeration.OperationType.UPDATE;

/**
 * @description: 自动填充字段的切面类
 * @author: Excell
 * @data 2025年03月19日 10:49
 */
@Aspect
@Component
@Log4j2
public class AutoFillAspectj {

    /**
     * @description: 通用切点表达式，且具有AutoAnnotation注解
     * @title: autoFillPointcut
     * @param: []
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoAnnotation))")
    private void autoFillPointcut() {
    }

    /**
     * @description: 前置通知
     * @title: autoFill
     * @param: [joinPoint]
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        // 根据joinPoint获取方法上的注解信息，本质上joinPoint也是在类的层面
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 根据方法签名获取注解
        AutoAnnotation annotation = signature.getMethod().getAnnotation(AutoAnnotation.class);
        // 根据注解获取具体的值
        OperationType operationType = annotation.value();

        // 约定增加和修改的第一个参数是实体类，获取实体类，并根据反射进行填充
        Object[] args = joinPoint.getArgs();
        if (args == null && args.length == 0) {
            return;
        }
        // 要填充的字段
        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();
        Map map = (Map) ThreadLocalUtil.get();
        Long id = Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID));

        if (operationType == INSERT) {
            try {
                Method setCreateTimeMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTimeMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUserMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTimeMethod.invoke(entity, now);
                setUpdateTimeMethod.invoke(entity, now);
                setCreateUserMethod.invoke(entity, id);
                setUpdateUserMethod.invoke(entity, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == UPDATE) {
            try {
                Method setUpdateTimeMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTimeMethod.invoke(entity, now);
                setUpdateUserMethod.invoke(entity, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
