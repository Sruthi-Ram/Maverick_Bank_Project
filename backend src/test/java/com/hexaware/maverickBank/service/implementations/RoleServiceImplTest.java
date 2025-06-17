package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.RoleDTO;
import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.repository.IRoleRepository;

import jakarta.validation.ValidationException;

class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private IRoleRepository roleRepository;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setRoleId(1L);
        role.setName("CUSTOMER");
        roleDTO = new RoleDTO(1L, "CUSTOMER");
    }

    @Test
    void testCreateRole() {
        when(roleRepository.findByName("CUSTOMER")).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleDTO createdRoleDTO = roleService.createRole(roleDTO);
        assertNotNull(createdRoleDTO);
        assertEquals(roleDTO.getName(), createdRoleDTO.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testCreateRole_ExistingName() {
        when(roleRepository.findByName("CUSTOMER")).thenReturn(role);
        assertThrows(ValidationException.class, () -> roleService.createRole(roleDTO));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testGetRoleById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        RoleDTO foundRoleDTO = roleService.getRoleById(1L);
        assertNotNull(foundRoleDTO);
        assertEquals(roleDTO.getRoleId(), foundRoleDTO.getRoleId());
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleById(1L));
    }

    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));
        List<RoleDTO> allRolesDTO = roleService.getAllRoles();
        assertNotNull(allRolesDTO);
        assertEquals(1, allRolesDTO.size());
        assertEquals(roleDTO.getRoleId(), allRolesDTO.get(0).getRoleId());
    }

    @Test
    void testUpdateRole() {
        RoleDTO updateDTO = new RoleDTO(1L, "ADMIN");
        Role updatedRole = new Role();
        updatedRole.setRoleId(1L);
        updatedRole.setName("ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.findByName("ADMIN")).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        RoleDTO updatedRoleDTO = roleService.updateRole(1L, updateDTO);
        assertNotNull(updatedRoleDTO);
        assertEquals("ADMIN", updatedRoleDTO.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testUpdateRole_NotFound() {
        RoleDTO updateDTO = new RoleDTO(1L, "ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.updateRole(1L, updateDTO));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_ExistingName() {
        RoleDTO updateDTO = new RoleDTO(1L, "ADMIN");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        Role existingAdminRole = new Role();
        existingAdminRole.setRoleId(2L);
        existingAdminRole.setName("ADMIN");
        when(roleRepository.findByName("ADMIN")).thenReturn(existingAdminRole);
        assertThrows(ValidationException.class, () -> roleService.updateRole(1L, updateDTO));
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testDeleteRole() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        roleService.deleteRole(1L);
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> roleService.deleteRole(1L));
        verify(roleRepository, never()).deleteById(any());
    }

    @Test
    void testGetRoleByName() {
        when(roleRepository.findByName("CUSTOMER")).thenReturn(role);
        RoleDTO foundRoleDTO = roleService.getRoleByName("CUSTOMER");
        assertNotNull(foundRoleDTO);
        assertEquals(roleDTO.getRoleId(), foundRoleDTO.getRoleId());
    }

    @Test
    void testGetRoleByName_NotFound() {
        when(roleRepository.findByName("ADMIN")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName("ADMIN"));
    }
}