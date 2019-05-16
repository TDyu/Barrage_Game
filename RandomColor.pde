class RandomColor {
  private color obejectColor;
  
  public RandomColor() {
    setColor();
  }
  
  private void setColor() {
    color[] colorPool = {#29A4FF, #D16ECA, #4B003E, #FF0000, #1CFF00, #F6FF08, #FF080C, #8E8E8E, #FF9600, /*#89D2DB, #96EA80*/}; // 9 (11) // Using Array //
    obejectColor = colorPool[(int)random(0, 9)];
  }
  
  public void setColor(color obejectColor) {
    this.obejectColor = obejectColor;
  }
  
  public color getColor() {
    return obejectColor;
  }
}