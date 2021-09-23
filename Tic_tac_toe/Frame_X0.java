import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;

public class Frame_X0 extends JPanel implements KeyListener{
   private final JFrame f;
   private double scaleW, scaleH;
   private Image winI, field;
   private int xLine=0, yLine=0;
   private final Image[][] b;
   private final X0 m;
   private int ii=-1, jj=-1, xx=0, yy=0, xwin=0, owin=0, turn=1, round=0;
   private String pl="X", bot="0", level="easy";
           
   public Frame_X0(){
      scaleW= scaleH= 1;
      b = new Image[3][3];
      field= getImages("open.png");
      m= new X0();
      
      this.setPreferredSize(new Dimension(350,400));
      addComponent();

      // this part deal with JFrame
      f= new JFrame("Tic-Tac-Toe (LG)");
      f.addKeyListener(this);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.add(this);
      f.pack();
      f.setMinimumSize(new Dimension(f.getWidth(),f.getHeight()));
      f.setLocationRelativeTo(null);
   
      f.setVisible(true);
   }
   
   // this method deals with the case when changing the size of the Graphic window to change that of the buttons
   private void addComponent(){
      addComponentListener(
         new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
               scaleW= getWidth()/350.0;
               scaleH= getHeight()/400.0;
               repaint();
            }
         });
         
      // this part put a mouseAction in JPanel
      this.addMouseListener(
         new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
               mouseAction(evt);
            }
         });
         
   }
   
   // this method paints graphis is JPanel
   public void paint(Graphics gr){
      Graphics2D g= (Graphics2D)gr;
      g.scale(scaleW,scaleH);
      
      g.setColor(new Color(6,211,255));                        //draw a rectangle in the bottom of JPanel
      g.fillRect(0,350,350,50);
      g.drawImage(field, 0, 0, this);                      //draw field of the game 
      if(m.go==10){
         paintOptions(g);
      }
      if(m.go!=5 && m.go!=10){
         paintScore(g); 
      }
          
      for(int i=0; i!=b.length; i++){
         for(int j=0; j!=b[i].length; j++){
            if(i!=ii || j!=jj){                                                 // if this X or 0 must paint regulary
               g.drawImage(b[i][j], 30+i*100, 30+j*100, 90, 90, this);          
            }
            else{                                                               // if this X or 0 must paint as an animacion 
               if(m.a[i][j].equals(pl)){
                  g.drawImage(b[i][j], 30+i*100, 30+j*100, xx, 90, this);                        
               }
               else{
                  g.drawImage(b[i][j], 30+i*100, 30+j*100, 90, xx, this);
               }
               animacion(i,j,m.a[i][j]);                                     
               repaint();                       // repaint JPanel
            }
         }
      }
      
      if(ii==-2){
         g.drawImage(winI, xLine, yLine, xx, yy, this);
         animacion2(winI.getWidth(this),winI.getHeight(this));
      }
   }
   
   // thi method paints the score and the turn of players
   public void paintScore(Graphics g){
      if(m.go==1 || m.go==0){
         g.setColor(new Color(2,152,242));
         g.fillRect(20+(m.go+turn)%2*190,335,120,46);
         g.setColor(new Color(6,211,255));
         g.fillRect(25+(m.go+turn)%2*190,340,110,36);
      }
      g.setColor(Color.BLACK);
      g.setFont(new Font("Imprint MT Shadow",Font.PLAIN,26));
      g.drawString(("X          ").substring(0,("X          ").length()-(xwin+"").length())+xwin, 30, 368);
      g.setColor(Color.WHITE);
      g.drawString(("O          ").substring(0,("O          ").length()-(owin+"").length())+owin, 220, 368);
   }
   
   // this method paints the options of game
   public void paintOptions(Graphics2D g){
      int x=2;
      int y=150;
      if(level.equals("medium")){
         x=78;
         y=148;
      }
      else if(level.equals("impossible")){
         x=198;
         y=149;
      }
      g.drawImage(getImages(level+".png"), x, y, this);
      x=28;
      y=260;
      if(bot.equals("X")){
         x=249;
         y=257;
      }
      g.drawImage(getImages(pl+"-o.png"), x, y, this);
   }
   
   // this method deal with animacions and the actions after animacion ends
   // animation when a player or a BOT put an 'X' or 'O'
   public void animacion(int i, int j, String s){
      xx++;
      stop(3);
      if(xx==91){                        // if animacion ends
         xx=0;
         ii=jj=-1;
         if(m.check(i,j,s)){            // checks for a winner
            winner(i,j);
         }
         else if(m.empty()){       // if there isnt left any place to play
            ii=-2;
            end("draw.png");
         }
         else if(s.equals(pl)){                 // if turn X has ended
            stop(500);
            PCturn();
         }
         else{                                 // if turn 0 has ended
            m.go=1;
         }
      }
   }
   
   // this method is called after a game has ended and take as a parameter which player has won the game (x, 0, draw)
   public void end(String s){
      xLine=yLine=0;
      winI= getImages(s);
      xx=yy=0;
      m.go=-1;
   }   

   // this method deal with animacions when someone has won
   public void animacion2(int width, int height){
      if(xx<width){
         xx++;
      }
      if(yy<height){
         yy++;
      }
      stop(3);
      if(xx==width && yy==height){
         if(m.go==0){
            stop(500);
            end(m.won+"winner.png");
         }
         else{
            m.go=2; 
         }
      }
      repaint();
   }
   
   // this mehod stops the code for "time" miliseconds
   public void stop(int time){
      try{
         Thread.sleep(time);
      }
      catch(InterruptedException e){}
   }
   
   // this method use minMax0 method from X0.class to find the perfect position for BOT player
   public void PCturn(){
      int x;
      if(level.equals("impossible")){
         x= m.minMax0(pl,bot);
      }
      else if(level.equals("medium")){
         x= m.random2(bot, pl);
      }
      else{
         x= m.random();
      }
      m.a[x/10][x%10]=bot;
      b[x/10][x%10]=getImages(bot+".png");
      ii=x/10;
      jj=x%10;
   }
   
   // this method choose the x,y positions for the winner line
   public void winner(int i, int j){
      ii=-2;
      winI=getImages("line"+m.w+".png");
      if(m.won.equals("X")){
         xwin++;
      }
      else{
         owin++;
      }
      if(m.w.equals("0")){                       // if line is like this -----------
         xLine=0;
         yLine=30+100*j;
      }
      else if(m.w.equals("1")){                 // if line is like this ||||||||||||
         xLine=30+100*i;
         yLine=0;
      }
   }
   
   // this method return an image by name that you put as a parameter
   public Image getImages(String name){
      ImageIcon i = new ImageIcon(getClass().getResource("Images//"+name));
      return i.getImage();
   }
   
   // this method is used after every round to blank every place of the game
   public void start(){
      for(int i=0; i!=b.length; i++){
         for(int j=0; j!=b[i].length; j++){
            b[i][j]=null;
            m.a[i][j]="-";
         }
      }
      winI=null;
      m.go=round%2;
   } 
   
   // this method deal with moseClick in JPanel
   public void mouseAction(MouseEvent e){
      if(m.go==1){                                   // if its player turn
         for(int i=0; i!=b.length; i++){
            for(int j=0; j!=b[i].length; j++){            // check if mouse click is in any of places where we can put a X
               if(m.a[i][j].equals("-") && e.getX()>=(30+i*100)*scaleW && e.getX()<=(30+i*100+90)*scaleW && e.getY()>=(30+j*100)*scaleH && e.getY()<=(30+j*100+90)*scaleH){
                  m.go=0;
                  b[i][j]=getImages(pl+".png");
                  m.a[i][j]=pl;
                  ii=i;
                  jj=j;
                  repaint();
               }
            }
         }
      }
      // if the roud has ended (player has won, lose or draw)
      else if(m.go==2){
         endRound();
         repaint();
      }
      // when user start a game
      else if(m.go==5 && e.getX()>=40*scaleW && e.getX()<=135*scaleW && e.getY()>=315*scaleH && e.getY()<=370*scaleH){
         endRound();
         field=getImages("field.png");
         xwin=owin=0;
         repaint();
      }
      // when user choose options
      else if(m.go==5 && e.getX()>=170*scaleW && e.getX()<=315*scaleW && e.getY()>=315*scaleH && e.getY()<=370*scaleH){
         field=getImages("options.png");
         m.go=10;
         repaint();
      }
      // when user exit options
      else if(m.go==10 && e.getX()>=125*scaleW && e.getX()<=210*scaleW && e.getY()>=335*scaleH && e.getY()<=375*scaleH){
         m.go=5;
         field=getImages("open.png");
         repaint();
      }
      // when user press easy level
      else if(m.go==10 && e.getX()>=5*scaleW && e.getX()<=70*scaleW && e.getY()>=160*scaleH && e.getY()<=190*scaleH){
         level="easy";
         repaint();
      }
      // when user press medium level
      else if(m.go==10 && e.getX()>=85*scaleW && e.getX()<=195*scaleW && e.getY()>=160*scaleH && e.getY()<=190*scaleH){
         level="medium";
         repaint();            
      }        
      // when user press impossible level
      else if(m.go==10 && e.getX()>=205*scaleW && e.getX()<=341*scaleW && e.getY()>=160*scaleH && e.getY()<=190*scaleH){
         level="impossible";
         repaint();            
      }       
      // when user press 'X' button (wants to be 'X') 
      else if(m.go==10 && e.getX()>=40*scaleW && e.getX()<=85*scaleW && e.getY()>=270*scaleH && e.getY()<=315*scaleH){
         pl="X";
         bot="0";
         turn=1;
         repaint();            
      } 
      // when user press 'O' button (wants to be 'O')        
      else if(m.go==10 && e.getX()>=255*scaleW && e.getX()<=305*scaleW && e.getY()>=270*scaleH && e.getY()<=315*scaleH){
         pl="0";
         bot="X";
         turn=0;
         repaint();            
      }
   }
   
   // this method is user to end a round
   private void endRound(){
      round++;
      start();
      if(m.go==0){
         PCturn();
      }
      else{
         ii=-1;
      }
      xx=yy=0;
   }

   public void keyTyped(KeyEvent e){} 
   
   // this method put action to quit the game when user press 'Q' key
   public void keyPressed(KeyEvent e){
      int c = e.getKeyCode();
      if(c==KeyEvent.VK_Q){
         xwin=owin=0;
         field=getImages("open.png");
         round=0;
         endRound();
         m.go=5;
         repaint();
      }
   }

   public void keyReleased(KeyEvent e){}
   
   public static void main(String [] args){
      new Frame_X0();
   }
}
