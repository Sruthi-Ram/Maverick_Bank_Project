package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.RoleDTO;
import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.service.interfaces.RoleService;

import jakarta.validation.ValidationException;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = convertDTOtoEntity(roleDTO);
        if (roleRepository.findByName(role.getName()) != null) {
            throw new ValidationException("Role with name " + role.getName() + " already exists");
        }
        Role savedRole = roleRepository.save(role);
        return convertEntityToDTO(savedRole);
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + roleId));
        return convertEntityToDTO(role);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + roleId));
        Role updatedRole = convertDTOtoEntity(roleDTO);
        if (!existingRole.getName().equals(updatedRole.getName()) && roleRepository.findByName(updatedRole.getName()) != null) {
            throw new ValidationException("Role with name " + updatedRole.getName() + " already exists");
        }
        updatedRole.setRoleId(roleId);
        Role savedRole = roleRepository.save(updatedRole);
        return convertEntityToDTO(savedRole);
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
    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            throw new NoSuchElementException("Role not found with name: " + name);
        }
        return convertEntityToDTO(role);
    }

    private RoleDTO convertEntityToDTO(Role role) {
        return new RoleDTO(role.getRoleId(), role.getName());
    }

    private Role convertDTOtoEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setRoleId(roleDTO.getRoleId()); // Only if you want to set the ID, usually it's auto-generated
        return role;
    }
}