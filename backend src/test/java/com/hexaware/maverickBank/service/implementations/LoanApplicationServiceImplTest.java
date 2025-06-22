package com.hexaware.maverickbank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hexaware.maverickbank.dto.CustomerDTO;
import com.hexaware.maverickbank.dto.LoanApplicationCreateRequestDTO;
import com.hexaware.maverickbank.dto.LoanApplicationDTO;
import com.hexaware.maverickbank.dto.LoanApplicationUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.Customer;
import com.hexaware.maverickbank.dto.entity.Loan;
import com.hexaware.maverickbank.dto.entity.LoanApplication;
import com.hexaware.maverickbank.repository.ILoanApplicationRepository;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceImplTest {

    @Mock
    private ILoanApplicationRepository loanApplicationRepository;

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private LoanServiceImpl loanService;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    private LoanApplication loanApp;
    private Customer customer;
    private Loan loan;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setCustomerId(1L);

        loan = new Loan();
        loan.setLoanId(101L);

        loanApp = new LoanApplication();
        loanApp.setApplicationId(1L);
        loanApp.setCustomer(customer);
        loanApp.setLoan(loan);
        loanApp.setRequestedAmount(BigDecimal.valueOf(50000));
        loanApp.setPurpose("Education");
        loanApp.setApplicationDate(LocalDateTime.now());
        loanApp.setStatus("Pending");
    }

    @Test
    void testCreateLoanApplication() {
        LoanApplicationCreateRequestDTO request = new LoanApplicationCreateRequestDTO();
        request.setCustomerId(1L);
        request.setLoanId(101L);
        request.setRequestedAmount(BigDecimal.valueOf(50000));
        request.setPurpose("Education");

        when(customerService.getCustomerById(1L)).thenReturn(null);
        when(loanService.getLoanById(101L)).thenReturn(null);
        when(loanApplicationRepository.save(any())).thenReturn(loanApp);

        LoanApplicationDTO result = loanApplicationService.createLoanApplication(request);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertEquals(101L, result.getLoanId());
        assertEquals("Education", result.getPurpose());
    }

    @Test
    void testGetLoanApplicationById() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(loanApp));
        LoanApplicationDTO result = loanApplicationService.getLoanApplicationById(1L);
        assertNotNull(result);
        assertEquals("Education", result.getPurpose());
    }

    @Test
    void testGetAllLoanApplications() {
        when(loanApplicationRepository.findAll()).thenReturn(List.of(loanApp));
        List<LoanApplicationDTO> results = loanApplicationService.getAllLoanApplications();
        assertEquals(1, results.size());
        assertEquals("Education", results.get(0).getPurpose());
    }

    @Test
    void testUpdateLoanApplication() {
        Long applicationId = 1L;

        LoanApplication existingApplication = new LoanApplication();
        existingApplication.setApplicationId(applicationId);
        existingApplication.setRequestedAmount(BigDecimal.valueOf(20000));
        existingApplication.setPurpose("Medical"); // original
        existingApplication.setStatus("Pending");

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        existingApplication.setCustomer(customer);

        Loan loan = new Loan();
        loan.setLoanId(101L);
        existingApplication.setLoan(loan);

        LoanApplicationUpdateRequestDTO updateRequest = new LoanApplicationUpdateRequestDTO();
        updateRequest.setRequestedAmount(BigDecimal.valueOf(25000));
        updateRequest.setPurpose("Education"); // updated
        updateRequest.setStatus("Approved");

        when(loanApplicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));

        // Simulate that the repository saves the updated object
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication arg = invocation.getArgument(0);
            return arg;
        });

        LoanApplicationDTO updatedLoanAppDTO = loanApplicationService.updateLoanApplication(applicationId, updateRequest);

        assertNotNull(updatedLoanAppDTO);
        assertEquals(applicationId, updatedLoanAppDTO.getApplicationId());
        assertEquals(BigDecimal.valueOf(25000), updatedLoanAppDTO.getRequestedAmount());
        assertEquals("Education", updatedLoanAppDTO.getPurpose()); // updated
        assertEquals("Approved", updatedLoanAppDTO.getStatus());   // updated
    }


    @Test
    void testDeleteLoanApplication() {
        when(loanApplicationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(loanApplicationRepository).deleteById(1L);
        assertDoesNotThrow(() -> loanApplicationService.deleteLoanApplication(1L));
    }

    @Test
    void testGetLoanApplicationsByCustomerId() {
        Long customerId = 1L;

        // Mock the returned CustomerDTO from the service
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerId);
        when(customerService.getCustomerById(customerId)).thenReturn(customerDTO);

        // Mock the LoanApplication entity
        LoanApplication loanApp = new LoanApplication();
        loanApp.setApplicationId(1L);
        loanApp.setRequestedAmount(BigDecimal.valueOf(30000));
        loanApp.setPurpose("Business");
        loanApp.setStatus("Pending");
        loanApp.setApplicationDate(LocalDateTime.now());

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        loanApp.setCustomer(customer);

        Loan loan = new Loan();
        loan.setLoanId(101L);
        loanApp.setLoan(loan);

        List<LoanApplication> loanApplications = List.of(loanApp);
        when(loanApplicationRepository.findByCustomer_CustomerId(customerId)).thenReturn(loanApplications);

        List<LoanApplicationDTO> result = loanApplicationService.getLoanApplicationsByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());
        assertEquals("Business", result.get(0).getPurpose());
    }

}
