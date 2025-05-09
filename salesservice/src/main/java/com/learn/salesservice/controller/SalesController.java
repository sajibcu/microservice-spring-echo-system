package com.learn.salesservice.controller;

import com.learn.salesservice.dto.ProductDto;
import com.learn.salesservice.dto.SalesDto;
import com.learn.salesservice.model.Sales;
import com.learn.salesservice.service.SalesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
public class SalesController {

    private  final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    // Add CRUD operations for sales
    // Create a new sales record
    @PostMapping
    public Sales createSales(@RequestBody Sales sales) {
        return  salesService.createSales(sales);
    }

    @GetMapping
    public List<SalesDto> getAllSales() {
        return salesService.getAllSales();
    }

    @GetMapping("/{id}")
    public SalesDto getSalesById(@PathVariable Long id) {
        return salesService.getSalesById(id);
    }
}
