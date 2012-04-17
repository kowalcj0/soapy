package com.yelllabs.soapy.enums;

/**
* @brief ENUM type to store number of loops
* @author Janusz Kowalczyk
* @created 2012-03-12
*/
enum Loops {

  TIMES;

  private int loops;

  private Loops() {
  }

  private Loops( int value ) {
    this.loops = value;
  }

  public Loops set( int value ) {
    this.loops = value;
    return this;
  }

  public int getValue() {
    return this.loops;
  }
}
