package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role getRoleById(Long roleId);
    List<Role> getAllRoles();
    Role updateRole(Long roleId, Role role);
    void deleteRole(Long roleId);
    Role getRoleByName(String name);
}