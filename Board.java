// This file contains the background system with some important values 
// such as score and grid which will be referenced by Charm class object.

import java.util.*;

//This class is the basic background system.
public class Board{
    private int score1; //current score of player 1
    private int score2; //current score of player 2
    private int count;
    private int blockType;
    private int preValue;
    private int colorNum = 3; //NUMBER OF BLOCK COLOR
    private int[][] grid; //grid value
    private int[] temp;
    private boolean[][] gridStatus;
    private Random random;
    
    public Board(){ 
        this.random = new Random();
        this.score1 = 0;
        this.score2 = 0;
        this.grid = new int[10][20];
        this.temp = new int[4];
        this.gridStatus = new boolean[10][20];
    }

    //return the value of particular tile 
    public int getTileValue(int column, int row){
        return this.grid[column][row];
    }

    //return the value of particular tile status
    public boolean getTileStatus(int column, int row){
        return this.gridStatus[column][row];
    }

    //return the current score of player 1
    public int getScore1(){
        return this.score1;
    }

    //return the current score of player 2
    public int getScore2(){
        return this.score2;
    }

    //make a new block on top
    public void makeNewBlock(int typeOfBlock){
        this.blockType = typeOfBlock;
        //SQUARE 
        //   00  
        //   00 
        if (typeOfBlock == 0){
            for (int c = 4; c <= 5; c++){
                for (int r = 0; r <= 1; r++){
                    grid[c][r] = random.nextInt(colorNum) + 1;
                    gridStatus[c][r] = true;
                }
            }
        }
        //Long one
        //  0000
        else if (typeOfBlock == 1){
            for (int c = 3; c <= 6; c++){
                grid[c][0] = random.nextInt(colorNum) + 1;
                gridStatus[c][0] = true;
            }
        }
        //Mountain shape
        //   0
        //  000
        else if (typeOfBlock == 2){
            grid[5][0] = random.nextInt(colorNum) + 1;
            gridStatus[5][0] = true;
            for (int c = 4; c <= 6; c++){
                grid[c][1] = random.nextInt(colorNum) + 1;
                gridStatus[c][1] = true;
            }
        }
        //7-shape
        //    0
        //  000
        else if (typeOfBlock == 3){
            grid[4][0] = random.nextInt(colorNum) + 1;
            gridStatus[4][0] = true;
            for (int c = 2; c <= 4; c++){
                grid[c][1] = random.nextInt(colorNum) + 1;
                gridStatus[c][1] = true;
            }
        }
        //Z(zet)-shape
        //  00
        //   00
        else if (typeOfBlock == 4){
            grid[4][0] = random.nextInt(colorNum) + 1;
            grid[5][0] = random.nextInt(colorNum) + 1;
            grid[5][1] = random.nextInt(colorNum) + 1;
            grid[6][1] = random.nextInt(colorNum) + 1;
            gridStatus[4][0] = true;
            gridStatus[5][0] = true;
            gridStatus[5][1] = true;
            gridStatus[6][1] = true;
        }
        /*
         * These two objects below can be used when necessary.
        //Inverse 7(seven)-shape 
        //  000
        //    0
        else if (typeOfBlock == 5){
            for (int c = 3; c <= 5; c++){
                grid[c][0] = random.nextInt(colorNum) + 1;
                gridStatus[c][0] = true;
            }
            grid[5][1] = random.nextInt(colorNum) + 1;
            gridStatus[5][1] = true;
        }
        //Inverse Z(zet)-shape
        //   00
        //  00
        else if (typeOfBlock == 6){
            grid[6][0] = random.nextInt(colorNum) + 1;
            grid[5][0] = random.nextInt(colorNum) + 1;
            grid[4][1] = random.nextInt(colorNum) + 1;
            grid[5][1] = random.nextInt(colorNum) + 1;
            gridStatus[6][0] = true;
            gridStatus[5][0] = true;
            gridStatus[4][1] = true;
            gridStatus[5][1] = true;
        }
        */
        else System.out.println("Wrong patameter on makeNewBlock()");
    }
    
    //Make all grid status to non-active. This method is called before making a new block.
    public void clearActiveGrid(int[] x, int[] y){
        for (int i = 0; i < 4; i++){
                gridStatus[x[i]][y[i]] = false;
        }
    }

