package com.a7mock.products.web.controller;

import com.a7mock.products.dao.IProductDao;
import com.a7mock.products.model.Product;
import com.a7mock.products.web.exception.ProductNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin( origins = "http://localhost:4200" )
@Api ( description="REST Microservice of Products for A7 Crud UI.")
public class ProductController {

  @Autowired
  IProductDao productDao;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);


  // Product List
  @GetMapping(value = "/products")
  public List<Product> getProducts() {

    List<Product> products = productDao.findAll();

    if(products.isEmpty()) throw new ProductNotFoundException("No Product !");

    return products;
  }

  // Get Product by id
  @GetMapping(value = "/products/{id}")
  public Optional<Product> getById(@PathVariable Long id) {

    Optional<Product> product = productDao.findById(id);

    if(!product.isPresent()) throw new ProductNotFoundException("Product with id " + id + " not found");

    return product;
  }

  @PostMapping(value = "/products", consumes = "application/json")
  public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
    Product productAdded = productDao.save(product);
    if (productAdded == null) return ResponseEntity.noContent().build();

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id]")
        .buildAndExpand(productAdded.getId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @PutMapping(value = "/products/{id}", consumes = "application/json")
  @ApiOperation(value = "Update Product", response = Product.class, responseContainer = "ResponseEntity")
  public ResponseEntity<Product> updateProduit(
      @Valid @RequestBody @ApiParam(value = "product informations to update", required = true) Product product,
      @PathVariable("id") @ApiParam(value = "product id", required = true) Long id
  ) {
    LOGGER.info("UPDATE (PUT) an existing product: " + id);
    Product productUpdated = productDao.save(product);
    return new ResponseEntity<>(productUpdated, HttpStatus.OK);
  }

  @DeleteMapping(value = "/products/{id}")
  public void supprimerProduit(@PathVariable Long id) {
    productDao.deleteById(id);
  }

}
