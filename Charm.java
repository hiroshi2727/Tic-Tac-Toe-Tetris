/* This is the GUI file of our game program. This includes
 * stage with main grid board, timer, score board, and user name. 
 * Also, it contains the timer which calculates the time of each
 * player to executes the specidied command. There is MyKeyHandler
 * class at the bottom which is used to detect user's keyboard input.
 *
 * Team name: CHARM
 * member1: Hiroshi Yurita
 * PID: A14754541
 * account name: cs11wmw
 * member2: Zhouyang Chen
 * PID: A91050968
 * account name: cs11wax
 * member3: Masaomi Tokunaga
 * PID: A15359203
 * account name: cs11wow
 * member4: Chanin Tangtartharakul
 * PID: A12297556
 * account name: cs11wfp
 */
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.input.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import java.util.*;

// This class initializes the game window and display the player's score and game window.
public class Charm extends Application{
    private int time1 = 100; //Initial time
    private int time2 = 100; //Initial time
    public int[] x; // x axis of current active grid
    public int[] y; // y axis of current active grid
    public int[] z; // value of current active grid
    private int arrayInd; // Index for active grid array
    private long prevTime1 = 0;
    private long prevTime2 = 0;
    private Random rand;
    private AnimationTimer timer1;
    private AnimationTimer timer2;
    private Label scoreNum1;
    private Label scoreNum2;
    private Label timerNum1;
    private Label timerNum2;
    private GridPane gridPane;
    private MyKeyHandler keyHandler = new MyKeyHandler(); //Create MyKeyHandler object
    private Direction direction; //Create Direction object
    private Board board; //Create Board object

    // Main method which initiazlies the game window
    public static void main(String args[]){
      launch(args);
    }

