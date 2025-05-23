package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.service.interfaces.RoleService;

import jakarta.validation.ValidationException;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()) != null) {
            throw new ValidationException("Role with name " + role.getName() + " already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + roleId));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(Long roleId, Role role) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + roleId));
        if (!existingRole.getName().equals(role.getName()) && roleRepository.findByName(role.getName()) != null) {
            throw new ValidationException("Role with name " + role.getName() + " already exists");
        }
        role.setRoleId(roleId);
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new NoSuchElementException("Role not found with ID: " + roleId);
        }
        // Consider adding logic to prevent deleting default or system roles
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            throw new NoSuchElementException("Role not found with name: " + name);
        }
        return role;
    }
}