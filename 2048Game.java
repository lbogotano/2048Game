package com.codegym.games.game2048;
import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
   
   @Override
    public void initialize() {
               setScreenSize(SIDE, SIDE);
               createGame();
               drawScene();
           
               
    }
    
    private void createGame(){
        createNewNumber();
        createNewNumber();
    }
    
   private void drawScene(){
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                setCellColoredNumber(x,y, gameField[y][x]);
            }
        }
    }
    
    
    private void createNewNumber(){
        int x;
        int y;
        do {
            x = getRandomNumber(SIDE);      //Choose a random cell
            y = getRandomNumber(SIDE);
        } while(gameField[x][y] != 0);   //which should be empty
        int chance = getRandomNumber(10);
        if (chance == 9) gameField[x][y] = 4;      //10% chance for a 4
        else gameField[x][y] = 2;
       }
       
       
     private Color getColorByValue(int value){
        //return a cell color based on value
        switch(value){
            case 0: return Color.WHITE;
            case 2: return Color.BLUE;
            case 4: return Color.RED;
            case 8: return Color.GREEN;
            case 16: return Color.CYAN;
            case 32: return Color.GRAY;
            case 64: return Color.MAGENTA;
            case 128: return Color.ORANGE;
            case 256: return Color.PINK;
            case 512: return Color.YELLOW;
            case 1024: return Color.PURPLE;
            case 2048: return Color.BROWN;
            default: return Color.WHITE;
        }
    }

private void setCellColoredNumber(int x, int y, int value){
        Color colorValue = getColorByValue(value);
        if(value > 0){
            setCellValueEx(x, y, colorValue, Integer.toString(value));   
        }else{
            setCellValueEx(x, y, colorValue, "");
        }
}


private boolean compressRow(int[] row){
    // int col = -1;
    int length = row.length;
    boolean hasChanged = false;
    
     for(int x=0; x< length; x++){
     
     for (int j = 0; j< length * length; j++){
         int i = j % length;
         
         if(i == length -1){
             continue;
         }
         if(row[i] == 0 && row[i +1] !=0){
             row[i] = row[i+1];
             row[i+1] =0;
             hasChanged = true;
         }
     }
     }
     return hasChanged;
    
}

private boolean mergeRow(int[] row){
    boolean moved = false;
    for (int i=0; i< row.length-1;i++)
        if ((row[i] == row[i+1])&&(row[i]!=0)){
            row[i] = 2*row[i];
            row[i+1] = 0;
            score += (row[i] + row[i+1]);
            setScore(score);
            moved = true;


        }

    return moved;
}  
private void moveLeft(){
    
    int count = 0;
    
    for(int i =0; i < SIDE; i++){
        
        boolean compress = compressRow(gameField[i]);
        boolean merge = mergeRow(gameField[i]);
        boolean compresses = compressRow(gameField[i]);
        
        if(compress || merge || compresses){
            count ++;
        }
    }
        if(count != 0){
            createNewNumber();
        }
    
}


private void moveRight(){
    rotateClockwise();
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    rotateClockwise();
}
private void moveUp(){
    rotateClockwise();
    rotateClockwise();
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    
}
private void moveDown(){
    rotateClockwise();
    moveLeft();
    rotateClockwise();
    rotateClockwise();
    rotateClockwise();
}

private void rotateClockwise(){
    for (int i =0; i < SIDE; i++){
        for (int j = i; j<SIDE - i - 1; j++){
            int temp = gameField[i][j];
            gameField[i][j] = gameField[SIDE -1 - j][i];
            gameField[SIDE -1 - j][i] = gameField[SIDE -1 -i][SIDE -1 - j];
            gameField[SIDE -1 -i][SIDE -1 - j] = gameField[j][SIDE - 1- i];
            gameField[j][SIDE -1 -i] = temp;
        }
    }
}

private int getMaxTileValue(){
    int max = 0;
    for (int i = 0; i<SIDE; i++){
        for(int j=0; j<SIDE; j++){
            if(gameField[i][j] > max){
                max = gameField[i][j];
            }
        }
    }
    return max;
}

private boolean canUserMove(){
    boolean hasMove = false;
    for (int x = 0; x <SIDE; x++){
        for(int y = 0; y < SIDE; y++){
            if(gameField[x][y] == 0){
                hasMove = true;
            }
        }
    }
    for (int x = 0; x <SIDE-1; x++){
        for(int y = 0; y < SIDE; y++){
            if(gameField[x][y] == gameField[x+1][y]){
                hasMove = true;
            }
        }
    }
    for (int x = 0; x <SIDE; x++){
        for(int y = 0; y < SIDE-1; y++){
            if(gameField[x][y] == gameField[x][y+1]){
                hasMove = true;
            }
        }
    }
    return hasMove;
}

private void gameOver(){
    isGameStopped = true;
    showMessageDialog(Color.GRAY, "You have no more Moves, You Lost", Color.RED, 34);
}

  @Override
    public void onKeyPress(Key key){
        if(isGameStopped){
            
            if(key == Key.SPACE){
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            }
        }
        else if( canUserMove() ){
        
            if (key == Key.LEFT){
            moveLeft();
            }
            else if (key == Key.RIGHT){
            moveRight();
            }
            else if (key == Key.UP){
                moveUp();
            }
            else if (key == Key.DOWN){
            moveDown();
            }
            drawScene();
            }
        else{
            gameOver(); }
    }
}