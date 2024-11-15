package com.example.SpringAppGB.Aspects;

import com.example.SpringAppGB.model.DTO.JwtRequest;
import com.example.SpringAppGB.model.Project;
import com.example.SpringAppGB.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс аспектов для логирования и мониторинга работы методов.
 * Этот класс использует аннотации AOP для логирования действий в сервисах и репозиториях.
 */
@Aspect
@Component
public class GBAspect {
    private static final Logger logger = LoggerFactory.getLogger(GBAspect.class);


    /**
     * Логирование создания токена авторизации.
     * Этот метод срабатывает перед вызовом метода создания токена и логирует имя пользователя.
     *
     * @param joinPoint точка соединения, предоставляющая информацию о методе
     */
    @Before("execution(* *.createAuthToken(..))")
    public void LoggingAuth(JoinPoint joinPoint) {
        JwtRequest userDTO = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof JwtRequest) {
                userDTO = (JwtRequest) arg;
            }
        }
        logger.info("Сгенерирован jwt токен для пользователя " + (userDTO != null ? userDTO.getUserName() : "Unknown"));
    }

    /**
     * Логирование вызова метода в ProjectService.
     * Этот метод срабатывает перед выполнением любого метода в ProjectService и логирует информацию о вызове.
     *
     * @param joinPoint точка соединения, предоставляющая информацию о методе
     */
    @Before("execution(* *..ProjectService.*(..))")
    public void logProjectServiceMethod(JoinPoint joinPoint) {
        logger.info("Вызван метод: {} с аргументами: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    /**
     * Логирование после завершения метода в ProjectService.
     * Этот метод срабатывает после успешного выполнения метода из ProjectService и логирует результат.
     *
     * @param joinPoint точка соединения, предоставляющая информацию о методе
     * @param result    результат выполнения метода
     */
    @AfterReturning(
            pointcut = "execution(* *..ProjectService.*(..))",
            returning = "result"
    )
    public void logAfterProjectServiceMethodExecution(JoinPoint joinPoint, Object result) {
        logMethodExecution(joinPoint, result);
    }

    /**
     * Логирование вокруг выполнения метода из UserRepository.
     * Этот метод срабатывает до и после выполнения метода из UserRepository и логирует информацию о вызове и результате.
     *
     * @param joinPoint точка соединения, предоставляющая информацию о методе
     * @return результат выполнения метода
     * @throws Throwable возможные исключения, выбрасываемые в процессе выполнения
     */
    @Around("execution(* *..UserRepository.*(..))")
    public Object logAroundProjectRepositoryMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        logger.info("Вызван метод: {} с аргументами: {}", joinPoint.getSignature(), args);
        Object result = joinPoint.proceed();
        logMethodExecution(joinPoint, result);
        return result;
    }

    /**
     * Логирование результата выполнения метода.
     * Этот метод логирует информацию о результатах выполнения метода, включая размер списка или подробности
     * объекта Project или объекта User.
     *
     * @param joinPoint точка соединения, предоставляющая информацию о методе
     * @param result результат выполнения метода
     */
    private void logMethodExecution(JoinPoint joinPoint, Object result) {
        if (result instanceof List) {
            logger.info("Метод {} завершился с аргументами: {} и вернул список размером: {}",
                    joinPoint.getSignature(), joinPoint.getArgs(), ((List<?>) result).size());
        } else if (result instanceof Project) {
            Project project = (Project) result;
            logger.info("Метод {} завершился с аргументами: {} и вернул: Project{id={}, name={}}",
                    joinPoint.getSignature(), joinPoint.getArgs(), project.getId(), project.getName());
        } else if (result instanceof User) {
            User user = (User) result;
            logger.info("Метод {} завершился с аргументами: {} и вернул: User{id={}, name={}}",
                    joinPoint.getSignature(), joinPoint.getArgs(), user.getId(), user.getUserName());
        } else {
            logger.info("Метод {} завершился с аргументами: {} и вернул: {}",
                    joinPoint.getSignature(), joinPoint.getArgs(), result);
        }
    }

}