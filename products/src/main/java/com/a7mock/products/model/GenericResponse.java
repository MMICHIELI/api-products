package com.a7mock.products.model;

import lombok.Data;

import static org.springframework.http.HttpStatus.OK;

/**
 * Response Model
 * @param <T>
 */
@Data
public class GenericResponse<T> {

  private String status;
  private String message;
  private T data;

  public GenericResponse() {
    this.status = OK.toString();
    this.message = "SUCCESS";
  }

  public GenericResponse(String status) {
    this.status = status;
    this.message = "SUCCESS";
  }
}
