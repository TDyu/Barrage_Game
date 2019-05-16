class Bullet {
  private PVector position;
  private final int radius = 48;
  private final RandomColor bulletColor;

  private PVector shootVelocity = new PVector(0, -4.8); // north
  private PVector fallVelocity = new PVector(0, random(4.8, 9)); // south
  /*private final PVector shootAcceleration = new PVector(0, -0.18);
  private final PVector fallAcceletation = new PVector(0, 0.048);*/

  public Bullet(PVector position) {
    super();
    this.position = position;
    this.bulletColor = new RandomColor();
  }

  public Bullet(PVector position, RandomColor bulletColor) {
    super();
    this.position = position;
    this.bulletColor = bulletColor;
  }
  
  public boolean shoot() {
    //display(shootAcceleration);
    display(shootVelocity);
    drawBullet();

    if (position.y <= 0) {
      return true;
    } else {
      return false;
    }
  }
  
  public int fall(PVector playerPos) {
    //display(fallAcceletation);
    display(fallVelocity);
    drawBullet();
    
    if (position.y >= height) {
      return 1;
    } else if (PVector.dist(playerPos, position) < 20) {
      //fallAcceletation.y = 0;
      fallVelocity.y = 0;
      return 2;
    } else {
      return 0;
    }
  }
  
  private void display(PVector velocity) {
    //velocity.add(acceleration);
    position.add(velocity);
  }
  
  private void drawBullet() {
    beginShape();
    fill(bulletColor.getColor());
    stroke(255);
    strokeWeight(3.9);
    arc(position.x, position.y, radius*2, radius*2, radians(225), radians(315)); // x, y, width, height, start radians, end radians
    endShape();
  }
  
  private void setPos(PVector pos) {
    position = pos;
  }
  
  public color getColor() {
    return bulletColor.getColor();
  }
  
  public PVector getPos() {
    return position;
  }
  
  public int getRadius() {
    return radius;
  }
  
  public Bullet copy() {
    Bullet copy = new Bullet(position.copy(), bulletColor);
    return copy;
  }
}