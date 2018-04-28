//This file contains the class of GameTile.
//GameTile class has the instruction to make
//the colored tile on the gridpane.

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.*;

//This class makes the tile for the game grid
public class GameTile extends StackPane{
    static HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
    
    //Constructor for GameTile object
    public GameTile(int tile_value, boolean active){
        Rectangle r = new Rectangle(29, 29); //Rectangle for tile
        populateColors();
        Color temp = colors.get(tile_value);
        r.setFill(temp); //Set color to the rectangle
        if (active == false){ //gives the stroke if tile is not active
            r.setStroke(Color.BLACK);
        }
        StackPane tile = new StackPane();
        tile.getChildren().addAll(r); //set the colored rectangle to stackpane
        this.getChildren().add(tile); //set the colored stackpane to GameTile
    }

    //populate the HashMap with Color of the tile.
    public static void populateColors(){ 
        colors.put(0, Color.WHITE);
        colors.put(1, Color.RED);
        colors.put(2, Color.BLUE);
        colors.put(3, Color.YELLOW);
        colors.put(4, Color.GREEN);
    }
}
