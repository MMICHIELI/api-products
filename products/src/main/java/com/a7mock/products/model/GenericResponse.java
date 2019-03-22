package com.a7mock.products.model;

import lombok.Data;

/**
 * Response Model
 * @param <T>
 */
@Data
public class GenericResponse<T> {

  private String state;
  private String message;
  private T data;

  public GenericResponse() {
    state = "OK";
    message = "SUCCESS";
  }
}
