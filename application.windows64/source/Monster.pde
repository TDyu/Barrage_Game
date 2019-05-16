/* The hat of each monster is random. */
/* If player shooted bullet that color is same as the hat of the attacked monster, then get attackPoint. */
/* If a monster touched the player except of antenna, then game is over. */

/* ***The original image of monster is designed by Fei. And is drawn by (TDyu).*** */

class Monster {
  private PVector position;
  private PVector velocity = new PVector(0, random(3, 6.3)); // Fall down
  private final RandomColor monsterColor = new RandomColor();

  // draw control
  private final int radius = 32;

  public Monster(PVector position) {
    this.position = position;
  }

  public int fall(PVector playerPos) {
    position.add(velocity);
    drawMonster();

    PVector playerUL = new PVector(playerPos.x-18*4 , playerPos.y-18*2); // left boundary of UFO-down, top of UFO-up
    float playerWidth = 18 * 6 - 18; // width of UFO-up & fine-tuning
    float playerHeight = 18 * 3 - 9.9; // height of UFO-down +UFO-up & fine-tuning
    
    if (position.y - radius*2.7 >= height) {
      return 1;
    } else if (collision(playerUL, playerWidth, playerHeight)) { // temp :PVector.dist(playerPos, position) < radius*2 + 2
      //fallAcceletation.y = 0;
      velocity.y = 0;
      return 2;
    } else {
      return 0;
    }
  }

  public void display(int x, int y) {
    position.x = x;
    position.y = y;
    drawMonster();
  }
  
  private void drawMonster() {
    float nose = 12;
    float eye = 16;
    float mouth = radius*0.7;
    
    beginShape();
    /* FACE */
    fill(#96EA80);
    noStroke();
    ellipse(position.x, position.y, radius*3.2, radius*4); // face

    /* Eyes */
    noStroke();
    fill(#071DF5);
    ellipse(position.x+radius*0.75, position.y-radius+eye, eye*2, eye); // right eye contour
    fill(0);
    ellipse(position.x+radius*0.75, position.y-radius+eye, eye/4, eye/4); // right eye*/
    fill(#071DF5);
    ellipse(position.x-radius*0.75, position.y-radius+eye, eye*2, eye); // left eye contour
    fill(0);
    ellipse(position.x-radius*0.75, position.y-radius+eye, eye/4, eye/4); // left eye*/

    /* NOSE */
    stroke(0);
    strokeWeight(5);
    fill(#96EA80);
    arc(position.x, position.y, nose, nose, radians(90), radians(270)); // nose

    /* MOUTH */
    line(position.x-radius, position.y+mouth, position.x-radius*0.75, position.y+radius); // mouth part 1
    line(position.x-radius*0.75, position.y+radius, position.x-radius*0.5, position.y+mouth); // mouth part 2
    line(position.x-radius*0.5, position.y+mouth, position.x-radius*0.25, position.y+radius); // mouth part 3
    line(position.x-radius*0.25, position.y+radius, position.x, position.y+mouth); // mouth part 4
    line(position.x, position.y+mouth, position.x+radius*0.25, position.y+radius); // mouth part 5
    line(position.x+radius*0.25, position.y+radius, position.x+radius*0.5, position.y+mouth); // mouth part 6
    line(position.x+radius*0.5, position.y+mouth, position.x+radius*0.75, position.y+radius); // mouth part 7
    line(position.x+radius*0.75, position.y+radius, position.x+radius, position.y+mouth); // mouth part 8

    /* HAT */
    stroke(#22B14B);
    strokeWeight(3);
    fill(monsterColor.getColor());
    triangle(position.x-radius*1.2, position.y-radius*1.25, position.x, position.y-radius*2.7, position.x+radius*1.2, position.y-radius*1.25); // hat
    line(position.x-radius*0.4, position.y-radius*2.13, position.x+radius*0.4, position.y-radius*2.13); // hat line up
    line(position.x-radius*0.8, position.y-radius*1.63, position.x+radius*0.8, position.y-radius*1.63); // hat line down
    endShape();
  }
  
  public color getColor() {
    return monsterColor.getColor();
  }
  
  public PVector getPos() {
    return position;
  }
  
  public boolean collision(PVector obeUL, float obeWidth, float obeHeight) { // rectangle text // UL : Upper left corner
    PVector selfUL = new PVector(position.x-radius*1.6 , position.y-radius*2.7); // left boundary of face, top of hat
    float selfWidth = radius*3.2 - 21; // width of face & fine-tuning
    float selfHeight = radius*4.7 - 3; // height of hat(2.7) + half height of face(2) & fine-tuning
    
    
    if (obeUL.x - selfUL.x >= 0.0000000000001 && obeUL.x - (selfUL.x + selfWidth) >= 0.0000000000001) { // absoULtely right
      return false;
    } else if (selfUL.x - obeUL.x >= 0.0000000000001 && selfUL.x - (obeUL.x + obeWidth) >= 0.0000000000001) {  // absoULtely left
      return false;
    } else if (obeUL.y - selfUL.y >= 0.0000000000001 && obeUL.y - (selfUL.y + selfHeight) >= 0.0000000000001) { // absoULtely up
      return false;
    } else if (selfUL.y - obeUL.y >= 0.0000000000001 && selfUL.y - (obeUL.y + obeHeight) >= 0.0000000000001) {  // absoULtely down
      return false;
    } else {
      return true;
    }
  }
}