    // Start method to open the stage of javafx 
    public void start(Stage primaryStage) {

      //Initialize border pane object 
      BorderPane pane = new BorderPane();
      pane.setStyle("-fx-background-color: #DDD");
      
      //Initialize scene object
      Scene scene = new Scene(pane, 400, 690);
      scene.setOnKeyPressed(keyHandler);

      //Initialize the board object
      this.board = new Board();
      
      //Initialize scoreNum objects
      scoreNum1 = new Label();
      scoreNum2 = new Label();
      
      //Initialize timer objects
      timerNum1 = new Label();
      timerNum2 = new Label();
      
      //Int array to store active grid position and value
      x = new int[4]; 
      y = new int[4];
      z = new int[4];

      //Initialize random object
      rand = new Random();

      //Player's name field rectangles
      Rectangle r_1 = new Rectangle(100, 50, Color.GRAY);
      r_1.setStroke(Color.BLACK);
      r_1.setArcWidth(20);
      r_1.setArcHeight(20);
      Rectangle r_2 = new Rectangle(100, 50, Color.GRAY);
      r_2.setStroke(Color.BLACK);
      r_2.setArcWidth(20);
      r_2.setArcHeight(20);

      //Player's name field texts
      Text text_1 = new Text("Player 1");
      text_1.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
      text_1.setFill(Color.WHITE);
      Text text_2 = new Text("Player 2");
      text_2.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
      text_2.setFill(Color.WHITE);
      
      //Player's name filed stack panes
      StackPane stack_name1 = new StackPane(r_1,text_1);
      StackPane stack_name2 = new StackPane(r_2,text_2);
      
      //Score board rectangles
      Rectangle r_score1 = new Rectangle(80, 80, Color.CYAN);
      r_score1.setStroke(Color.BLACK);
      r_score1.setArcWidth(20);
      r_score1.setArcHeight(20);
      Rectangle r_score2 = new Rectangle(80, 80, Color.CYAN);
      r_score2.setStroke(Color.BLACK);
      r_score2.setArcWidth(20);
      r_score2.setArcHeight(20);
      
      //Score board label (set the inital score to label)
      updateScore1(board.getScore1());
      updateScore2(board.getScore2());
      
      //Score board stack panes
      StackPane stack_score1 = new StackPane(r_score1, scoreNum1);
      StackPane stack_score2 = new StackPane(r_score2, scoreNum2);
      
      //Hbox for name fields and score boards
      HBox borderTop = new HBox(10, stack_name1, stack_score1, stack_score2, stack_name2);
      borderTop.setAlignment(Pos.CENTER);
      pane.setTop(borderTop);

      //Main board (grid pane)
      gridPane = new GridPane();
      gridPane.setStyle("-fx-background-color: #DDD");
      for (int col = 0; col < 10; col++){ //Initialize the board
          for (int row = 0; row < 20; row++){
              gridPane.add(new GameTile(0, false), col, row);
          }
      }
      pane.setCenter(gridPane);
      
      //Timer rectangle
      Rectangle r_timer1 = new Rectangle(80, 80, Color.PINK);
      r_timer1.setStroke(Color.BLACK);
      r_timer1.setArcWidth(20);
      r_timer1.setArcHeight(20);
      Rectangle r_timer2 = new Rectangle(80, 80, Color.PINK);
      r_timer2.setStroke(Color.BLACK);
      r_timer2.setArcWidth(20);
      r_timer2.setArcHeight(20);
      
      //Set the initial time
      updateTimer1(time1);
      updateTimer2(time2);
      
      //Initialize AnimationTimer objects
      this.timer1 = new AnimationTimer(){
          @Override
          public void handle(long now){
              if ((now - prevTime1) >= 1000*1000*1000){
                  if (board.canMoveDown(x, y)){
                      board.moveDown(x, y, z);
                      updateBoard();
                  }
                  else{
                     System.out.println("the block is stoped"); 
                      timer1.stop();
                      timer2.start();
                      board.checkAlignment(x, y, true);
                      updateScore1(board.getScore1());
                      board.clearActiveGrid(x, y); 
                      //check if the block reaches the top
                      for (int i = 0; i < 10; i++){
                          if(board.getTileValue(i, 0) != 0){
                              if (board.getScore1() > board.getScore2()){
                                  showGameOver(1);
                              } else showGameOver(2);
                              break;
                          }
                      }
                      board.makeNewBlock(rand.nextInt(5));
                      updateBoard();
                  }
                  //Set the time so that timer proceeds by 1 sec
                  time1--;
                  updateTimer1(time1);
                  prevTime1 = now;
                  if (time1 == 0){ //Stops the timer when time is 0
                      timer1.stop();
		      try{
			  showGameOver(2);
		      }catch(Exception e){
			  System.out.println("Player2 win!");
		      }
                  }
              }
          }
      };

      //Initialize AnimationTimer object for player 2
      this.timer2 = new AnimationTimer(){
          @Override
          public void handle(long now){
              if ((now - prevTime2) >= 1000*1000*1000){
                  if (board.canMoveDown(x, y)){
                      board.moveDown(x, y, z);
                      updateBoard();
                  }
                  else{
                      System.out.println("the block is stoped"); 
                      timer1.stop();
                      timer2.stop();
                      timer1.start();
                      board.checkAlignment(x, y, false);
                      updateScore2(board.getScore2());
                      board.clearActiveGrid(x, y);
                      for (int i = 0; i < 10; i++){ //
                          if(board.getTileValue(i, 0) != 0){
                              if (board.getScore2() > board.getScore1()){
                                  showGameOver(2);
                              } else showGameOver(1);
                              break;
                          }
                      }
                      board.makeNewBlock(rand.nextInt(5));
                      updateBoard();
                      //if (board.canMoveDown() == false)
                      //    showGameOver(); 
                  }
                  //Change the time by second
                  time2--;
                  updateTimer2(time2);
                  prevTime2 = now;
                  if (time2 == 0){
                      timer2.stop();
		      try{
			  showGameOver(1);
		      }catch(Exception e){
			  System.out.println("Player1 win!");
		      }
                  }
              }
          }
      };

      //Timer stack panes
      StackPane stack_timer1 = new StackPane(r_timer1, timerNum1);
      StackPane stack_timer2 = new StackPane(r_timer2, timerNum2);

      //Timer name rectangle
      Rectangle r_next1 = new Rectangle(90, 60, Color.ORANGE);
      r_next1.setStroke(Color.BLACK);
      r_next1.setArcWidth(20);
      r_next1.setArcHeight(20);
      Rectangle r_next2 = new Rectangle(90, 60, Color.ORANGE);
      r_next2.setStroke(Color.BLACK);
      r_next2.setArcWidth(20);
      r_next2.setArcHeight(20);
      
      //Timer name texts
      Text text_next1 = new Text("Player 1's\n Time \n (second)");
      text_next1.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
      text_next1.setFill(Color.BLACK);
      Text text_next2 = new Text("Player 2's\n Time \n (second)");
      text_next2.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
      text_next2.setFill(Color.BLACK);
      
      //Timer name stack panes
      StackPane stack_next1 = new StackPane(r_next1, text_next1);
      StackPane stack_next2 = new StackPane(r_next2, text_next2);
      VBox borderRight = new VBox(30, stack_next1, stack_timer1, stack_next2, stack_timer2);
      borderRight.setAlignment(Pos.CENTER);
      pane.setRight(borderRight);

      //Make a first block and start the timer
      board.makeNewBlock(rand.nextInt(5));
      updateBoard();
      timer1.start();

      //Set up and show primaryStage
      primaryStage.setTitle("TicTacToeTeris");
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
    }
 
