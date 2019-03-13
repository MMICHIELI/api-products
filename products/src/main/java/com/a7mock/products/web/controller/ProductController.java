package com.a7mock.products.web.controller;

import com.a7mock.products.dao.IProductDao;
import com.a7mock.products.model.Product;
import com.a7mock.products.web.exception.ProductNotFoundException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Api ( description="REST Microservice of Products for A7 Crud UI.")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

  @Autowired
  IProductDao productDao;

  // Product List
  @GetMapping(value = "/products")
  public List<Product> getProducts() {

    List<Product> products = productDao.findAll();

    if(products.isEmpty()) throw new ProductNotFoundException("No Product !");

    return products;
  }

  // Get Product by id
  @GetMapping(value = "/products/{id}")
  public Optional<Product> getById(@PathVariable int id) {

    Optional<Product> product = productDao.findById(id);

    if(!product.isPresent()) throw new ProductNotFoundException("Product with id " + id + " not found");

    return product;
  }

  @PostMapping(value = "/products")
  public ResponseEntity<Void> addProduct(@Valid @RequestBody Product product) {
    Product productAdded = productDao.save(product);
    if (productAdded == null) return ResponseEntity.noContent().build();

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id]")
        .buildAndExpand(productAdded.getId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @PutMapping(value = "/products")
  public void updateProduit(@RequestBody Product product) {
    productDao.save(product);
  }

  @DeleteMapping(value = "/products/{id}")
  public void supprimerProduit(@PathVariable int id) {
    productDao.deleteById(id);
  }

}