    //This method checks if there is a row or column which has the same color 
    //  over more than 3 consective grids. And add points if there was.
    public void checkAlignment(int[] x, int[] y, boolean turnOfOne){
        System.out.println("checkAlignment() is called.");
        preValue = 0;
        count = 1; //counter of the same consective color tile
        
        for (int i = 0; i < 10; i++){
            preValue = 0;
            count = 1;
             for (int j = 0; j < 20; j++){
                 if (j == 19 && grid[i][j] == preValue && count >= 2){ //when on the bottom of board
                     getPoint(preValue, count + 1, turnOfOne);
                     for (int k = 0; k <= count; k++){
                             grid[i][j - k] = 0;
                     }
                 } else if (grid[i][j] == 0){   //on white tile
                     if (count >= 3){ //when the consective ends
                         System.out.println("count is more than 3");
                         getPoint(preValue, count, turnOfOne);
                         preValue = grid[i][j];
                         for (int k = 0; k < count; k++){
                             grid[i][j - 1 - k] = 0;
                         }
                         count = 1;
                     } else{         //when no consective
                         preValue = 0;
                         count = 1;
                     }
                 } else{ //on colored tile
                     if (grid[i][j] == preValue){ //on consective color
                         count++;
                     } else{ //on different color from previous one
                         if (count >= 3){ //where more than 3 consective color                              
                             System.out.println("count is more than 3 with non-white");
                             getPoint(preValue, count, turnOfOne);
                             preValue = grid[i][j];
                             for (int k = 0; k < count; k++){
                                grid[i][j - 1 - k] = 0;
                             }
                             count = 1;
                         } else{          //where less than 3 consective color
                             preValue = grid[i][j];
                             count = 1;
                         }
                     }
                 }
             }
        }
        for (int j = 0; j < 20; j++){
            preValue = 0;
            count = 1;
             for (int i = 0; i < 10; i++){
                 if (j == 19 && grid[i][j] == preValue && count >= 2){ //when on the bottom of board
                     getPoint(preValue, count + 1, turnOfOne);
                     for (int k = 0; k <= count; k++){
                             grid[i - k][j] = 0;
                     }
                 } else if (grid[i][j] == 0){   //on white tile
                     if (count >= 3){ //when the consective ends
                         System.out.println("count is more than 3");
                         getPoint(preValue, count, turnOfOne);
                         preValue = grid[i][j];
                         for (int k = 0; k < count; k++){
                             grid[i - 1 - k][j] = 0;
                         }
                         count = 1;
                     } else{         //when no consective
                         preValue = 0;
                         count = 1;
                     }
                 } else{ //on colored tile
                     if (grid[i][j] == preValue){ //on consective color
                         count++;
                     } else{ //on different color from previous one
                         if (count >= 3){ //where more than 3 consective color                              
                             System.out.println("count is more than 3 with non-white");
                             getPoint(preValue, count, turnOfOne);
                             preValue = grid[i][j];
                             for (int k = 0; k < count; k++){
                                grid[i - 1 - k][j] = 0;
                             }
                             count = 1;
                         } else{          //where less than 3 consective color
                             preValue = grid[i][j];
                             count = 1;
                         }
                     }
                 }
             }
        }
    }

    //This method adds points to player's score.
    public void getPoint(int color, int numberOfBlock, boolean turnOfOne){
        System.out.println("getPoint() is called.");
        if (turnOfOne){
            this.score1 += 10 * color + numberOfBlock;
        } else this.score2 += 10 * color + numberOfBlock;
    }

    //Check if the player can move the block.
    //This method is called everytime each player try to move the block.
    public boolean canMove(Direction direction, int[] x, int[] y, int[] z){
        if (direction == Direction.LEFT){
            return canMoveLeft(x, y);
        } else if (direction == Direction.RIGHT){
            return canMoveRight(x, y);
        } else if (direction == Direction.UP){
            return canRotate(x, y, z);
        } else if (direction == Direction.DOWN){
            return canMoveDown(x, y);
        }
        else{
            return false;
        }
    }

    //check if the player can move the block to the left
    private boolean canMoveLeft(int[] x, int[] y){
        count = 0;
        for (int i = 0; i < 4; i++){
            if (x[i] == 0){
                return false;
            }else if ((gridStatus[x[i] - 1][y[i]]) || (grid[x[i] - 1][y[i]] == 0)){
                count++;
                if (count == 4){
                    return true;
                }
            }
        }
        return false;
    }

    //check if the player can move the block to the right
    private boolean canMoveRight(int x[], int y[]){
        count = 0;
        for (int i = 0; i < 4; i++){
            if (x[i] == 9){
                return false;
            }else if ((gridStatus[x[i] + 1][y[i]]) || (grid[x[i] + 1][y[i]] == 0)){      
                count++;
                if (count == 4){
                    return true;
                }
            }
        }
        return false;
    }

