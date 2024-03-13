package com.example.demo.aspect;

import com.example.demo.entity.audit.Audit;
import com.example.demo.entity.user.User;
import com.example.demo.repository.AuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " + "@annotation(org.springframework.web.bind.annotation.PostMapping) || " + "@annotation(org.springframework.web.bind.annotation.PutMapping) || " + "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            logAudit(joinPoint, true);
        } catch (Exception e) {
            logAudit(joinPoint, false);
            throw e;
        }
        return result;
    }

    private void logAudit(ProceedingJoinPoint joinPoint, boolean success) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null && authentication.getPrincipal() instanceof User) ? ((User) authentication.getPrincipal()).getEmail() : null;
        String roles = (authentication != null) ? authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")) : "";
        String action = joinPoint.getSignature().toShortString();
        String requestParams = Arrays.toString(joinPoint.getArgs());
        Audit audit = Audit.builder().timestamp(LocalDateTime.now()).email(email).roles(roles).action(action).success(success).requestParams(requestParams).build();
        auditLogRepository.save(audit);
    }
}
