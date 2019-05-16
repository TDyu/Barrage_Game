/* Own bullets useing Stack
/* When touch the bullet, the player get it.
/* Each time shoot the latest bullet.
/* The color of each bullet is random.
/* Tho color of the player's balls of antennas are same as the latest bullet player own, or is white if player did not have any bullet.
/* Click the left mouse button to fire.
 */
 
/* ***The original image of player is designed by WenTing & Fei. And is drawn by (TDyu).*** */

class Player {
  private PVector position;
  private color playerColor;
  private Stack<Bullet> bullets;

  // draw control
  private final int radius = 18;

  public Player() {
    super();
    //this.position = new PVector(mouseX, height-radius*2);
    this.position = new PVector(0, 0);
    this.bullets = new Stack<Bullet>();
    this.playerColor = #FFFFFF;
  }

  //{{Display
  public void display(int x, int y) {
    drawPlayer();
    setPosition(x, y);
  }
  private void drawPlayer() {
    float eye = 4.8;
    int line = 10;

    beginShape();
    /*fill(playerColor);
     stroke(255);
     strokeWeight(3.9);
     arc(position.x, position.y, radius*2, radius*2, radians(315), radians(585)); // x, y, width, height, start radians, end radians (( end must > start. 0 is 0 of +x*/

    /* UFO - UP */
    fill(#B0D935);
    noStroke();
    arc(position.x, position.y+radius, radius*6, radius*6, radians(180), radians(360)); // UFO up

    /* FACE */
    fill(#FF7C8F);
    ellipse(position.x, position.y, radius*2, radius*2); // face

    /* FEELER */
    stroke(0);
    strokeWeight(2);
    fill(#96EA80);
    curve(position.x-radius*0.5, position.y, position.x-radius, position.y-radius, position.x-radius*0.5, position.y-radius*0.8, position.x-radius*2, position.y-radius*0.5);
    curve(position.x+radius*0.5, position.y, position.x+radius, position.y-radius, position.x+radius*0.5, position.y-radius*0.8, position.x+radius*2, position.y-radius*0.5);
    
    /* EYES */
    fill(0);
    strokeWeight(0);
    ellipse(position.x-radius/4-4, position.y-radius/2-eye+12, eye*1.3, eye*1.3); // left eye
    ellipse(position.x+radius/4+4, position.y-radius/2-eye+12, eye*1.3, eye*1.3); // right eye

    /* UFO - DOWN */
    fill(#B0D935);
    noStroke();
    rectMode(CENTER);
    rect(position.x, position.y+1.5*radius, radius*8, radius, 360); // UPO down
    // LINES of UFO - DOWN
    stroke(#C2C5BA);
    strokeWeight(10);
    line(position.x-radius*2.5, position.y+radius*1.5, position.x-radius*2.5, position.y+radius*2-8.1); // UPO line 1
    line(position.x-radius, position.y+radius*1.5, position.x-radius, position.y+radius*2-8.1); // UPO line 2
    line(position.x+radius, position.y+radius*1.5, position.x+radius, position.y+radius*2-8.1); // UPO line 3
    line(position.x+radius*2.5, position.y+radius*1.5, position.x+radius*2.5, position.y+radius*2-8.1); // UPO line 4

    /* ANTENNA */
    stroke(#89D2DB);
    strokeWeight(5);
    line(position.x+2*radius+7, position.y-radius, position.x+2*radius+line*2+3, position.y-radius-line*2); // right antenna
    fill(playerColor);
    noStroke();
    ellipse(position.x+2*radius+line*2+3, position.y-radius-line*2, line*2, line*2); // ball of right antenna
    stroke(#89D2DB);
    strokeWeight(5);
    line(position.x-2*radius-7, position.y-radius, position.x-2*radius-line*2-3, position.y-radius-line*2); // left antenna
    fill(playerColor);
    noStroke();
    ellipse(position.x-2*radius-line*2-3, position.y-radius-line*2, line*2, line*2); // ball of left antenna
    endShape();
  }
  private void setPosition(int x, int y) {
    if (x - radius*4 <= 0) {
      position.x = 0 + radius*4;
    } else if (x + radius*4 > width) {
      position.x = width - radius*4;
    } else {
      position.x = x;
      position.y = y;
    }
  }
  //}}

  public void getBullet(Bullet bullet, color playerColor) {
    setColor(playerColor);
    bullets.push(bullet);
  }

  public Bullet shoot() {
    Bullet re = null;
    if (!bullets.isEmpty()) {
      re = bullets.top();
      bullets.pop();
      if (!bullets.isEmpty()) {
        playerColor = bullets.top().getColor();
      } else {
        playerColor = #FFFFFF;
      }
    }
    return re;
  }

  public void setColor(color playerColor) {
    this.playerColor = playerColor;
  }

  public float getXPos() {
    return position.x;
  }

  public PVector getPos() {
    return position;
  }

  public int getRadius() {
    return radius;
  }

  public boolean isBulletEmpty() {
    return bullets.isEmpty();
  }
}