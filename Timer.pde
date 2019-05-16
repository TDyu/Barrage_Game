class Timer {
  private int startTime;
  private int timer;
  
  public Timer(int timer) {
    this.startTime = 0;
    this.timer = timer;
  }
  
  public void timing() {
    startTime = millis(); // millis() is now   //(other func. :second() minute() hour() day() month() year())
  }
  
  public boolean isTime() {
    if (millis() - startTime >= timer) {
      return true;
    } else {
      return false;
    }
  }
  
  public int getStartTime() {
    return startTime;
  }
}