package com.a7mock.products.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Product {

  @Id
  @GeneratedValue
  private Long id;

  private String prodDesc;

  private String prodName;

  private Double prodPrice;

  public Product() {
  }

  public Product(String prodName, String prodDesc, Double prodPrice) {
    this.prodName = prodName;
    this.prodDesc = prodDesc;
    this.prodPrice = prodPrice;
  }

  @Override
  public String toString() {
    return "Product{" +
        "id=" + id +
        ", prodName='" + prodName + '\'' +
        ", prodDesc='" + prodDesc + '\'' +
        ", prodPrice=" + prodPrice +
        '}';
  }
}
