package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickBank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IBankEmployeeRepository;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.AdminService;

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
		BeanUtils.copyProperties(userRegistrationRequestDTO, user);
		user.setPassword(passwordEncoder.encode(userRegistrationRequestDTO.getPassword()));

		// Fetch the Role entity based on the roleId from the request
		roleRepository.findById(userRegistrationRequestDTO.getRoleId()).ifPresentOrElse(user::setRole, () -> {
			throw new RuntimeException("Role not found with ID: " + userRegistrationRequestDTO.getRoleId());
		});

		User savedUser = userRepository.save(user);
		UserDTO userDTO = new UserDTO();
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
			user.setEmail(userUpdateRequestDTO.getEmail());

			if (userUpdateRequestDTO.getPassword() != null) {
				user.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getPassword()));
			}

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
		BeanUtils.copyProperties(bankEmployeeCreateRequestDTO, bankEmployee);

		// Fetch the User entity based on the userId from the request
		Optional<User> userOptional = userRepository.findById(bankEmployeeCreateRequestDTO.getUserId());
		userOptional.ifPresentOrElse(bankEmployee::setUserId, () -> {
			throw new RuntimeException("User not found with ID: " + bankEmployeeCreateRequestDTO.getUserId());
		});

		BankEmployee savedEmployee = bankEmployeeRepository.save(bankEmployee);
		BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
		BeanUtils.copyProperties(savedEmployee, bankEmployeeDTO);

		// Manually set the userId in the DTO as BeanUtils might not copy it automatically from the User object
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
			BeanUtils.copyProperties(bankEmployeeUpdateRequestDTO, bankEmployee);
			bankEmployee.setEmployeeId(employeeId);
			BankEmployee updatedEmployee = bankEmployeeRepository.save(bankEmployee);
			BankEmployeeDTO bankEmployeeDTO = new BankEmployeeDTO();
			BeanUtils.copyProperties(updatedEmployee, bankEmployeeDTO);

			// Manually set the branchId in the DTO
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