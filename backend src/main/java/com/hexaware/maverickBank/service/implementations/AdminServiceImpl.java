/**
 * -----------------------------------------------------------------------------
 * Author      : Sruthi Ramesh
 * Date        : May 22, 2025
 * Description : This class implements the AdminService interface and manages
 *               the business logic related to user and bank employee management, including:
 * 
 *               User Management:
 *               - Creating users with encoded passwords and assigned roles
 *               - Retrieving users by ID
 *               - Listing all users
 *               - Updating user details including email, password, and role
 *               - Deleting users by ID
 * 
 *               Bank Employee Management:
 *               - Creating bank employees linked to users
 *               - Retrieving bank employees by employee ID
 *               - Listing all bank employees
 *               - Updating bank employee details
 *               - Deleting bank employees by ID
 * 
 *               The class performs necessary validations such as role and user existence,
 *               handles entity-to-DTO and DTO-to-entity conversions,
 *               and logs key actions for traceability.
 * -----------------------------------------------------------------------------
 */


package com.hexaware.maverickbank.service.implementations;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaware.maverickbank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankEmployeeDTO;
import com.hexaware.maverickbank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickbank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.BankEmployee;
import com.hexaware.maverickbank.dto.entity.User;
import com.hexaware.maverickbank.repository.IBankEmployeeRepository;
import com.hexaware.maverickbank.repository.IRoleRepository;
import com.hexaware.maverickbank.repository.IUserRepository;
import com.hexaware.maverickbank.service.interfaces.AdminService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private IBankEmployeeRepository bankEmployeeRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// User Management

	@Override
	public UserDTO createUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
		log.info("Creating user with username: {}", userRegistrationRequestDTO.getUsername());
		User user = new User();
		// Copy fields from DTO to User entity
		BeanUtils.copyProperties(userRegistrationRequestDTO, user);
		// Encode password before saving
		user.setPassword(passwordEncoder.encode(userRegistrationRequestDTO.getPassword()));

		// Set the role to the user
		roleRepository.findById(userRegistrationRequestDTO.getRoleId()).ifPresentOrElse(user::setRole, () -> {
			throw new RuntimeException("Role not found with ID: " + userRegistrationRequestDTO.getRoleId());
		});

		User savedUser = userRepository.save(user);
		UserDTO userDTO = new UserDTO();
		// Copy back properties from saved user to DTO
		BeanUtils.copyProperties(savedUser, userDTO);
		if (savedUser.getRole() != null) {
			userDTO.setRoleId(savedUser.getRole().getRoleId());
		}
		log.info("User created successfully with ID: {}", userDTO.getUserId());
		return userDTO;
	}

	@Override
	public UserDTO getUserById(Long userId) {
		log.info("Fetching user by ID: {}", userId);
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			UserDTO userDTO = new UserDTO();
			// Convert entity to DTO
			BeanUtils.copyProperties(userOptional.get(), userDTO);
			if (userOptional.get().getRole() != null) {
				userDTO.setRoleId(userOptional.get().getRole().getRoleId());
			}
			log.info("User found with ID: {}", userId);
			return userDTO;
		} else {
			log.warn("User not found with ID: {}", userId);
			return null;
		}
	}

	@Override
	public List<UserDTO> getAllUsers() {
		log.info("Fetching all users");
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO userDTO = new UserDTO();
			// Convert each User entity to DTO
			BeanUtils.copyProperties(user, userDTO);
			if (user.getRole() != null) {
				userDTO.setRoleId(user.getRole().getRoleId());
			}
			return userDTO;
		}).collect(Collectors.toList());
		log.info("Fetched {} users", userDTOs.size());
		return userDTOs;
	}

	@Override
	public UserDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO) {
		log.info("Updating user with ID: {} and data: {}", userId, userUpdateRequestDTO);
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			// Update email
			user.setEmail(userUpdateRequestDTO.getEmail());

			// Update password if provided
			if (userUpdateRequestDTO.getPassword() != null) {
				user.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getPassword()));
			}

			// Update role if provided
			if (userUpdateRequestDTO.getRoleId() != null) {
				roleRepository.findById(userUpdateRequestDTO.getRoleId()).ifPresentOrElse(user::setRole, () -> {
					log.warn("Role not found with ID: {}", userUpdateRequestDTO.getRoleId());
				});
			}

			user.setUserId(userId);
			User updatedUser = userRepository.save(user);
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(updatedUser, userDTO);
			if (updatedUser.getRole() != null) {
				userDTO.setRoleId(updatedUser.getRole().getRoleId());
			}
			log.info("User with ID {} updated successfully", userId);
			return userDTO;
		} else {
			log.warn("User not found with ID: {}", userId);
			return null;
		}
	}

	@Override
	public void deleteUser(Long userId) {
		log.info("Deleting user with ID: {}", userId);
		userRepository.deleteById(userId);
		log.info("User with ID {} deleted successfully", userId);
	}

	// Bank Employee Management

	@Override
	public BankEmployeeDTO createBankEmployee(BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO) {
		log.info("Creating bank employee with user ID: {}", bankEmployeeCreateRequestDTO.getUserId());
		BankEmployee bankEmployee = new BankEmployee();
		// Copy request DTO to entity
		BeanUtils.copyProperties(bankEmployeeCreateRequestDTO, bankEmployee);

		// Set User object using userId
		Optional<User> userOptional = userRepository.findById(bankEmployeeCreateRequestDTO.getUserId());
		userOptional.ifPresentOrElse(bankEmployee::setUserId, () -> {
			throw new RuntimeException("User not found with ID: " + bankEmployeeCreateRequestDTO.getUserId());
		});

		BankEmployee savedEmployee = bankEmployeeRepository.save(bankEmployee);
		BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
		BeanUtils.copyProperties(savedEmployee, bankEmployeeDTO);

		// Set userId in DTO manually
		if (savedEmployee.getUserId() != null) {
			bankEmployeeDTO.setUserId(savedEmployee.getUserId().getUserId());
		}

		log.info("Bank employee created successfully with ID: {}", bankEmployeeDTO.getEmployeeId());
		return bankEmployeeDTO;
	}

	@Override
	public BankEmployeeDTO getBankEmployeeById(Long employeeId) {
		log.info("Fetching bank employee by ID: {}", employeeId);
		Optional<BankEmployee> bankEmployeeOptional = bankEmployeeRepository.findById(employeeId);
		if (bankEmployeeOptional.isPresent()) {
			BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
			BeanUtils.copyProperties(bankEmployeeOptional.get(), bankEmployeeDTO);
			if (bankEmployeeOptional.get().getUserId() != null) {
				bankEmployeeDTO.setUserId(bankEmployeeOptional.get().getUserId().getUserId());
			}
			log.info("Bank employee found with ID: {}", employeeId);
			return bankEmployeeDTO;
		} else {
			log.warn("Bank employee not found with ID: {}", employeeId);
			return null; // Or throw an exception
		}
	}

	@Override
	public List<BankEmployeeDTO> getAllBankEmployees() {
		log.info("Fetching all bank employees");
		List<BankEmployee> bankEmployees = bankEmployeeRepository.findAll();
		List<BankEmployeeDTO> bankEmployeeDTOs = bankEmployees.stream().map(bankEmployee -> {
			BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
			BeanUtils.copyProperties(bankEmployee, bankEmployeeDTO);
			if (bankEmployee.getUserId() != null) {
				bankEmployeeDTO.setUserId(bankEmployee.getUserId().getUserId());
			}
			return bankEmployeeDTO;
		}).collect(Collectors.toList());
		log.info("Fetched {} bank employees", bankEmployeeDTOs.size());
		return bankEmployeeDTOs;
	}

	@Override
	public BankEmployeeDTO updateBankEmployee(Long employeeId,
			BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO) {
		log.info("Updating bank employee with ID: {} and data: {}", employeeId, bankEmployeeUpdateRequestDTO);
		Optional<BankEmployee> bankEmployeeOptional = bankEmployeeRepository.findById(employeeId);
		if (bankEmployeeOptional.isPresent()) {
			BankEmployee bankEmployee = bankEmployeeOptional.get();
			// Copy updates from DTO
			BeanUtils.copyProperties(bankEmployeeUpdateRequestDTO, bankEmployee);
			bankEmployee.setEmployeeId(employeeId);
			BankEmployee updatedEmployee = bankEmployeeRepository.save(bankEmployee);
			BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
			BeanUtils.copyProperties(updatedEmployee, bankEmployeeDTO);

			// Set branch and user IDs
			if (updatedEmployee.getBranch() != null) {
				bankEmployeeDTO.setBranchId(updatedEmployee.getBranch().getBranchId());
			}
			if (updatedEmployee.getUserId() != null) {
				bankEmployeeDTO.setUserId(updatedEmployee.getUserId().getUserId());
			}
			log.info("Bank employee with ID {} updated successfully", employeeId);
			return bankEmployeeDTO;
		} else {
			log.warn("Bank employee not found with ID: {}", employeeId);
			return null;
		}
	}

	@Override
	public void deleteBankEmployee(Long employeeId) {
		log.info("Deleting bank employee with ID: {}", employeeId);
		bankEmployeeRepository.deleteById(employeeId);
		log.info("Bank employee with ID {} deleted successfully", employeeId);
	}
}
