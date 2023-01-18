package com.pacoprojects.webmvnsbamigoscodespringsecurity.security;

import com.google.common.collect.Sets;

import java.util.Set;


/* ROLE */
public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.STUDENT_WRITE,
            ApplicationUserPermission.COURSE_READ,
            ApplicationUserPermission.COURSE_WRITE));

    /* Mapeamento @OneToMany - Uma Role tem muitas permissoes*/
    private final Set<ApplicationUserPermission> permissions;

    /* Construtor*/
    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
