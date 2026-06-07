package com.bank.customer.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String documentNumber;
    private String fullName;
    private CustomerType customerType;
    private String email;
    private String phone;
    private CustomerProfile customerProfile;
    private String address;
    private Boolean active;
}