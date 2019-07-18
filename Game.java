import java.io.*;
import java.lang.*;
import javax.swing.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

//Left Paddle Code
class LeftPaddle {
  private int xPos = 20;
  private int yPos = 20;

  // Set position of left paddle
  public void setPos(int pos) {
    // this keyword refers to current class object
    this.yPos = pos;
  }

  // Get x position of left paddle
  public int getxPos() {
    return this.xPos;
  }

  // Get y position of left paddle
  public int getyPos() {
    return this.yPos;
  }
}

// Right Paddle Code
class RightPaddle {
  private int xPos = 770;
  private int yPos = 20;

  // Sets position of right paddle same as ball y position
  public RightPaddle(int ballPos) {
    this.yPos = ballPos;
  }

  // Set position of right paddle
  public void setPos(int pos) {
    this.yPos = pos;
  }

  // Get x position of right paddle
  public int getxPos() {
    return this.xPos;
  }

  // Get y position of right paddle
  public int getyPos() {
    return this.yPos;
  }
}

// Ball Code
// Movement is changed with collision detection
class Ball {
  private int xPos;
  private int yPos;

  // These control the acceleration of the ball
  private int dx = -10;
  private int dy = 3;

  // Constructor
  // Sets initial position of ball to middle of applet window
  Ball() {
    setPos(400, 250);
  }

  // Set position of ball
  public void setPos(int x, int y) {
    this.xPos = x;
    this.yPos = y;
  }

  // Get x position of ball
  public int getxPos() {
    return this.xPos;
  }

  // Get y position of ball
  public int getyPos() {
    return this.yPos;
  }

  // Set x acceleration of ball
  public void setxAcc(int x) {
    this.dx = x;
  }

  // Set y acceleration of ball
  public void setyAcc(int y) {
    this.dy = y;
  }

  // Get x acceleration of ball
  public int getxAcc() {
    return this.dx;
  }

  // Get y acceleration of ball
  public int getyAcc() {
    return this.dy;
  }

  // Controls movement of ball with a set acceleration
  public void move() {
    setPos(this.xPos + dx, this.yPos + dy);
  }

  // Resets the ball position and acceleration
  public void reset() {
    setPos(400, 250);
    dx = -10;
    dy = 5;
  }
}

public class Game implements ActionListener {

  // Frame Components. One each for starting frame and end frame.
  JFrame f1;
  JPanel p1, p2, p3, p4;
  JButton b1, b2, b3, b4, b5, b6;
  JLabel l1, l2, l3, l4, l5;
  GridLayout gl;
  Font ft;
  int player;
  Image img;
  Dimension dim;

  Game()
  {
    dim = Toolkit.getDefaultToolkit().getScreenSize();
  }

  // Main menu frame
  // No seperate frame used here
  // Background image is drawn on window
  // Controls drawn over the background image
  // controlname.setMaximumSize(Dimension) sets maximum size of control
  // controlname.setBounds(Rectangle dimensions) sets location where control is
  // drawn
  // setLayout(null) is required to manually position controls
  // Contains three buttons: Computer,Player,Instructions
  public void startframe() {
    f1.getContentPane().removeAll();
    p1 = new Panel1();
    f1.add(p1);
    f1.revalidate();
    f1.repaint();
    b1 = new JButton("Computer");
    b1.addActionListener(this);
    b2 = new JButton("Player");
    b2.addActionListener(this);
    b3 = new JButton("Instructions");
    b3.addActionListener(this);
    ft = new Font("Courier", 1, 20);
    b1.setFont(ft);
    b2.setFont(ft);
    b3.setFont(ft);
    p1.setLayout(null);
    p1.setOpaque(true);
    p1.add(b1);
    p1.add(b2);
    p1.add(b3);
    b1.setBounds(new Rectangle(310, 460, 200, 30));
    b2.setBounds(new Rectangle(310, 500, 200, 30));
    b3.setBounds(new Rectangle(310, 540, 200, 30));
    f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f1.setSize(820, 630);
    f1.setTitle("Welcome!");
    f1.setLocation(dim.width/2-f1.getSize().width, dim.height/2-f1.getSize().height/2);
    f1.setVisible(true);
  }

