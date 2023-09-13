package com.pacoprojects.springbootSecurityV5.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


/* ROLE */
public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.STUDENT_WRITE,
            ApplicationUserPermission.COURSE_READ,
            ApplicationUserPermission.COURSE_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(
            ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.COURSE_READ));

    /* Mapeamento @OneToMany - Uma Role tem muitas permissoes*/
    private final Set<ApplicationUserPermission> permissions;

    /* Construtor*/
    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        /* Percorre a Lista de permissoes que sao passadas no construtor de ApplicationUserRole */
        Set<SimpleGrantedAuthority> rolesPermissionsList = getPermissions().stream()

                /* Para cada permissao eh instanciado um novo objeto SimpleGrantedAuthority com o nome da permissao*/
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                /* Todas permissoes dentro da lista sao retornadas como Set<>*/
                .collect(Collectors.toSet());

        /* Renomeia as permissoes para ROLE_X */
        rolesPermissionsList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return rolesPermissionsList;
    }
}
