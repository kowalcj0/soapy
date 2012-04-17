package com.yelllabs.soapy.exceptions;




/**
* @brief An exception thrown when given object does not implement an iterator interface
* @author Janusz Kowalczyk
* @created 2012-03-21
*/
class HasToBeIterableException extends Exception{

  public HasToBeIterableException(){
    super("Object has to implement an iterator interface");
  }

  public HasToBeIterableException(String msg){
    super(msg);
  }

  public HasToBeIterableException(String msg, Throwable t){
    super(msg, t);
  }

}