  public void gameframe() {

    f1.getContentPane().removeAll();
    b6=new JButton("Exit");
    b6.addActionListener(this);
    p2 = new Panel2(this);
    p2.setOpaque(true);
    f1.add(p2);
    p2.add(b6);
    p2.requestFocus();
    p2.setBackground(Color.green);
    f1.revalidate();
    f1.repaint();
    b6.setBounds(new Rectangle(700, 10, 80, 30));
    f1.setTitle("Ping Pong");
  }

  // Instructions frame
  // Shows instructions and ok button
  public void instructions() {
    f1.getContentPane().removeAll();
    p3 = new JPanel();
    p3.setBackground(Color.BLACK);
    f1.add(p3);
    f1.revalidate();
    f1.repaint();
    p3.setLayout(null);
    p3.setOpaque(true);
    l2 = new JLabel("* Use mouse to control player 1");
    l3 = new JLabel("* Use up and down keys to control player 2");
    l4 = new JLabel("* Difficulty increases at level 5 and 10");
    l5 = new JLabel("* Game speed increases with levels");
    b4 = new JButton("Ok");
    b4.setBounds(new Rectangle(330, 500, 200, 30));
    b4.addActionListener(this);
    ft = new Font("Courier", 1, 22);
    l2.setFont(ft);
    l3.setFont(ft);
    l4.setFont(ft);
    l5.setFont(ft);
    l2.setForeground(Color.WHITE);
    l3.setForeground(Color.WHITE);
    l4.setForeground(Color.WHITE);
    l5.setForeground(Color.WHITE);
    b4.setFont(ft);
    p3.add(l2);
    p3.add(l3);
    p3.add(l4);
    p3.add(l5);
    p3.add(b4);
    l2.setBounds(new Rectangle(50, 150, 700, 30));
    l3.setBounds(new Rectangle(50, 200, 700, 30));
    l4.setBounds(new Rectangle(50, 250, 700, 30));
    f1.setTitle("Instructions");
  }

  // End frame when level reaches 4
  // Shows winner and restart button
  public void endframe(int pwin) {
    f1.getContentPane().removeAll();
    p4 = new JPanel();
    f1.add(p4);
    f1.revalidate();
    f1.repaint();
    p4.setLayout(null);
    p4.setOpaque(true);
    p4.setBackground(Color.BLACK);
    if(player!=1)
    {
    ft=new Font("Courier",1,50);
    l1=new JLabel("Computer Wins!");
    l1.setFont(ft);
    l1.setBounds(new Rectangle(100, 150, 700, 100));
    }
    else
    {
    ft=new Font("Courier",1,50);
    l1=new JLabel("Player "+ pwin+ " Wins!");
    l1.setFont(ft);
    l1.setBounds(new Rectangle(100, 150, 700, 100));
    }
    b5 = new JButton("Restart");
    b5.setBounds(new Rectangle(330, 460, 200, 50));
    b5.addActionListener(this);
    ft = new Font("Courier", 1, 20);
    l1.setForeground(Color.WHITE);
    b5.setFont(ft);
    p4.add(l1);
    p4.add(b5);
    // p3.add(Box.createVerticalStrut(20));
    f1.setTitle("End Screen");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        // System.out.println(SwingUtilities.isEventDispatchThread());
        // System.out.println(Thread.currentThread().getName());
        Game pingpong = new Game();
        pingpong.f1 = new JFrame();
        pingpong.startframe();
      }
    });

  }

  public void actionPerformed(ActionEvent ae) {

    // Select Computer
    if (ae.getSource() == b1) {
      player=0;
      this.gameframe();
    } 
    else if (ae.getSource() == b2) {
      player=1;
      this.gameframe();
    }
    else if (ae.getSource() == b3) {
      this.instructions();
    } 
    else if (ae.getSource() == b4) {
      this.startframe();
    }
    else if(ae.getSource()==b5){
      this.startframe();
    }
    else if(ae.getSource()==b6){
      this.startframe();
    }
  }
}

class Panel1 extends JPanel {

  private static final long serialVersionUID = 1L;
  private BufferedImage img;

  public Panel1() {
    try {
      img = ImageIO.read(new File("pong.png"));
    } catch (IOException ex) {
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(img, 0, 0, null);
  }
}

class Panel2 extends JPanel implements MouseMotionListener, KeyListener {

  LeftPaddle pLeft;
  Ball ball;
  RightPaddle pRight;
  int score1, score2;
  int scored1, scored2;
  int collide;
  Timer repaintTimer;
  int pwin;
  int player;

