package labs.enums;

/**
* @brief ENUM type to store delay value
* @author Janusz Kowalczyk 38115ja
* @created 2011-12-15
*/
enum Delay {

  MS,
  SEC,
  MIN;
  
  private int delay;

  private Delay() {
  }

  private Delay( int value ) {
    this.delay = value;
  }

  public Delay set( int value ) {
    this.delay = value;
    return this;
  }

  public int getValue() {
    switch(this) {
      case MS: return this.delay;
      case SEC: return (this.delay * 1000);
      case MIN: return (this.delay * 60000);
      default: throw new IllegalStateException();
    }
  }
}
