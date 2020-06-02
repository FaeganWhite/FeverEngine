/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author faegan
 */
public class Grid {
    int[][] currentGrid = new int[3][3];
    int[][] targetGrid = {{0, 1, 2},{2, 4, 5},{6, 7, 8}}; 
    
    public Grid(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
        targetGrid [0][0] = a;
        targetGrid [0][1] = b;
        targetGrid [0][2] = c;
        targetGrid [1][0] = d;
        targetGrid [1][1] = e;
        targetGrid [1][2] = f;
        targetGrid [2][0] = g;
        targetGrid [2][1] = h;
        targetGrid [2][2] = i;
        
        printGrid();
    }
    
    public void printGrid() {
        for (int i = 0; i < targetGrid.length; i++) {
            for (int j = 0; j < targetGrid[i].length; j++) {
                System.out.print(targetGrid[i][j]);
            }
            System.out.println("");
        }
    }
    
}
