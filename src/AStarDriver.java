import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import astar.*;

/**
 * 
 * Program
 * Filename:	AStarDriver.java
 * 
 * Title:		AStarDriver Class (version 2.0)
 * Created on: 	August 21, 2020
 * 
 * Last Date
 * Modified:	
 * 
 * @author Author Maurice Tedder
 *              (based on the Java Applet by James Macgill - http://www.ccg.leeds.ac.uk/james/aStar/
 * 				Informaion about A* algorithm by: Patel, Amits. Amit's A* Pages. Retrieved on February 2005 from
 * 				http://theory.stanford.edu/~amitp/GameProgramming.
 *              Additional references:
 *              https://www.redblobgames.com/pathfinding/a-star/introduction.html
 *              https://en.wikipedia.org/wiki/A*_search_algorithm
 *              https://www.redblobgames.com/pathfinding/a-star/implementation.html#optimize-queue
 *              https://www.redblobgames.com/pathfinding/grids/graphs.html
 * Target
 * Compilers:	Java - j2sdk 1.4.2
 *
 * Description:	Class that implements the A Start shortest path 
 * 				algorithm for a grid map divided into a X x Y grid of gridcells.
 *              
 *              Driver test class for the AStar class in the com.bmai.astar package.
 *              To compile run > javac AStarDriver.java
 *              To run > java AStarDriver
 * 
 *              Ref:
 *              Generate ascii maps from this Web site: https://www.dcode.fr/maze-generator
 */
public class AStarDriver {
	GridMap gridMap = null;

    public AStarDriver() {
        gridMap = getGrid();
        
//        displayGrid();

        //Calculate A* path
        calcAStar();
            
        //plot path on display grid
//        displayGrid();
    }

    /**
     * Calculate AStar* path planning algorithm.
     * @param gridMap
     * @return Map containing path waypoints.
     */
    private GridMap calcAStar() {

        AStar aStar = new AStar();//shortest path algorithm class
//        System.out.println(gridMap);
        GridCell[] pathWaypoints = aStar.findPath(gridMap);

        if(pathWaypoints == null ){//no path found        		
            System.out.println("NO PATH FOUND!");	 	        				
        }
        else{//path found
            System.out.println("PATH FOUND!");

            /*
             * Plot path planner waypoints in grid
             */
            int waypointXCoordinate = 0;
            int waypointYCoordinate = 0;
            for(GridCell gridCell: pathWaypoints){
                    waypointXCoordinate = (int) gridCell.position.x;
                    waypointYCoordinate = (int) gridCell.position.y;
                    gridMap.setGridCell(waypointXCoordinate, waypointYCoordinate, GridMap.PATH, GridMap.NORMAL_CELL);
            }            
        }
        return gridMap;
    }

    /**
     * Load A* grid from file.
     */
    private GridMap getGrid() {
    	int rows = 7;
    	int cols = 5;
    	
    	gridMap = new GridMap((int)rows, (int)cols);
    	
        int[][] map = {
        	{1, 1, 0, 1, 1},
        	{1, 0, 0, 0, 1},
        	{1, 1, 1, 0, 1},
        	{1, 0, 0, 0, 1},
        	{1, 0, 1, 0, 1},
        	{1, 0, 0, 1, 1},
        	{1, 1, 0, 1, 1}
        };
        
        for (int i = 0; i < rows; i++) {
        	for (int j = 0; j < cols; j++) {
				int val = map[i][j];
				if (val == 0) {
					gridMap.setGridCell(i,j, GridMap.NORMAL, GridMap.NORMAL_CELL);
				} else if(val == 1) {
					gridMap.setGridCell(i,j, GridMap.BLOCK, GridMap.NORMAL_CELL);
				}
			}
        }
                    
        gridMap.setGridCell(0,2, GridMap.BLOCK, GridMap.START_CELL);
        gridMap.setGridCell(6,2, GridMap.BLOCK, GridMap.FINISH_CELL);
        return gridMap;
    }

    /**
     * Displays gridMap as an ascii command line graphic
     * 
     * symbols unicode values 0x25A0 solid box 0x2593 dark shade 0x2592 medium shade
     * 0x2591 light shade 0x26AA medium white circle 0x26AB medium black circle
     * 0x2588 full black block
     * 
     * @param gridMap
     */
    private void displayGrid() {

        GridCell[][] grid = gridMap.gridCellMap;

        //Column number formating code
        System.out.println(grid[0].length);
        System.out.print(" ");
  
        for(int col = 0; col < grid[0].length; col++){
            System.out.printf("%d", col%10);//
        }
        System.out.println();// newline

        for (int row = 0; row < grid.length; row++) {
            System.out.printf("%d", row%10);//grid row number
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col].isStart) {
                    System.out.printf("%c", 'S');// medium white circle 
                }else if (grid[row][col].isFinish) {
                    System.out.printf("%c", '$');// medium black circle 
                }else if (grid[row][col].cost == GridMap.NORMAL) {
                    System.out.printf("%c", ' ');// empty space
                } else if (grid[row][col].cost == GridMap.BLOCK) {
                    System.out.printf("%c", 'X');// solid box
                } else if (grid[row][col].cost == GridMap.VERY_TOUGH) {
                    System.out.printf("%c", 0x2592);// medium shade
                } else if (grid[row][col].cost == GridMap.TOUGH) {
                    System.out.printf("%c", 0x2593);// dark shade
                } else if (grid[row][col].cost == GridMap.EASY) {
                    System.out.printf("%c", 0x2591);// light shade
                } else if (grid[row][col].cost == GridMap.PATH) {
                    System.out.printf("%c", '*');// solid box
                } else{//default
                    System.out.printf("%c", ' ');// empty space
                }
            }
            System.out.println();// newline
        }
    }
    
    public List<Integer> getMove() {
    	List<Integer> move = new ArrayList<>();
    	int startX = 0, startY = 0;
    	int X, Y;
    	GridCell[][] grid = gridMap.gridCellMap;
    	
    	for (int row = 0; row < grid.length; row++) {
//            System.out.printf("%d", row%10);//grid row number
            for (int col = 0; col < grid[row].length; col++) {
            	if (grid[row][col].isStart) {
                    startX = col;
                    startY = row;
                }
            }
        }
    	
    	X = startX;
    	Y = startY;
    	
    	// 0 Forward
    	// 1 Left
    	// 2 Right
    	while(true) {
//    		System.out.printf("%d %d\n", X, Y);
    		gridMap.setGridCell(Y,X, GridMap.BLOCK, GridMap.NORMAL_CELL);
    		if(grid[Y][X].isFinish) {
    			move.add(0);
    			break;
    		}
    		
    		if(grid[Y+1][X].cost == GridMap.PATH) {
    			move.add(0);
    			Y = Y+1;
    		} else if(grid[Y][X+1].cost == GridMap.PATH) {
    			move.add(1);
    			move.add(0);
    			move.add(2);
    			X = X+1;
    		} else if(grid[Y][X-1].cost == GridMap.PATH) {
    			move.add(2);
    			move.add(0);
    			move.add(1);
    			X = X - 1;
    		}
    	}
    	
    	return move;
    }
}