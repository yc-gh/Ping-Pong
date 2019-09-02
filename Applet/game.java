//Total 9 Files in project folders
//Including source Code
//6 audio files
//1 background image
//1 html file for appletviewer


import java.io.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.applet.*;
//For BoxLayout
import javax.swing.*;

//Left Paddle Code
class LeftPaddle
{
 private int xPos=20;
 private int yPos=20;

 //Set position of left paddle
 public void setPos(int pos)
 {
   //this keyword refers to current class object
    this.yPos=pos;
 }

 //Get x position of left paddle
 public int getxPos()
 {
   return this.xPos;
 }

 //Get y position of left paddle
 public int getyPos()
 {
   return this.yPos;
 }
}

//Right Paddle Code
class RightPaddle
{
  private int xPos=770;
  private int yPos=20;

  //Sets position of right paddle same as ball y position
  public RightPaddle(int ballPos)
  {
    this.yPos=ballPos;
  }

  //Set position of right paddle
  public void setPos(int pos)
  {
     this.yPos=pos;
  }

  //Get x position of right paddle
  public int getxPos()
  {
    return this.xPos;
  }

  //Get y position of right paddle
  public int getyPos()
  {
    return this.yPos;
  }
}

//Ball Code
//Movement is changed with collision detection
class Ball
{
  private int xPos;
  private int yPos;

  //These control the acceleration of the ball
  private int dx=-10;
  private int dy=3;

  //Constructor
  //Sets initial position of ball to middle of applet window
  Ball()
  {
    setPos(400,250);
  }

  //Set position of ball
  public void setPos(int x,int y)
  {
    this.xPos=x;
    this.yPos=y;
  }

  //Get x position of ball
  public int getxPos()
  {
    return this.xPos;
  }

  //Get y position of ball
  public int getyPos()
  {
    return this.yPos;
  }

  //Set x acceleration of ball
  public void setxAcc(int x)
  {
    this.dx=x;
  }

  //Set y acceleration of ball
  public void setyAcc(int y)
  {
    this.dy=y;
  }

  //Get x acceleration of ball
  public int getxAcc()
  {
    return this.dx;
  }

  //Get y acceleration of ball
  public int getyAcc()
  {
    return this.dy;
  }

  //Controls movement of ball with a set acceleration
  public void move()
  {
    setPos(this.xPos+dx,this.yPos+dy);
  }

  //Resets the ball position and acceleration
  public void reset()
  {
    setPos(400,250);
    dx=-10;
    dy=5;
  }
}



//ACtionListener used to select buttons from Frames
//MouseMotionListener moves left paddle
//KeyListener moves right paddle in 2 player
public class game extends Applet implements MouseMotionListener,ActionListener,KeyListener
{
  //Stores y position of mouse
  public int y;
  //Stores state of game. Game runs when state is 1
  public int state;
  //Stores scores of both players
  public int score1,score2;
  //Used to check if 2 player mode is selected
  public int player;
  //Used to change color of ball each time it collides with paddles
  public int collide;
  //Stores game level
  public int level;
  //Used to print which player won
  public int pwin;
  //Used to check if a player scored
  public int scored1,scored2;

  //AudioClip class from java.awt package
  //Used to load AudioClips into the applet
  //Audio clips
  AudioClip mainmenu,level1,levelchange,level2,level3,gameend;

  //Buffering is used to prevent screen flickers. Draws all objects on an offscreen image first, then it is drawn to screen.
  Graphics bg,g;
  Image offscreen;

  //Instantiating paddles and ball
  LeftPaddle pLeft=new LeftPaddle();
  Ball ball=new Ball();
  //Starts right paddle such that ball y is in the middle of the paddle
  RightPaddle pRight=new RightPaddle(ball.getyPos()-25);

  //Frame Components. One each for starting frame and end frame.
  Frame f1,f2,f3;
  Panel p2,p3;
  Button b1,b2,b3,b4,b5;
  Label l1,l2,l3,l4,l5;
  GridLayout gl;
  Font ft;

  Image img;


