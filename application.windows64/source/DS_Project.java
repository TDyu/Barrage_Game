import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DS_Project extends PApplet {



/* Opening */
//undone

/* Ending */
//Table scoreTable; // undone
Queue<Monster> attackedMonsters; // Using Queue
int count;
//BTree btree // undone

/* Game Controller */
int dodgePoint; // 1
int attackPoint; // 2
Timer timer;
int second;
boolean reStart;
boolean end;
boolean opening;

/* Player */
Player player;

/* Bullet - Shoot (Using Array) */
ArrayList<Bullet> shootBullets;
boolean topDetect;

/* Bullet - Fall (Using Array) */
ArrayList<Bullet> fallBullets;
int fallBulletDetect; // 0: Nothing, 1: Buttom 2: Player 
int bulletFallTime;
int bulletTimer;

/* Monster */
ArrayList<Monster> monsters;
int fallMonsterDetect; // 0: Nothing, 1: Buttom 2: Attack
int monsterFallTime;
int monsterTimer;
int monsterAcce;

/* Sound effects */
Minim minim;
AudioPlayer soundEff;

public void setup() {
  
  background(0);
  
  stroke(255);

  /* Opening */
  //undone

  /* Ending */
  //scoreTable = loadTable("scoreRanking.csv", "header");   // undone
  attackedMonsters = new Queue<Monster>();
  count = 0;
  //btree = new BTree(); // undone

  /* Game Controller */
  dodgePoint = 0;
  second = 48;
  timer = new Timer(second * 1000); // < 49000 millis => 48s
  timer.timing(); // start time (now)
  reStart = false;
  end = false;
  opening = true;

  /* Player */
  player = new Player();

  /* Bullet Shoot */
  shootBullets = new ArrayList<Bullet>();
  topDetect = false;

  /* Bullets Fall */
  fallBullets = new ArrayList<Bullet>();
  fallBulletDetect = 0;
  bulletFallTime = 96;
  bulletTimer = 0;

  /* Monster */
  monsters = new ArrayList<Monster>();
  fallMonsterDetect = 0;
  monsterFallTime = 48;
  monsterTimer = 0;
  monsterAcce = 0;

  /* Sound effects */
  minim = new Minim(this);
  soundEff = minim.loadFile("do.wav"); // get bullet
  //soundEff = minim.loadFile("daung.wav"); // successfully shoot
  //soundEff = minim.loadFile("baung.wav"); // shoot
}

public void draw() {
  background(0);

  if (opening) { // Opening
    PFont font = createFont("\u3010\u5d50\u3011\u828a\u67d4\u4f53", 27);
    fill(255);
    rect(50, 50, 700, 500);
    textFont(font);
    textSize(27);
    fill(0);
    text("Description", 51, 72);
    stroke(0);
    line(50, 76, 750, 76);
    String lines[] = loadStrings("Description.txt");
    textSize(16);
    for (int i=1; i<lines.length; i++) {
      println(lines[i]);
      text(lines[i], 51, 96+18.6f*(i-1));
    }

    //noLoop();
  } else if (end) {
    shootBullets.clear();
    if (count != attackPoint/2 && attackPoint != 0) {
      attackedMonsters.top().display(width/2, height/2); /*bug*/
      attackedMonsters.pop();
      if (count != attackPoint/2)  count++;
      /*soundEff = minim.loadFile("daung.wav");
       soundEff.play();*/

      textSize(27);
      fill(255);
      text("\u64ca\u843d\u4e86 ", 300, 76);
      fill(255, 0, 0);
      text(count, 386, 76);
      fill(255);
      text("\u96bb * 2-point", 396, 76);

      delay(1000); // 1s
    } else {
      textSize(27);
      fill(255);
      text("\u64ca\u843d\u4e86 ", 300, 76);
      fill(255, 0, 0);
      text(count, 386, 76);
      fill(255);
      text("\u96bb * 2-point", 402, 76);

      delay(1000); // 1s

      text("\u9583\u907f\u4e86 ", 300, 109);
      fill(255, 0, 0);
      text(dodgePoint, 386, 109);
      fill(255);
      text("\u96bb * 1-point", 402, 109);

      delay(1000); // 1s

      text("\u7e3d\u5f97\u5206 ", 300, 142);
      fill(255, 0, 0);
      text(dodgePoint + attackPoint, 386, 142);
      fill(255);
      text("\u5206", 402, 142);
      stop();
    }
    /*undone
     btree.push(dodgePoint + attackPoint, year()+"/"+month()+"/"+day()+" "+hour()+":"+second());
     
     for (TableRow row : scoreTable.rows()) { // load
     
     String id = row.getString("id");
     int score = row.getInt("score");
     
     btree.push(score, id);
     }
     
     TableRow row;
     for (int i=0; i<scoreTable.getRowCount(); i++) { // save & show
     row = scoreTable.getRow(i);
     row.setInt("score", );
     row.setString("id", );
     }
     */
  } else {
    // Reset
    if (reStart) {
      reset();
      timer.timing();
    }

    // Clock
    second = 48 - (millis() - timer.getStartTime())/1000;
    fill(255);
    textSize(27);
    text("00:", 6, 30); 
    if (second >=10) {
      text(second, 32, 30);
    } else {
      text(0 + second, 32, 30);
    }

    // Score
    text("Score: ", 6, 46);
    text(dodgePoint + attackPoint, 70, 46);

    // Player move
    player.display(mouseX, height-player.getRadius()*2);

    // Attack Detect
    attack();

    //{{ Monster Fall
    if (monsterTimer == monsterFallTime) {
      newNormalMonster();
      monsterTimer = 0;
      monsterFallTime = (int)random(72, 81) - monsterAcce;
    } else {
      monsterTimer++;
    }
    if (monsters.size() != 0) {
      for (int i=0; i<monsters.size(); i++) {
        fallMonsterDetect = monsters.get(i).fall(player.getPos());
        if (fallMonsterDetect == 1) { // Button Detect
          monsters.remove(i);
          dodgePoint++;
          println(dodgePoint); // for test
        } else if (fallMonsterDetect == 2) { // Attact
          end = true;
        }
      }
    }
    if (frameCount % 81 == 0 && monsterAcce <= 50) {
      monsterAcce += 3;
    }
    //}}

    // Attack Detect
    attack();

    //{{ Bullets Fall
    if (bulletTimer == bulletFallTime) {
      newBullet();
      bulletTimer = 0;
    } else {
      bulletTimer++;
    }
    if (fallBullets.size() != 0) {
      for (int i=0; i<fallBullets.size(); i++) {
        fallBulletDetect = fallBullets.get(i).fall(player.getPos());
        if (fallBulletDetect == 1) { // Button Detect
          fallBullets.remove(i);
        } else if (fallBulletDetect == 2) { // Player Detect
          player.getBullet(fallBullets.get(i), fallBullets.get(i).getColor());
          soundEff = minim.loadFile("do.wav");
          soundEff.play();
          fallBullets.remove(i);
        }
      }
    }
    //}}

    // Draw Bullets Shoot
    if (shootBullets.size() != 0) {
      for (int i=0; i<shootBullets.size(); i++) {
        topDetect = shootBullets.get(i).shoot();
        if (topDetect) {
          shootBullets.remove(i);
        }
      }
    }

    // Attack Detect
    attack();

    // Time Over
    if (timer.isTime()) {
      end = true;
    } else {
      println(millis() - timer.getStartTime()); // for test
    }
  }
}

public void newBullet() {
  PVector newBulletPos = new PVector(random(0+player.getRadius()*4, width-player.getRadius()*4), 0);
  Bullet newBullet = new Bullet(newBulletPos);
  fallBullets.add(newBullet);
}

public void newNormalMonster() {
  PVector newMonsterPos = new PVector(random(0+player.getRadius()*4, width-player.getRadius()*4), 0);
  Monster newMonster = new Monster(newMonsterPos);
  monsters.add(newMonster);
}

public void attack() { // algorithmneed to be optimized 
  if (!end && shootBullets.size() != 0 && monsters.size() != 0) {
    for (int i=0; i<shootBullets.size(); i++) {
      for (int j=0; j<monsters.size(); j++) {
        PVector bulletUL = new PVector(shootBullets.get(i).getPos().x-shootBullets.get(i).getRadius()-5, shootBullets.get(i).getPos().y-shootBullets.get(i).getRadius()); // left boundary of bullet, top of bullet & fine-tuning
        float bulletWidth = shootBullets.get(i).getRadius() * 1.8f + 10; // & fine-tuning
        float bulletHeight = shootBullets.get(i).getRadius() * 2; 

        if (shootBullets.get(i).getColor() == monsters.get(j).getColor() && monsters.get(j).collision(bulletUL, bulletWidth, bulletHeight)) { // temp :PVector.dist(shootBullets.get(i).getPos(), monsters.get(i).getPos()) < 32
          attackedMonsters.push(monsters.get(j));
          monsters.remove(j);
          attackPoint += 2;
          soundEff = minim.loadFile("daung.wav");
          soundEff.play();
          if (monsters.size() == 0 || shootBullets.size() == 0) {
            return;
          }
        }
      }
    }
  }
}

public void reset() {
  /* Ending */
  count = 0;

  /* Game Controller */
  dodgePoint = 0;
  second = 48;
  timer.timing(); // start time (now)
  reStart = false;
  end = false;

  /* Player */
  while (player.bullets.getSize() != 0) {
    player.bullets.pop();
  }
  player.setColor(0xffFFFFFF);

  /* Bullet Shoot */
  shootBullets.clear();
  topDetect = false;

  /* Bullets Fall */
  fallBullets.clear();
  fallBulletDetect = 0;
  bulletFallTime = 96;
  bulletTimer = 0;

  /* Monster */
  monsters.clear();
  fallMonsterDetect = 0;
  monsterFallTime = 48;
  monsterTimer = 0;
  monsterAcce = 0;
}

public void mousePressed() {
  if (opening && mouseButton == LEFT) {
    opening = false;
    reset();
  } else if (mouseButton == LEFT) {
    if (!player.isBulletEmpty()) {
      Bullet newShoot = player.shoot().copy();
      soundEff = minim.loadFile("baung.wav");
      soundEff.play();
      newShoot.setPos(player.getPos().copy());
      shootBullets.add(newShoot);
    }
  } else if (mouseButton == RIGHT) {
    reStart = true;
    end = false;
    loop();
  }
}

public void stop() {
  noLoop();
  soundEff.close();
  minim.stop();
}

class BTree {
  private BTreeNode root;

  public BTree() {
    this.root = null;
  }

  public void push(int score, String id) {
    if (root == null) {
      root = new BTreeNode(score, id);
    } else {
      push(score, id, root);
    }
  }
  private void push(int score, String id, BTreeNode leaf) { // insert private version
    if (score < leaf.score) {
      if (leaf.left == null) {
        leaf.left = new BTreeNode(score, id);
      } else {
        push(score, id, leaf.left);
      }
    } else { // data >= leaf->data
      if (leaf.right == null) {
        leaf.right = new BTreeNode(score, id);
      } else {
        push(score, id, leaf.right);
      }
    }
  }
  //}}

  //{{ Ascending
  /*public void ascending() { // public version // Inorder Traversal
    ascending(root, 0);
  }
  private String ascending(BTreeNode ptr, int in) { // private version
    Table scoreTable = loadTable("scoreRanking.csv", "header");
    TableRow row = scoreTable.getRow(in);
    String info = null;
    if (ptr != null) {
      ascending(ptr.left);
      textSize(27);
      fill(255);
      return 
      ascending(ptr.right);
    } else {
      return;
    }
  }*/
  //}}
}

class BTreeNode {
  private BTreeNode left;
  private BTreeNode right;
  private int score;
  private String id;
  
  public BTreeNode(int score, String id) {
    this.left = this.right = null;
    this.score = score;
    this.id = id;
  }
  
  public int getData() {
    return score;
  }
  
  public String getId() {
    return id;
  }
  
}
class Bullet {
  private PVector position;
  private final int radius = 48;
  private final RandomColor bulletColor;

  private PVector shootVelocity = new PVector(0, -4.8f); // north
  private PVector fallVelocity = new PVector(0, random(4.8f, 9)); // south
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
    strokeWeight(3.9f);
    arc(position.x, position.y, radius*2, radius*2, radians(225), radians(315)); // x, y, width, height, start radians, end radians
    endShape();
  }
  
  private void setPos(PVector pos) {
    position = pos;
  }
  
  public int getColor() {
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
/* The hat of each monster is random. */
/* If player shooted bullet that color is same as the hat of the attacked monster, then get attackPoint. */
/* If a monster touched the player except of antenna, then game is over. */

/* ***The original image of monster is designed by Fei. And is drawn by (TDyu).*** */

class Monster {
  private PVector position;
  private PVector velocity = new PVector(0, random(3, 6.3f)); // Fall down
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
    float playerHeight = 18 * 3 - 9.9f; // height of UFO-down +UFO-up & fine-tuning
    
    if (position.y - radius*2.7f >= height) {
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
    float mouth = radius*0.7f;
    
    beginShape();
    /* FACE */
    fill(0xff96EA80);
    noStroke();
    ellipse(position.x, position.y, radius*3.2f, radius*4); // face

    /* Eyes */
    noStroke();
    fill(0xff071DF5);
    ellipse(position.x+radius*0.75f, position.y-radius+eye, eye*2, eye); // right eye contour
    fill(0);
    ellipse(position.x+radius*0.75f, position.y-radius+eye, eye/4, eye/4); // right eye*/
    fill(0xff071DF5);
    ellipse(position.x-radius*0.75f, position.y-radius+eye, eye*2, eye); // left eye contour
    fill(0);
    ellipse(position.x-radius*0.75f, position.y-radius+eye, eye/4, eye/4); // left eye*/

    /* NOSE */
    stroke(0);
    strokeWeight(5);
    fill(0xff96EA80);
    arc(position.x, position.y, nose, nose, radians(90), radians(270)); // nose

    /* MOUTH */
    line(position.x-radius, position.y+mouth, position.x-radius*0.75f, position.y+radius); // mouth part 1
    line(position.x-radius*0.75f, position.y+radius, position.x-radius*0.5f, position.y+mouth); // mouth part 2
    line(position.x-radius*0.5f, position.y+mouth, position.x-radius*0.25f, position.y+radius); // mouth part 3
    line(position.x-radius*0.25f, position.y+radius, position.x, position.y+mouth); // mouth part 4
    line(position.x, position.y+mouth, position.x+radius*0.25f, position.y+radius); // mouth part 5
    line(position.x+radius*0.25f, position.y+radius, position.x+radius*0.5f, position.y+mouth); // mouth part 6
    line(position.x+radius*0.5f, position.y+mouth, position.x+radius*0.75f, position.y+radius); // mouth part 7
    line(position.x+radius*0.75f, position.y+radius, position.x+radius, position.y+mouth); // mouth part 8

    /* HAT */
    stroke(0xff22B14B);
    strokeWeight(3);
    fill(monsterColor.getColor());
    triangle(position.x-radius*1.2f, position.y-radius*1.25f, position.x, position.y-radius*2.7f, position.x+radius*1.2f, position.y-radius*1.25f); // hat
    line(position.x-radius*0.4f, position.y-radius*2.13f, position.x+radius*0.4f, position.y-radius*2.13f); // hat line up
    line(position.x-radius*0.8f, position.y-radius*1.63f, position.x+radius*0.8f, position.y-radius*1.63f); // hat line down
    endShape();
  }
  
  public int getColor() {
    return monsterColor.getColor();
  }
  
  public PVector getPos() {
    return position;
  }
  
  public boolean collision(PVector obeUL, float obeWidth, float obeHeight) { // rectangle text // UL : Upper left corner
    PVector selfUL = new PVector(position.x-radius*1.6f , position.y-radius*2.7f); // left boundary of face, top of hat
    float selfWidth = radius*3.2f - 21; // width of face & fine-tuning
    float selfHeight = radius*4.7f - 3; // height of hat(2.7) + half height of face(2) & fine-tuning
    
    
    if (obeUL.x - selfUL.x >= 0.0000000000001f && obeUL.x - (selfUL.x + selfWidth) >= 0.0000000000001f) { // absoULtely right
      return false;
    } else if (selfUL.x - obeUL.x >= 0.0000000000001f && selfUL.x - (obeUL.x + obeWidth) >= 0.0000000000001f) {  // absoULtely left
      return false;
    } else if (obeUL.y - selfUL.y >= 0.0000000000001f && obeUL.y - (selfUL.y + selfHeight) >= 0.0000000000001f) { // absoULtely up
      return false;
    } else if (selfUL.y - obeUL.y >= 0.0000000000001f && selfUL.y - (obeUL.y + obeHeight) >= 0.0000000000001f) {  // absoULtely down
      return false;
    } else {
      return true;
    }
  }
}
class Node<T> {
  private T data;
  private Node next;
  
  public Node(T data) {
    this.data = data;
    this.next = null;
  }
  
  public void setData(T data) {
    this.data = data;
  }
  
  public T getData() {
    return data;
  }
  
  public void setNext(Node next) {
    this.next = next;
  }
  
  public Node getNext() {
    return next;
  }
}
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
  private int playerColor;
  private Stack<Bullet> bullets;

  // draw control
  private final int radius = 18;

  public Player() {
    super();
    //this.position = new PVector(mouseX, height-radius*2);
    this.position = new PVector(0, 0);
    this.bullets = new Stack<Bullet>();
    this.playerColor = 0xffFFFFFF;
  }

  //{{Display
  public void display(int x, int y) {
    drawPlayer();
    setPosition(x, y);
  }
  private void drawPlayer() {
    float eye = 4.8f;
    int line = 10;

    beginShape();
    /*fill(playerColor);
     stroke(255);
     strokeWeight(3.9);
     arc(position.x, position.y, radius*2, radius*2, radians(315), radians(585)); // x, y, width, height, start radians, end radians (( end must > start. 0 is 0 of +x*/

    /* UFO - UP */
    fill(0xffB0D935);
    noStroke();
    arc(position.x, position.y+radius, radius*6, radius*6, radians(180), radians(360)); // UFO up

    /* FACE */
    fill(0xffFF7C8F);
    ellipse(position.x, position.y, radius*2, radius*2); // face

    /* FEELER */
    stroke(0);
    strokeWeight(2);
    fill(0xff96EA80);
    curve(position.x-radius*0.5f, position.y, position.x-radius, position.y-radius, position.x-radius*0.5f, position.y-radius*0.8f, position.x-radius*2, position.y-radius*0.5f);
    curve(position.x+radius*0.5f, position.y, position.x+radius, position.y-radius, position.x+radius*0.5f, position.y-radius*0.8f, position.x+radius*2, position.y-radius*0.5f);
    
    /* EYES */
    fill(0);
    strokeWeight(0);
    ellipse(position.x-radius/4-4, position.y-radius/2-eye+12, eye*1.3f, eye*1.3f); // left eye
    ellipse(position.x+radius/4+4, position.y-radius/2-eye+12, eye*1.3f, eye*1.3f); // right eye

    /* UFO - DOWN */
    fill(0xffB0D935);
    noStroke();
    rectMode(CENTER);
    rect(position.x, position.y+1.5f*radius, radius*8, radius, 360); // UPO down
    // LINES of UFO - DOWN
    stroke(0xffC2C5BA);
    strokeWeight(10);
    line(position.x-radius*2.5f, position.y+radius*1.5f, position.x-radius*2.5f, position.y+radius*2-8.1f); // UPO line 1
    line(position.x-radius, position.y+radius*1.5f, position.x-radius, position.y+radius*2-8.1f); // UPO line 2
    line(position.x+radius, position.y+radius*1.5f, position.x+radius, position.y+radius*2-8.1f); // UPO line 3
    line(position.x+radius*2.5f, position.y+radius*1.5f, position.x+radius*2.5f, position.y+radius*2-8.1f); // UPO line 4

    /* ANTENNA */
    stroke(0xff89D2DB);
    strokeWeight(5);
    line(position.x+2*radius+7, position.y-radius, position.x+2*radius+line*2+3, position.y-radius-line*2); // right antenna
    fill(playerColor);
    noStroke();
    ellipse(position.x+2*radius+line*2+3, position.y-radius-line*2, line*2, line*2); // ball of right antenna
    stroke(0xff89D2DB);
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

  public void getBullet(Bullet bullet, int playerColor) {
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
        playerColor = 0xffFFFFFF;
      }
    }
    return re;
  }

  public void setColor(int playerColor) {
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
/* Use Singly Linkes List */
class Queue<T> {
  private SSL<T> queue;
    
  public Queue() {
    super();
    this.queue = new SSL<T>();
  }
  
  public void push(T data) { // form tail
    queue.pushInTail(data);
  }
  
  public void pop() { // from head
    queue.pop();
  }
  
  public T top() {
    return queue.top();
  }
  
  public int getSize() {
    return queue.getSize();
  }
  
  public boolean isEmpty() {
    return queue.isEmpty();
  }

  /*public void show() {
    queue.show();
  }*/
}
class RandomColor {
  private int obejectColor;
  
  public RandomColor() {
    setColor();
  }
  
  private void setColor() {
    int[] colorPool = {0xff29A4FF, 0xffD16ECA, 0xff4B003E, 0xffFF0000, 0xff1CFF00, 0xffF6FF08, 0xffFF080C, 0xff8E8E8E, 0xffFF9600, /*#89D2DB, #96EA80*/}; // 9 (11) // Using Array //
    obejectColor = colorPool[(int)random(0, 9)];
  }
  
  public void setColor(int obejectColor) {
    this.obejectColor = obejectColor;
  }
  
  public int getColor() {
    return obejectColor;
  }
}
class SSL<T> {
  private Node<T> top;
  private Node<T> tail;
  private T data; 
  private int size;

  public SSL() {
    super();
    this.top = null;
    this.tail = null;
    this.size = 0;
  }

  public void pushInTop(T data) {
    if (isEmpty()) {
      top = new Node(data);
      tail = top;
      size++;
    } else {
      Node newNode = new Node(data);
      newNode.setNext(top);
      top = newNode;
      size++;
    }
  }

  public void pushInTail(T data) {
    if (isEmpty()) {
      tail = new Node(data);
      top = tail;
      size++;
    } else {
      Node newNode = new Node(data);
      tail.setNext(newNode);
      tail = newNode;
      size++;
    }
  }

  public void pop() { // from top/head
    if (isEmpty()) {
    } else if (top.getNext() == null) {
      top = null;
      size--;
    } else {
      Node deleteNode = top;
      top = top.getNext();
      deleteNode = null;
      /*Node temp = top.getNext();
       top.setNext(null);
       top = temp;*/
      size--;
    }
  }

  public int getSize() {
    return size;
  }

  public boolean isEmpty() {
    return (size == 0);
  }

  public T top() {
    if (isEmpty()) {
      return null;
    } else {
      return top.getData();
    }
  }

  /*public void show() { // from op/head
    Node current = top;
    if (!isEmpty()) {
      while (current != tail) {
        System.out.print(current.getData() + "   ");
        current = current.getNext();
      }
      System.out.println(current.getData());
    } else {
      System.out.println();
    }
  }*/
}
/* Use Singly Linkes List */
class Stack<T> {
  private SSL<T> stack;
    
  public Stack() {
    super();
    this.stack = new SSL();
  }
    
  public void push(T data) { // form top
    stack.pushInTop(data);
  }
  
  public void pop() { // from top
    stack.pop();
  }
  
  public T top() {
    return stack.top();
  }
  
  public int getSize() {
    return stack.getSize();
  }
  
  public boolean isEmpty() {
    return stack.isEmpty();
  }
   
  /*public void show() {
    stack.show();
  }*/
}
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
  public void settings() {  size(800, 600);  smooth(100); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DS_Project" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
