package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.dto.RoleDTO;
import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO getRoleById(Long roleId);
    List<RoleDTO> getAllRoles();
    RoleDTO updateRole(Long roleId, RoleDTO roleDTO);
    void deleteRole(Long roleId);
    RoleDTO getRoleByName(String name);
}