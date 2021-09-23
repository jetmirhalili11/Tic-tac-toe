/** By: Jetmir Halili */

public class X0{
   protected String a[][];
   protected String w, won;
   protected int go=5;
   
   public X0(){
      a= new String[3][3];
   }
   
   //this method choose a random place to put a 0 (BOT player) is used in a easy level
   public int random(){
      int i=0; 
      int j=0;
      while(true){                          //while that place isn't "-" (empty)
         i=(int)(Math.random()*3);
         j=(int)(Math.random()*3);
         if(a[i][j].equals("-")){
            break;
         }
      }
      return i*10+j;
   }
   
   // this method is used in medium level. First check if he can win with only one move else check only the next move of BOT
   protected int random2(String s1, String s2){
       for(int i=0; i!=a.length; i++){
           for(int j=0; j!=a.length; j++){
               if(a[i][j].equals("-")){
                    a[i][j]=s1;
                    if(check(i,j,s1)){
                        a[i][j]="-";
                        return i*10+j;
                    }
                    a[i][j]="-";
               }
           }
           if(!s1.equals(s2) && i==a.length-1){
               s1=s2;
               i=-1;
           }
       }
       return random();
   }
   
   //check if someone win the game
   protected boolean check(int i, int j, String s){
      boolean t=false;
      if(a[i][0].equals(s) && a[i][1].equals(s) && a[i][2].equals(s)){              // cheks in i positions (-----)
         t=true;
         w="1";
      }
      else if(a[0][j].equals(s) && a[1][j].equals(s) && a[2][j].equals(s)){         // cheks in j positions (|||||||)
         t=true;
         w="0";
      }
      else if(i==j || i+j==2){                                                      // cheks in diagonal positions
            if(a[1][1].equals(s) && a[0][0].equals(s) && a[2][2].equals(s)){        // like this ( \\\\\ )
                t=true;
                w="2";
            }
            else if(a[1][1].equals(s) && a[0][2].equals(s) && a[2][0].equals(s)){   // like this ( /////// )
                t=true;
                w="3";
            }
      }
      if(t){
          won=s;
      }
      return t;
   }
   
   // check for empty place
   public boolean empty(){
      for(int i=0; i!=a.length; i++){
         for(int j=0; j!=a[i].length; j++){
            if(a[i][j].equals("-")){            // if there is only one empty place "-" return false
               return false;
            }
         }
      }
      return true;
   }
   
   // use minMax method to choose the best positions for BOT. This method is used in impossible level
   public int minMax0(String pl, String bot){
      int value=10;             // the best value for BOT is negative so the start is positive
      int value2;               // is the second positions value wich will be compared with "value" to find the best (smallest)
      int rez=0;                // the best posiotion so rez=i*10+j;
      for(int i=0; i!=a.length; i++){                     //cheks every positions one by one
         for(int j=0; j!=a[i].length; j++){               // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            if(a[i][j].equals("-")){             // if this positions is free
               a[i][j]=bot;                   
               if(check(i,j,bot)){             // cheks if BOT wins in this position and return this position
                  a[i][j]="-";
                  return i*10+j;
               }
                value2=minMax(true,pl,bot);         // cheks every next posiotions wich is X position
                a[i][j]="-";
                if(value2<value){           // gets the best value (negativ values)
                    value=value2;
                    rez=i*10+j;
               }
                else if(value2==value && ((int)(Math.random()*10))%3==0){        //if two moves have same value randomly take one
                    rez=i*10+j;
                }
            }
         }
      }
      return rez;
   }
   
   // this method is made by minMax algorith so it search in every moves the rezults and return it
   public int minMax(boolean t, String pl, String bot){
      if(empty()){                      // if there istn any place left then rezult is neutrl so 0
         return 0;
      }
      int max=-10;                     // max shound be max result so in the begining we take is as a -10
      int min=10;                      // min should be min result so in the begining we take it as a +10 
      for(int i=0; i!=a.length; i++){
         for(int j=0; j!=a[i].length; j++){
            if(a[i][j].equals("-")){             //if this place is empty
               if(t){                           //if its X turn 
                  a[i][j]=pl;            
                  if(check(i,j,pl)){           //cheks if X win then return best value for X wich is 1
                     a[i][j]="-";
                     return 1; 
                  }
                  max=Math.max(max,minMax(false,pl,bot));       //else try every next move wich is from BOT
                  a[i][j]="-";
               }
               else{                          //if its BOT turn (0)
                  a[i][j]=bot;
                  if(check(i,j,bot)){         //cheks if BOT win then return best value for BOT wich is -1
                     a[i][j]="-";
                     return -1; 
                  }
                  min=Math.min(min,minMax(true,pl,bot));    //else try every next move wich is from X
                  a[i][j]="-";
               }
            }
         }
      }
      if(t){                //if its X turn then return max value
         return max;
      }
      else{                //if its BOT (0) turn then return min value
         return min;
      }
   }
}
