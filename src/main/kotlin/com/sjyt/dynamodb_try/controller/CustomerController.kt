package com.sjyt.dynamodb_try.controller

import com.sjyt.dynamodb_try.dto.CustomerCreateDto
import com.sjyt.dynamodb_try.model.Customer
import com.sjyt.dynamodb_try.repository.CustomerRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerRepository: CustomerRepository,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody customerCreateDto: CustomerCreateDto): Customer {
        val customer = Customer.buildFrom(customerCreateDto)
        println(customerCreateDto)
        println(customer)
        try {
            return customerRepository.save(customer)
        } catch (error: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to save customer: ${error.message}")
        }
    }

    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: String): Customer {
        return customerRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found")
            }
    }

    @GetMapping
    fun getAllCustomers(): List<Customer> {
        return customerRepository.findAll()
    }

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable id: String, @RequestBody customerCreateDto: CustomerCreateDto): Customer {
        return customerRepository.findById(id)
            .map { existingCustomer ->
                existingCustomer.name = customerCreateDto.name
                existingCustomer.email = customerCreateDto.email
                customerRepository.save(existingCustomer)
            }
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found")
            }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: String) {
        return customerRepository.findById(id)
            .map { customerRepository.delete(id) }
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found")
            }
    }
}