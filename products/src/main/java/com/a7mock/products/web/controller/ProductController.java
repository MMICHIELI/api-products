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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/products")
@CrossOrigin(origins = "http://localhost:4200")
@Api (description="REST Microservice of Products for A7 Crud UI.", value = "/products")
public class ProductController {

  @Autowired
  IProductDao productDao;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);


  // Product List
  @GetMapping
  @ApiOperation(value = "List all Products", response = Product.class, responseContainer = "List")
  public List<Product> getProducts() {

    LOGGER.info("PRODUCT [CONTROLLER] - GET all Products");
    List<Product> products = productDao.findAll();

    if(products.isEmpty()) throw new ProductNotFoundException("No Product !");

    return products;
  }

  // Get Product by id
  @GetMapping(value = "/{productId}")
  @ApiOperation(value = "Get a Product by id", response = Product.class, responseContainer = "ResponseEntity")
  public Optional<Product> getById(
      @PathVariable("productId") @ApiParam(value = "Product Id", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - GET Product by id: " + productId);
    Optional<Product> product = productDao.findById(productId);

    if(!product.isPresent()) throw new ProductNotFoundException("Product with id " + productId + " not found");

    return product;
  }

  @PostMapping(consumes = "application/json")
  @ApiOperation(value = "Create a new Product", response = Product.class, responseContainer = "ResponseEntity")
  public ResponseEntity<Product> addProduct(
      @Valid @RequestBody @ApiParam(value = "Product data", required = true) Product product
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - CREATE a new Product = {name: " + product.getProdName()
        + ", desc: " + product.getProdDesc()
        + ", price: " + product.getProdPrice() + " }");

    Product productAdded = productDao.save(product);
    if (productAdded == null) return ResponseEntity.noContent().build();

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id]")
        .buildAndExpand(productAdded.getId())
        .toUri();

    return new ResponseEntity<>(productAdded, CREATED);
  }

  @PutMapping(value = "/{productId}", consumes = "application/json")
  @ApiOperation(value = "Update Product", response = Product.class, responseContainer = "ResponseEntity")
  public ResponseEntity<Product> updateProduit(
      @Valid @RequestBody @ApiParam(value = "Product data to update", required = true) Product product,
      @PathVariable("productId") @ApiParam(value = "product id", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - UPDATE Product by id: " + productId);

    Product productUpdated = productDao.save(product);

    return new ResponseEntity<>(productUpdated, OK);
  }

  @DeleteMapping(value = "/{productId}")
  @ApiOperation(value = "Delete a Product")
  public void supprimerProduit(
      @PathVariable("productId") @ApiParam(value = "Id of the Product to Delete", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - DELETE Product by id: " + productId);

    productDao.deleteById(productId);
  }

}
