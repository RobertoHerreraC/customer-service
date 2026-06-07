package com.bank.customer.service.imp;

import com.bank.customer.api.dto.CustomerRequest;
import com.bank.customer.api.dto.CustomerResponse;
import com.bank.customer.domain.Customer;
import com.bank.customer.domain.CustomerProfile;
import com.bank.customer.domain.CustomerType;
import com.bank.customer.exception.CustomerAlreadyExistsException;
import com.bank.customer.exception.CustomerBusinessException;
import com.bank.customer.exception.CustomerNotFoundException;
import com.bank.customer.mapper.CustomerMapper;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.strategy.BusinessCustomerProfileValidationStrategy;
import com.bank.customer.strategy.CustomerProfileValidationContext;
import com.bank.customer.strategy.PersonalCustomerProfileValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper;
    private CustomerProfileValidationContext profileValidationContext;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerMapper = new CustomerMapper();

        profileValidationContext = new CustomerProfileValidationContext(
                List.of(
                        new PersonalCustomerProfileValidationStrategy(),
                        new BusinessCustomerProfileValidationStrategy()
                )
        );

        customerService = new CustomerServiceImpl(
                customerRepository,
                customerMapper,
                profileValidationContext
        );
    }

    @Test
    void shouldCreatePersonalVipCustomer() {
        CustomerRequest request = buildRequest("12345678", "Luis Torres",
                "PERSONAL", "VIP", "victor@mail.com","132465789", "Lima, Lima");

        Customer savedCustomer = Customer.builder()
                .id("customer-id")
                .documentNumber("12345678")
                .fullName("Luis Torres")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.VIP)
                .email("victor@mail.com")
                .phone("132465789")
                .address("Lima, Lima")
                .build();

        when(customerRepository.existsByDocumentNumber("12345678"))
                .thenReturn(Mono.just(false));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.just(savedCustomer));

        CustomerResponse response = customerService.create(request).blockingGet();

        assertEquals("customer-id", response.getId());
        assertEquals("12345678", response.getDocumentNumber());
        assertEquals("Luis Torres", response.getFullName());
        assertEquals("PERSONAL", response.getCustomerType());
        assertEquals("VIP", response.getProfile());
        assertEquals("132465789", response.getPhone());
        assertEquals("Lima, Lima", response.getAddress());
        assertEquals("victor@mail.com", response.getEmail());
    }

    @Test
    void shouldCreateBusinessPymeCustomer() {
        CustomerRequest request = buildRequest("20600111222", "Empresa SAC",
                "BUSINESS", "PYME", "test@mail.com", "123456789", "Lima, Lima");

        Customer savedCustomer = Customer.builder()
                .id("customer-id")
                .documentNumber("20600111222")
                .fullName("Empresa SAC")
                .customerType(CustomerType.BUSINESS)
                .customerProfile(CustomerProfile.PYME)
                .email("test@mail.com")
                .phone("123456789")
                .address("Lima, Lima")
                .build();

        when(customerRepository.existsByDocumentNumber("20600111222"))
                .thenReturn(Mono.just(false));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.just(savedCustomer));

        CustomerResponse response = customerService.create(request).blockingGet();

        assertEquals("BUSINESS", response.getCustomerType());
        assertEquals("PYME", response.getProfile());
    }

    @Test
    void shouldCreateCustomerWithStandardProfileWhenProfileIsNull() {
        CustomerRequest request = buildRequest("11223344", "Ana Lopez",
                "PERSONAL", null, "test@mail.com", "123456789", "Lima, Lima");

        Customer savedCustomer = Customer.builder()
                .id("customer-id")
                .documentNumber("11223344")
                .fullName("Ana Lopez")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.STANDARD)
                .email("test@mail.com")
                .phone("123456789")
                .address("Lima, Lima")
                .build();

        when(customerRepository.existsByDocumentNumber("11223344"))
                .thenReturn(Mono.just(false));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.just(savedCustomer));

        CustomerResponse response = customerService.create(request).blockingGet();

        assertEquals("STANDARD", response.getProfile());
    }

    @Test
    void shouldThrowWhenCreatePersonalWithPymeProfile() {
        CustomerRequest request = buildRequest("87654321", "Carlos Ramos",
                "PERSONAL", "PYME", "test@mail.com", "123456789", "Lima, Lima");

        assertThrows(CustomerBusinessException.class, () ->
                customerService.create(request).blockingGet());
    }

    @Test
    void shouldThrowWhenCreateBusinessWithVipProfile() {
        CustomerRequest request = buildRequest("20600999888", "Empresa Norte",
                "BUSINESS", "VIP", "test@mail.com", "123456789", "Lima, Lima");

        assertThrows(CustomerBusinessException.class, () ->
                customerService.create(request).blockingGet());
    }

    @Test
    void shouldThrowWhenCustomerAlreadyExists() {
        CustomerRequest request = buildRequest("12345678", "Luis Torres",
                "PERSONAL", "VIP", "test@mail.com", "123456789", "Lima, Lima");

        when(customerRepository.existsByDocumentNumber("12345678"))
                .thenReturn(Mono.just(true));

        assertThrows(CustomerAlreadyExistsException.class, () ->
                customerService.create(request).blockingGet());
    }

    @Test
    void shouldUpdatePersonalToVip() {//
        String id = "customer-id";
        String documentNumber = "12345678";

        Customer existingCustomer = Customer.builder()
                .id(id)
                .documentNumber("12345678")
                .fullName("Luis Torres")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.STANDARD)
                .build();

        Customer updatedCustomer = Customer.builder()
                .id(id)
                .documentNumber("12345678")
                .fullName("Luis Torres Updated")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.VIP)
                .build();

        CustomerRequest request = buildRequest(
                "12345678",
                "Luis Torres Updated",
                "PERSONAL",
                "VIP",
                "test@mail.com",
                "132465789",
                "Lima, Lima"
        );
        when(customerRepository.findById(id))
                .thenReturn(Mono.just(existingCustomer));

        when(customerRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(existingCustomer));

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.just(updatedCustomer));

        CustomerResponse response = customerService.update(id, request).blockingGet();

        assertEquals("Luis Torres Updated", response.getFullName());
        assertEquals("VIP", response.getProfile());
    }

    @Test
    void shouldThrowWhenUpdateCustomerNotFound() {
        String id = "customer-id";
        CustomerRequest request = buildRequest("12345678", "Luis Torres",
                "PERSONAL", "VIP", "test@mail.com", "123456789", "Lima, Lima");

        when(customerRepository.findById(id))
                .thenReturn(Mono.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                customerService.update(id, request).blockingGet());
    }

    @Test
    void shouldThrowWhenUpdatePersonalToPyme() {
        String id = "customer-id";
        String documentNumber = "12345678";

        Customer existingCustomer = Customer.builder()
                .id(id)
                .documentNumber("12345678")
                .fullName("Luis Torres")
                .customerType(CustomerType.PERSONAL)
                .customerProfile(CustomerProfile.STANDARD)
                .email("test@mail.com")
                .phone("123456789")
                .address("Lima, Lima")
                .build();

        CustomerRequest request = buildRequest("12345678", "Luis Torres",
                "PERSONAL", "PYME", "test@mail.com", "123456789", "Lima, Lima");

        when(customerRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Mono.just(existingCustomer));

        when(customerRepository.findById(id))
                .thenReturn(Mono.just(existingCustomer));

        assertThrows(CustomerBusinessException.class, () ->
                customerService.update(id, request).blockingGet());
    }

    private CustomerRequest buildRequest(
            String documentNumber,
            String name,
            String customerType,
            String profile,
            String email,
            String phone,
            String address) {

        CustomerRequest request = new CustomerRequest();
        request.setDocumentNumber(documentNumber);
        request.setFullName(name);
        request.setCustomerType(customerType);
        request.setProfile(profile);
        request.setEmail(email);
        request.setPhone(phone);
        request.setAddress(address);
        return request;
    }
}
