package com.a7mock.products.web.controller;

import com.a7mock.products.dao.IProductDao;
import com.a7mock.products.model.GenericResponse;
import com.a7mock.products.model.Product;
import com.a7mock.products.web.exception.ProductNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

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
  @ApiOperation(value = "List all Products")
  public GenericResponse<Page<Product>> getProducts(
      @RequestParam(value = "size", defaultValue = "10") final int size,
      @RequestParam(value = "page", defaultValue = "0") final int page,
      @RequestParam(value = "sortProperty", defaultValue = "id") final String sortProperty,
      @RequestParam(value = "sortDirection", defaultValue = "asc") final String sortDirection
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - GET all Products");

    GenericResponse<Page<Product>> response = new GenericResponse<>();
    Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDirection), sortProperty);
    Pageable pageable = PageRequest.of(page, size, Sort.by(order));

    try {
      Page<Product> products = productDao.findAll(pageable);
      response.setData(products);
    } catch(Exception e) {
      e.printStackTrace();
      response.setStatus(INTERNAL_SERVER_ERROR.toString());
      response.setMessage(e.getMessage());
    }

    if(response.getData().getContent().isEmpty())
      throw new ProductNotFoundException("No Product !");

    return response;
  }

  // Get Product by id
  @GetMapping(value = "/{productId}")
  @ApiOperation(value = "Get a Product by id")
  public GenericResponse<Optional<Product>> getById(
      @PathVariable("productId") @ApiParam(value = "Product Id", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - GET Product by id: " + productId);

    GenericResponse<Optional<Product>> response = new GenericResponse<>();
    try {
      Optional<Product> product = productDao.findById(productId);
      response.setData(product);
    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus("KO");
      response.setMessage(e.getMessage());
    }

    if(!response.getData().isPresent())
      throw new ProductNotFoundException("Product with id " + productId + " not found");

    return response;
  }

  @PostMapping(consumes = "application/json")
  @ApiOperation(value = "Create a new Product")
  public GenericResponse<Product> addProduct(
      @Valid @RequestBody @ApiParam(value = "Product data", required = true) Product product
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - CREATE a new Product = {name: " + product.getProdName()
        + ", desc: " + product.getProdDesc()
        + ", price: " + product.getProdPrice() + " }");

    GenericResponse<Product> response = new GenericResponse<>(CREATED.toString());
    try {
      Product productAdded = productDao.save(product);
      response.setData(productAdded);
    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus(NO_CONTENT.toString());
      response.setMessage(e.getMessage());
    }


    if (response.getData() == null)
      response.setStatus(NO_CONTENT.toString());


    return response;
  }

  @PutMapping(value = "/{productId}", consumes = "application/json")
  @ApiOperation(value = "Update Product")
  public GenericResponse<Product> updateProduct(
      @Valid @RequestBody @ApiParam(value = "Product data to update", required = true) Product product,
      @PathVariable("productId") @ApiParam(value = "product id", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - UPDATE Product by id: " + productId);

    GenericResponse<Product> response = new GenericResponse<>();
    try {
      Product productUpdated = productDao.save(product);
      response.setData(productUpdated);
    } catch (Exception e) {
      response.setStatus(NO_CONTENT.toString());
      response.setMessage(e.getMessage());
    }

    if (response.getData() == null)
      response.setStatus(NO_CONTENT.toString());

    return response;
  }

  @DeleteMapping(value = "/{productId}")
  @ApiOperation(value = "Delete a Product", response = GenericResponse.class)
  public GenericResponse<Product> supprimerProduct(
      @PathVariable("productId") @ApiParam(value = "Id of the Product to Delete", required = true) Long productId
  ) {

    LOGGER.info("PRODUCT [CONTROLLER] - DELETE Product by id: " + productId);

    GenericResponse<Product> response = new GenericResponse<>();
    try {
      productDao.deleteById(productId);
    } catch (Exception e) {
      response.setStatus("KO");
      response.setMessage(e.getMessage());
    }

    return response;
  }
}