  //Main menu frame
  //No seperate frame used here
  //Background image is drawn on window
  //Controls drawn over the background image
  //controlname.setMaximumSize(Dimension) sets maximum size of control
  //controlname.setBounds(Rectangle dimensions) sets location where control is drawn
  //setLayout(null) is required to manually position controls
  //Contains three buttons: Computer,Player,Instructions
  public void mainframe()
  {
    b1=new Button("Computer");
    b1.addActionListener(this);
    b1.setMaximumSize(new Dimension(350,30));
    b2=new Button("Player");
    b2.addActionListener(this);
    b2.setMaximumSize(new Dimension(350,30));
    b3=new Button("Instructions");
    b3.addActionListener(this);
    b3.setMaximumSize(new Dimension(350,30));
    ft=new Font("Courier",1,20);
    b1.setFont(ft);
    b2.setFont(ft);
    b3.setFont(ft);
    add(b1);
    add(b2);
    add(b3);
    b1.setBounds(new Rectangle(300,380,200,30));
    setLayout(null);
    b2.setBounds(new Rectangle(300,420,200,30));
    setLayout(null);
    b3.setBounds(new Rectangle(300,460,200,30));
    setLayout(null);
  }

  //End frame when level reaches 4
  //Shows winner and restart button
  public void endframe()
  {
    f2=new Frame();
    p2=new Panel();
    p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));
    p2.setBackground(Color.BLACK);
    if(player!=1)
    {
      ft=new Font("Courier",1,50);
      l1=new Label("Computer Wins!");
      l1.setFont(ft);
      l1.setAlignment(Label.CENTER);
    }
    else
    {
      ft=new Font("Courier",1,50);
      l1=new Label("Player   "+ pwin+ "  Wins!");
      l1.setFont(ft);
      l1.setAlignment(Label.CENTER);
    }
    b5=new Button("Restart");
    b5.addActionListener(this);
    b5.setMaximumSize(new Dimension(350,40));
    ft=new Font("Courier",1,20);
    l1.setForeground(Color.WHITE);
    b5.setFont(ft);
    f2.add(p2);
    p2.add(l1);
    p2.add(b5);
    p2.add(Box.createVerticalStrut(20));
    f2.setSize(820,580);
    f2.setTitle("End Screen");
    f2.setVisible(false);
  }

  //Instructions frame
  //Shows instructions and ok button
  public void instructions()
  {
    f3=new Frame();
    p3=new Panel();
    p3.setLayout(new BoxLayout(p3,BoxLayout.Y_AXIS));
    p3.setBackground(Color.BLACK);
    l2=new Label("* Use mouse to control player 1");
    l3=new Label("* Use up and down keys to control player 2");
    l4=new Label("* Difficulty increases at level 5 and 10");
    l5=new Label("* Game speed increases with levels");
    b4=new Button("Ok");
    b4.setMaximumSize(new Dimension(350,70));
    b4.addActionListener(this);
    ft=new Font("Courier",1,22);
    l2.setFont(ft);
    l3.setFont(ft);
    l4.setFont(ft);
    l5.setFont(ft);
    l2.setForeground(Color.WHITE);
    l3.setForeground(Color.WHITE);
    l4.setForeground(Color.WHITE);
    l5.setForeground(Color.WHITE);
    l2.setAlignment(Label.CENTER);
    l3.setAlignment(Label.CENTER);
    l4.setAlignment(Label.CENTER);
    l5.setAlignment(Label.CENTER);
    b4.setFont(ft);
    f3.add(p3);
    p3.add(l2);
    p3.add(l3);
    p3.add(l4);
    p3.add(l5);
    p3.add(Box.createVerticalStrut(120));
    p3.add(b4);
    l2.setMaximumSize(new Dimension(800,50));
    l3.setMaximumSize(new Dimension(800,50));
    l4.setMaximumSize(new Dimension(800,50));
    l5.setMaximumSize(new Dimension(800,50));
    f3.setSize(820,580);
    f3.setTitle("Instructions");
    f3.setVisible(false);
  }

  //Initialization of applet
  public void init()
  {
    level=1;
    score1=0;
    score2=0;

    //Toolkit class provides getDefaultToolkit() function
    //getDefaultToolkit() can load image from a file (provides other functionality as well)
    //Loads image from file into variable of data type Image
    img = Toolkit.getDefaultToolkit().createImage("pong.png");


    //Loads audio files when
    //getCodebase() establishes a path to other files or folders that are in the same location as the class being run
    //audioclipname.play() plays the clip
    //audioclipname.stop() stops the clip
    mainmenu=getAudioClip(getCodeBase(), "mainmenu.wav");
    level1=getAudioClip(getCodeBase(), "level1.wav");
    levelchange=getAudioClip(getCodeBase(), "levelchange.wav");
    level2=getAudioClip(getCodeBase(), "level2.wav");
    level3=getAudioClip(getCodeBase(), "level3.wav");
    gameend=getAudioClip(getCodeBase(), "gameend.wav");
    mainmenu.play();


    //Loads all Frames
    //Only mainframe is visible at applet start
    mainframe();
    instructions();
    endframe();


    //Add listeners for frames and paddles
    addMouseMotionListener(this);
    addKeyListener(this);

    //Offscreen image where buffer graphics are stored
    offscreen = createImage(800,500);
    bg = offscreen.getGraphics();
  }

    //All graphical drawing done here
     public void paint(Graphics g)
    {
      //Show endframe when score sum reaches 15
      if(level==4)
      {
        if(score1>score2)
        {
          pwin=1;
        }
        if(score2>score1)
        {
          pwin=2;
        }
        level=1;
        state=0;
        score1=0;
        score2=0;
        endframe();
        gameend.play();
        f2.setVisible(true);
        b4.addActionListener(this);
      }

      //Background image for main menu drawn here
      if(state==0)
      {
        g.drawImage(img,0,0,800,500,this);
      }
      //State is set to 1 only after user selects opponent and when game is restarted.
      else if(state==1)
      {
        try
       {
         //Computer as opponent
         if(player!=1)
         {
          //Paddle moves with ball
          pRight.setPos(ball.getyPos() - 25);
         }

         //Call to collision detection function
         checkCollision();

         //Clear any previously drawn images
         bg.clearRect(0,0,800,500);

         //setBackground() only works in init and only once when applet is started
         //Instead we draw a rectangle across the entire applet window
         //All graphical drawings are done after this rectangle is drawn so that it looks like a background color
         if(level==1)
         {
           bg.setColor(Color.GREEN);
           bg.fillRect(0,0,800,500);
           bg.setColor(Color.BLUE);
         }
         else if(level==2)
         {
           bg.setColor(Color.GRAY);
           bg.fillRect(0,0,800,500);
           bg.setColor(Color.WHITE);
         }
         else if(level==3)
         {
           bg.setColor(Color.BLUE);
           bg.fillRect(0,0,800,500);
           bg.setColor(Color.GREEN);
         }

         //Scores and level drawn here
         ft = new Font("Courier",3,30);
         bg.setFont(ft);
         bg.setColor(Color.BLACK);
         bg.drawString("LEVEL "+level,350,25);
         ft = new Font("Courier",1,50);
         bg.setFont(ft);
         bg.drawString(""+score1,200,70);
         bg.drawString(""+score2,590,70);
         ft = new Font("Courier",1,20);
         bg.setFont(ft);

         bg.drawLine(405,30,405,500);

         //Paddles drawn here
         if(level==3)
         {
            bg.setColor(Color.GREEN);
         }
         else
         {
           bg.setColor(Color.BLUE);
         }
         bg.fillRect(pLeft.getxPos(),pLeft.getyPos(),10,70);
         bg.fillRect(pRight.getxPos(),pRight.getyPos(),10,70);

         //Change ball color with each collision
         if(collide==0)
         {
          bg.setColor(Color.BLACK);
         }
         else if(collide==1)
         {
          bg.setColor(Color.RED);
         }
         else if(collide==2)
         {
          bg.setColor(Color.WHITE);
         }
         else
         {
          bg.setColor(Color.MAGENTA);
         }

         //Checks and shows who scored
         if(scored1==1)
         {
           bg.drawString("PLAYER 1 SCORES!",130,400);
           g.drawImage(offscreen,0,0,this);
           try
           {
           Thread.sleep(2000);
           }
           catch(Exception ex)
           {}
           scored1=0;
           repaint();
         }
         else if(scored2==1)
         {
           if(player!=1)
           {
             bg.drawString("COMPUTER SCORES!",500,400);
             g.drawImage(offscreen,0,0,this);
           }
           else
           {
             bg.drawString("PLAYER 2 SCORES!",500,400);
             g.drawImage(offscreen,0,0,this);
           }
           try
           {
           Thread.sleep(2000);
           }
           catch(Exception ex)
           {}
           scored2=0;
           repaint();
         }

         //Ball drawn here
         bg.fillOval(ball.getxPos(),ball.getyPos(),10,10);

         //Update ball positon with set acceleration
         ball.move();

         //Draw offscreen image to applet window
         g.drawImage(offscreen,0,0,this);

         //Changes level
         //Changes music
         //Level changes when score reaches 5/10/15 for any side
         //Cannot use modulus function to increment level
         //Since this code will run multiple times at each score
         //Level will increment to 4 before any side scores more than 15
         //Game ends at level 4
         if((score1==5||score2==5)&&level==1)
         {
           level=2;
           level1.stop();
           levelchange.play();
           try
           {
             g.clearRect(0,0,800,500);
             g.setColor(Color.DARK_GRAY);
             g.fillRect(0,0,800,500);
             g.setColor(Color.WHITE);
             ft = new Font("Courier",1,100);
             g.setFont(ft);
             g.drawString("LEVEL 2",190,250);
             Thread.sleep(4000);
             level2.play();
           }
           catch(Exception e)
           {}
         }
         else if((score1==10||score2==10)&&level==2)
         {
           level=3;
           level2.stop();
           levelchange.play();
           try
           {
             g.clearRect(0,0,800,500);
             g.setColor(Color.BLUE);
             g.fillRect(0,0,800,500);
             g.setColor(Color.WHITE);
             ft = new Font("Courier",1,100);
             g.setFont(ft);
             g.drawString("LEVEL 3",190,250);
             Thread.sleep(4000);
             level3.play();
           }
           catch(Exception e)
           {}
         }
         else if((score1>=15||score2>=15))
         {
           level3.stop();
           level=4;
         }

         //Controls speed of game. Increases every 5 levels
         if(level==1)
         {
           Thread.sleep(20);
         }
         if(level==2)
         {
           Thread.sleep(7);
         }
         else if(level==3)
         {
           Thread.sleep(2);
         }
       }
       catch(Exception ex)
       {}
      }
      repaint();
    }


   //Necessary to further clear any flickers along with buffering
   public void update(Graphics g)
   {
     paint(g);
   }


   //Definition of ActionListener functions
   public void actionPerformed(ActionEvent ae)
   {

    //Select Computer
    if(ae.getSource()==b1)
    {
      mainmenu.stop();
      level1.play();
      state=1;
      remove(b1);
      remove(b2);
      remove(b3);
      paint(g);
    }

    //Select Player
    else if(ae.getSource()==b2)
    {
      mainmenu.stop();
      level1.play();
      player=1;
      state=1;
      remove(b1);
      remove(b2);
      remove(b3);
      paint(g);
    }

    //Instructions open
    else if(ae.getSource()==b3)
    {
      f3.setVisible(true);
    }

    //Instructions close
    else if(ae.getSource()==b4)
    {
      f3.setVisible(false);
    }

    //End Frame
    else if(ae.getSource()==b5)
    {
      gameend.stop();
      player=0;
      f2.setVisible(false);
      mainframe();
      mainmenu.play();
    }
   }


   //Definition of MouseMotionListener functions to move the LeftPaddle
   public void mouseMoved(MouseEvent m)
   {
     //getY gets the mouse position and stores it in LeftPaddle
     y=m.getY();
     // Paddle is 50 pixels in height. This sets position of move to be at middle of the paddle.
     pLeft.setPos(y-25);
   }
   public void mouseDragged(MouseEvent m)
   {}


   //Definition of KeyListener functions. Controls right paddle
   public void keyPressed(KeyEvent k)
   {
     int keyCode=k.getKeyCode();

     //Up
     if(keyCode==38)
     {
       pRight.setPos(pRight.getyPos()-30);
       if(pRight.getyPos()<=0)
       {
         pRight.setPos(0);
       }
     }

     //Down
     else if(keyCode==40)
     {
       pRight.setPos(pRight.getyPos()+30);
       if(pRight.getyPos()>=440)
       {
         pRight.setPos(440);
       }
     }
   }
   public void keyReleased(KeyEvent k)
   {}
   public void keyTyped(KeyEvent k)
   {}


   //Collision Detection
   //Checks for edges of the paddles and boundaries of the applet windows
   //Sets ball acceleration to negative of its value upon collision
   //Each time ball touches left and right edge of screen
   //Score is increased and ball (color) is reset
   public void checkCollision()
    {
     if(ball.getyPos()<=0||ball.getyPos()>=490)
     {
       ball.setyAcc((ball.getyAcc()*-1));
     }
     if(ball.getxPos()<=0)
     {
       score2++;
       scored2++;
       collide=0;
       ball.reset();
     }
     if(ball.getxPos()>=800)
     {
       score1++;
       scored1++;
       collide=0;
       ball.reset();
     }
     if(ball.getxPos()==760)
     {
       if(pRight.getyPos()-10<=ball.getyPos() && pRight.getyPos()+70>ball.getyPos())
      {
       ball.setxAcc((ball.getxAcc())*-1);
       if(collide>=3)
       {
         collide=0;
       }
       else
       {
       collide++;
       }
      }
     }
     if(ball.getxPos()==30)
     {
       if(pLeft.getyPos()-10<=ball.getyPos() && pLeft.getyPos()+70>ball.getyPos())
       {
         ball.setxAcc((ball.getxAcc())*-1);
         if(collide>=3)
         {
           collide=0;
         }
         else
         {
         collide++;
         }
        }
       }
    //checkCollision ends here
    }
  //game class ends here
 }