  Panel2(Game gameobj) {
    // Paddles and ball
    pLeft = new LeftPaddle();
    ball = new Ball();
    // Starts right paddle such that ball y is in the middle of the paddle
    pRight = new RightPaddle(ball.getyPos() - 25);

    player=gameobj.player;

    // audio();

    // Add listeners for frames and paddles
    addMouseMotionListener(this);
    addKeyListener(this);

    repaintTimer = new Timer(15, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        // do some stuff here, for example calculate game physics.
        // repaint actually does not repaint anything, but enqueues repaint request
        if(score1==10||score2==10)
      {
        if(score1>score2)
        {
          pwin=1;
        }
        else if(score2>score1)
        {
          pwin=2;
        }
        gameobj.endframe(pwin);
        repaintTimer.stop();
      }
      else
        repaint();
      }
    });

    repaintTimer.start();

  }

  public void audio() {
    try {
      String soundName = "level1.wav";
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
    } catch (Exception e) {
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Scores and level drawn here
    Font ft = new Font("Courier", 3, 30);
    g.setFont(ft);
    g.setColor(Color.BLACK);
    ft = new Font("Courier", 1, 50);
    g.setFont(ft);
    g.drawString("" + score1, 200, 70);
    g.drawString("" + score2, 590, 70);
    ft = new Font("Courier", 1, 20);
    g.setFont(ft);

    g.drawLine(405, 30, 405, 630);

    //Computer as opponent
    if(player!=1)
    {
     //Paddle moves with ball
     pRight.setPos(ball.getyPos() - 25);
    }

    g.fillRect(pLeft.getxPos(), pLeft.getyPos(), 10, 70);
    g.fillRect(pRight.getxPos(), pRight.getyPos(), 10, 70);

    // Ball drawn here
    g.fillOval(ball.getxPos(), ball.getyPos(), 10, 10);

    // Collision detection function
    checkCollision();
         
    // Update ball positon with set acceleration
    ball.move();

    
  }

  // Definition of MouseMotionListener functions to move the LeftPaddle
  public void mouseMoved(MouseEvent m) {
    // getY gets the mouse position and stores it in LeftPaddle
    int y = m.getY();
    // Paddle is 50 pixels in height. This sets position of move to be at middle of
    // the paddle.
    pLeft.setPos(y - 25);
  }

  public void mouseDragged(MouseEvent m) {
  }

  // Definition of KeyListener functions. Controls right paddle
  public void keyPressed(KeyEvent k) {
    int keyCode = k.getKeyCode();
    // Up
    if (keyCode == 38) {
      pRight.setPos(pRight.getyPos() - 30);
      if (pRight.getyPos() <= 0) {
        pRight.setPos(0);
      }
    }

    // Down
    else if (keyCode == 40) {
      pRight.setPos(pRight.getyPos() + 30);
      if (pRight.getyPos() >= 510) {
        pRight.setPos(510);
      }
    }
  }

  public void keyReleased(KeyEvent k) {
  }

  public void keyTyped(KeyEvent k) {
  }

  // Collision Detection
  // Checks for edges of the paddles and boundaries of the app windows
  // Sets ball acceleration to negative of its value upon collision
  // Each time ball touches left and right edge of screen
  // Score is increased and ball (color) is reset
  public void checkCollision() {
    if (ball.getyPos() <= 0 || ball.getyPos() >= 575) {
      ball.setyAcc((ball.getyAcc() * -1));
    }
    if (ball.getxPos() <= 0) {
      score2++;
      scored2++;
      collide = 0;
      ball.reset();
    }
    if (ball.getxPos() >= 800) {
      score1++;
      scored1++;
      collide = 0;
      ball.reset();
    }
    if (ball.getxPos() == 760) {
      if (pRight.getyPos() - 10 <= ball.getyPos() && pRight.getyPos() + 70 > ball.getyPos()) {
        ball.setxAcc((ball.getxAcc()) * -1);
        if (collide >= 3) {
          collide = 0;
        } else {
          collide++;
        }
      }
    }
    if (ball.getxPos() == 30) {
      if (pLeft.getyPos() - 10 <= ball.getyPos() && pLeft.getyPos() + 70 > ball.getyPos()) {
        ball.setxAcc((ball.getxAcc()) * -1);
        if (collide >= 3) {
          collide = 0;
        } else {
          collide++;
        }
      }
    }
    // checkCollision ends here
  }
}
