package com.ravichanderjha.jpa_entity_version;

import com.ravichanderjha.jpa_entity_version.entity.Customer;
import com.ravichanderjha.jpa_entity_version.entity.Product;
import com.ravichanderjha.jpa_entity_version.repository.CustomerRepository;
import com.ravichanderjha.jpa_entity_version.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JpaEntityVersionApplicationTests {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

@Test
public void testOptimisticLockingWithVersion() {
    // Create and save a Product
    Product product = new Product();
    product.setName("Laptop");
    product.setPrice(1000.0);
    product = productRepository.save(product);

    // Simulate User A fetching the product
    Product userAProduct = productRepository.findById(product.getId()).orElseThrow();

    // Simulate User B fetching the product
	Product userBProduct = productRepository.findById(product.getId()).orElseThrow();

    // User A updates the product price
    userAProduct.setPrice(1100.0);
    productRepository.save(userAProduct);
	Product userCProduct = productRepository.findById(product.getId()).orElseThrow();

    // User B tries to update the product price
    userBProduct.setPrice(900.0);

    // Expect OptimisticLockException when User B tries to save
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
        productRepository.save(userBProduct);
    });
}
	@Test
	public void testNoOptimisticLockingWithoutVersion() {
		// Create and save a Customer
		Customer customer = new Customer();
		customer.setName("John Doe");
		customer.setEmail("john.doe@example.com");
		customer = customerRepository.save(customer);

		// Simulate User A fetching the customer
		Customer userACustomer = customerRepository.findById(customer.getId()).orElseThrow();

		// Simulate User B fetching the customer
		Customer userBCustomer = customerRepository.findById(customer.getId()).orElseThrow();

		// User A updates the customer email
		userACustomer.setEmail("john.updated@example.com");
		customerRepository.save(userACustomer);

		// User B updates the customer email
		userBCustomer.setEmail("john.overwritten@example.com");
		customerRepository.save(userBCustomer);

		// Verify that the last update overwrites the previous one
		Customer updatedCustomer = customerRepository.findById(customer.getId()).orElseThrow();
		assertEquals("john.overwritten@example.com", updatedCustomer.getEmail());
	}
}