    //update the score of player 1
    void updateScore1(int score1){
        scoreNum1.setText(String.valueOf(score1)); 
    }

    //update the score of player 2
    void updateScore2(int score2){
        scoreNum2.setText(String.valueOf(score2));
    }

    //update the timer of player 1
    void updateTimer1(int second1){
        timerNum1.setText(String.valueOf(second1));
    }

    //update the timer of player 2
    void updateTimer2(int second2){
        timerNum2.setText(String.valueOf(second2));
    }

    //update the board to the values of current grid in board object
    private void updateBoard(){
        arrayInd = 0;
        for (int c = 0; c < 10; c++){
            for (int r = 0; r < 20; r++){
                gridPane.add(new GameTile((board.getTileValue(c, r)),
                            board.getTileStatus(c,r)), c, r);
                //Store the values of active grid
                if (board.getTileStatus(c, r)){
                    x[arrayInd] = c;
                    y[arrayInd] = r;
                    z[arrayInd] = board.getTileValue(c, r);
                    arrayInd++;
                }
            }
        }
    }

    //Show game over window
    private void showGameOver(int player){
        timer1.stop();
        timer2.stop();
        StackPane overPane = new StackPane(new Label("PLAYER " + player + " WIN"));
        Scene overScene = new Scene(overPane, 200, 100);
        Stage overStage = new Stage();
        overStage.setScene(overScene);
        overStage.show();
    }

    //This class and handle() method will accept the keyboard input
    //  and implement the command.
    class MyKeyHandler implements EventHandler<KeyEvent>{
        public void handle(KeyEvent e){
            direction = null;
            
            //If user hits LEFT
            if (e.getCode() == KeyCode.LEFT){
                direction = Direction.LEFT;
                System.out.println("Moving LEFT");
            }
            //If user hits RIGHT
            else if (e.getCode() == KeyCode.RIGHT){
                direction = Direction.RIGHT;
                System.out.println("Moving RIGHT");
            }
            //If user hits UP
            else if (e.getCode() == KeyCode.UP){
                direction = Direction.UP;
                System.out.println("Rotate");
            }
            //If user hits DOWN
            else if (e.getCode() == KeyCode.DOWN){
                direction = Direction.DOWN;
                System.out.println("Moving Down");
            }
            //Otherwise, If user hits LEFT
            else if (e.getCode() == KeyCode.A){
                direction = Direction.LEFT;
                System.out.println("Moving LEFT");
            }
            //If user hits RIGHT
            else if (e.getCode() == KeyCode.D){
                direction = Direction.RIGHT;
                System.out.println("Moving RIGHT");
            }
            //If user hits UP
            else if (e.getCode() == KeyCode.W){
                direction = Direction.UP;
                System.out.println("Rotate");
            }
            //If user hits DOWN
            else if (e.getCode() == KeyCode.S){
                direction = Direction.DOWN;
                System.out.println("Moving Down");
            }
            //Implement the command based on the input direction
            if(board.move(direction, x, y, z)){
                updateBoard();
            }
        }
    }
}