    //Rotate the block
    private boolean canRotate(int[] x, int[] y, int[] z){
        count = 0;
        if (this.blockType == 0){
            return true;
        }
        else if (this.blockType == 1){
            if (y[0] == y[1]){
                for (int i = 0; i < 4; i++){
                    if (i == 2){
                        continue;
                    }
                    else if (grid[x[2]][y[i] + 2 - i] == 0 &&
                            gridStatus[x[2]][y[i] + 2 - i] == false){
                        count++;
                        if (count == 3){
                            return true;
                        }
                    }
                }
            }
            else{
                for (int i = 0; i < 4; i++){
                    if (i == 2){
                        continue;
                    }
                    else if (grid[x[i] - 2 + i][y[2]] == 0 ||
                            gridStatus[x[i] - 2 + i][y[2]] == false){
                        count++;
                        if (count == 3){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        else if (this.blockType == 2){
            if (y[0] == y[2]){
                if (y[2] == y[3]){ //type A-1
                    if(grid[x[2]][y[2] + 1] == 0 && gridStatus[x[2]][y[2] + 1] == false){
                        return true;
                    }
                }
                else{ // type A-2
                    if(grid[x[2] + 1][y[2]] == 0 && gridStatus[x[2] + 1][y[2]] == false){
                        return true;
                    }
                }
            }
            else{ //type B-1
                if (x[0] == x[1]){
                    if(grid[x[1] - 1][y[1]] == 0 && gridStatus[x[1] - 1][y[1]] == false){
                        return true;
                    }
                }
                else{ //type B-2
                    if(grid[x[1]][y[1] - 1] == 0 && gridStatus[x[1]][y[1] - 1] == false){
                        return true;
                    }
                }
            }
            return false;
        }
        else if (this.blockType == 3){
            if (x[1] == x[2]){ //A
                if (y[2] == y[3]){//A-1
                    if(grid[x[1] - 1][y[1]] == 0 && grid[x[2] - 1][y[2]] == 0 && 
                            grid[x[2] + 1][y[2]] == 0 && gridStatus[x[1] - 1][y[1]] == false
                            && gridStatus[x[2] - 1][y[2]] == false && gridStatus[x[2] + 1][y[2]]== false)
                        return true;
                }
                else{//A-2
                    if(grid[x[2] - 2][y[2]] == 0 && grid[x[2] - 1][y[2]] == 0 && 
                            grid[x[3] - 2][y[3]] == 0 && gridStatus[x[2] - 2][y[2]] == false
                            && gridStatus[x[2] - 1][y[2]] == false && gridStatus[x[2] + 1][y[2]]== false)
                        return true;
                }
            }
            else{ //B
                if (x[2] == x[3]){//B-1
                    if(grid[x[1]][y[1] - 1] == 0 && grid[x[1]][y[1] + 1] == 0 &&
                            grid[x[2]][y[2] + 1] == 0 && gridStatus[x[1]][y[1] - 1] == false
                            && gridStatus[x[1]][y[1] + 1] == false && gridStatus[x[2]][y[2] + 1]==false)
                        return true;
                }
                else{ //B-2
                    if(grid[x[2]][y[2] - 1] == 0 && grid[x[2]][y[2] + 1] == 0 &&
                            grid[x[3]][y[3] + 1] == 0 && gridStatus[x[2]][y[2] - 1] == false
                            && gridStatus[x[2]][y[2] + 1] == false && gridStatus[x[3]][y[3] + 1]==false)
                        return true;
                }
            }
        }
        return false; //invalid block type
    }

    //check if the player can move the block down
    public boolean canMoveDown(int[] x, int[] y){
        int count = 0;
        for (int i = 0; i < 4; i++){
            if (y[i] == 19){
                return false;
            }
            if ((gridStatus[x[i]][y[i] + 1]) || (grid[x[i]][y[i] + 1] == 0)){      
                count++;
                if (count == 4){
                    return true;
                }
            }
        }
        return false;
    }

    //Move the block in right, left, or down  direction. The block rotates
    //when the up direction is selecteed.
    public boolean move(Direction direction, int[] x, int[] y, int[] z){ 
        //Check if the block can move or rotate.
        if (canMove(direction, x, y, z) == true){
            //call move() or rotate() method corresponding to the entered direction.
            if (direction == Direction.LEFT){
                moveLeft(x, y, z);
            } else if (direction == Direction.RIGHT){
                moveRight(x, y, z);
            } else if (direction == Direction.UP){
                rotate(x, y, z);
            } else if (direction == Direction.DOWN){
                moveDownAll(x, y, z);
            } else return false;
        } else return false;
        return true; //return true if move() method was sucessful
    }
    
    //Move the block down
    public void moveDown(int[] x, int[] y, int[] z){
        for (int i = 3; i >= 0; i--){
            grid[x[i]][y[i] + 1] = z[i];
            grid[x[i]][y[i]] = 0;
            gridStatus[x[i]][y[i]] = false;
            gridStatus[x[i]][y[i] + 1] = true;
        }
    }

    //Move the block down immediately
    public void moveDownAll(int[] x, int[] y, int[] z){
        moveDown(x, y, z);
        for (int row = 1; row < 20; row++){
            for (int i = 0; i < 4; i++){
                temp[i] = y[i]++;
            }
            if (canMoveDown(x, temp)){
                moveDown(x, temp, z);
            }
            else return;
        }
    }

    //move the block to the left direction
    private void moveLeft(int[] x, int[] y, int[] z){
        for (int i = 0; i < 4; i++){
            grid[x[i] - 1][y[i]] = z[i];
            grid[x[i]][y[i]] = 0;
            gridStatus[x[i] - 1][y[i]] = true;
            gridStatus[x[i]][y[i]] = false;
        } 
    }

    //move the block to the right direction
    private void moveRight(int[] x, int[] y, int[] z){
        for (int i = 3; i >= 0 ; i--){
            grid[x[i] + 1][y[i]] = z[i];
            grid[x[i]][y[i]] = 0;
            gridStatus[x[i] + 1][y[i]] = true;
            gridStatus[x[i]][y[i]] = false;
        }
    }

    //rotate the block
    private void rotate(int[] x, int[] y, int[] z){
        if (this.blockType == 0){
            grid[x[0]][y[0]] = z[1];
            grid[x[1]][y[1]] = z[3];
            grid[x[2]][y[2]] = z[0];
            grid[x[3]][y[3]] = z[2];
        } else if (this.blockType == 1){
            if (y[0] == y[1]){
                for (int i = 0; i < 4; i++){
                    if (i == 2){
                        continue;
                    }
                    grid[x[i]][y[i]] = 0;
                    grid[x[2]][y[i] + 2 - i] = z[i];
                    gridStatus[x[i]][y[i]] = false;
                    gridStatus[x[2]][y[i] + 2 - i] = true;
                }
            }
            else{
                for (int i = 0; i < 4; i++){
                    if (i == 2){
                        continue;
                    }
                    grid[x[i]][y[i]] = 0;
                    grid[x[i] - 2 + i][y[2]] = z[i];
                    gridStatus[x[i]][y[i]] = false;
                    gridStatus[x[i] - 2 + i][y[2]] = true;
                }
            }
        } else if (this.blockType == 2){
            if (y[0] == y[2]){
                if (y[2] == y[3]){ //type A-1
                    grid[x[0]][y[0]] = 0;
                    grid[x[1]][y[1]] = z[0];
                    grid[x[3]][y[3]] = z[1];
                    grid[x[2]][y[2] + 1] = z[3];
                    gridStatus[x[0]][y[0]] = false;
                    gridStatus[x[2]][y[2] + 1] = true;
                    }
                else{ // type A-2
                    grid[x[0]][y[0]] = z[3];
                    grid[x[1]][y[1]] = z[0];
                    grid[x[3]][y[3]] = 0;
                    grid[x[2] + 1][y[2]] = z[1];
                    gridStatus[x[3]][y[3]] = false;
                    gridStatus[x[2] + 1][y[2]] = true;
                }
            }
            else{ //type B-1
                if (x[0] == x[1]){
                    grid[x[0]][y[0]] = 0;
                    grid[x[2]][y[2]] = z[3];
                    grid[x[3]][y[3]] = z[0];
                    grid[x[1] - 1][y[1]] = z[2];
                    gridStatus[x[0]][y[0]] = false;
                    gridStatus[x[1] - 1][y[1]] = true;
                }
                else{ //type B-2
                    grid[x[0]][y[0]] = z[2];
                    grid[x[2]][y[2]] = z[3];
                    grid[x[3]][y[3]] = 0;
                    grid[x[1]][y[1] - 1] = z[0];
                    gridStatus[x[3]][y[3]] = false;
                    gridStatus[x[1]][y[1] - 1] = true;
                }
            }
        }else return; //Invalid block type
    }
}

