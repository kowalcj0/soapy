package labs.helpers;


/**
* @brief A simple class to hold the delay time in ms;
* @author Janusz Kowalczyk 38115ja
* @created 2011-12-14
*/
class Delay {

  private int delay;

  Delay( int delay ) {
    this.delay = delay;
  }

  public int getValue() {
    return this.delay;
  }
  
}
