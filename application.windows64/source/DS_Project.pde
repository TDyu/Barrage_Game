import ddf.minim.*;

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

void setup() {
  size(800, 600);
  background(0);
  smooth(100);
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

void draw() {
  background(0);

  if (opening) { // Opening
    PFont font = createFont("【嵐】芊柔体", 27);
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
      text(lines[i], 51, 96+18.6*(i-1));
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
      text("擊落了 ", 300, 76);
      fill(255, 0, 0);
      text(count, 386, 76);
      fill(255);
      text("隻 * 2-point", 396, 76);

      delay(1000); // 1s
    } else {
      textSize(27);
      fill(255);
      text("擊落了 ", 300, 76);
      fill(255, 0, 0);
      text(count, 386, 76);
      fill(255);
      text("隻 * 2-point", 402, 76);

      delay(1000); // 1s

      text("閃避了 ", 300, 109);
      fill(255, 0, 0);
      text(dodgePoint, 386, 109);
      fill(255);
      text("隻 * 1-point", 402, 109);

      delay(1000); // 1s

      text("總得分 ", 300, 142);
      fill(255, 0, 0);
      text(dodgePoint + attackPoint, 386, 142);
      fill(255);
      text("分", 402, 142);
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

void newBullet() {
  PVector newBulletPos = new PVector(random(0+player.getRadius()*4, width-player.getRadius()*4), 0);
  Bullet newBullet = new Bullet(newBulletPos);
  fallBullets.add(newBullet);
}

void newNormalMonster() {
  PVector newMonsterPos = new PVector(random(0+player.getRadius()*4, width-player.getRadius()*4), 0);
  Monster newMonster = new Monster(newMonsterPos);
  monsters.add(newMonster);
}

void attack() { // algorithmneed to be optimized 
  if (!end && shootBullets.size() != 0 && monsters.size() != 0) {
    for (int i=0; i<shootBullets.size(); i++) {
      for (int j=0; j<monsters.size(); j++) {
        PVector bulletUL = new PVector(shootBullets.get(i).getPos().x-shootBullets.get(i).getRadius()-5, shootBullets.get(i).getPos().y-shootBullets.get(i).getRadius()); // left boundary of bullet, top of bullet & fine-tuning
        float bulletWidth = shootBullets.get(i).getRadius() * 1.8 + 10; // & fine-tuning
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

void reset() {
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
  player.setColor(#FFFFFF);

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

void mousePressed() {
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

void stop() {
  noLoop();
  soundEff.close();
  minim.stop();
}