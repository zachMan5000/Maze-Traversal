// Zachary Deem - Advanced Programming Projects - JAVA - 04/09/2024
import java.io.*;
import java.util.*;
import java.util.Stack;
import java.util.Scanner;  // Import the Scanner class
 
// Output Info:
// blank: cell, dash: wall, 2: visited cell, 3: examined cell, S: start, F: finish, *: finalized path

public class Main{
    static int mazeDim = 10; // Dimension of maze
    static int sleepMs = 500; // Wait time in between frames (ms)
    
    // SLEEP
    public static void doSleep(){
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }       
    // DIRECTION VECTORS
    static int dRow[] = { -1, 0, 1, 0 };
    static int dCol[] = { 0, 1, 0, -1 };
    
    // COORDINATE
    static class coord{
        int x, y;
        public coord(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    
    public static void print2D(int mat[][], coord cS, coord cF){
        for (int i = 0; i < mat.length; i++){
            for (int j = 0; j < mat[i].length; j++){
                if(i == cS.x && j == cS.y){
                    System.out.print("S" + " ");
                }else if(i == cF.x && j == cF.y){
                    System.out.print("F" + " ");
                }
                else if(mat[i][j] == 0){
                    System.out.print("  ");
                }
                else if(mat[i][j] == 1){
                    System.out.print("- ");
                }
                else if(mat[i][j] == 4){
                    System.out.print("* ");
                }
                else{
                    System.out.print(mat[i][j] + " ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
    
    // A half-tree class with nodes only containing values and PARENTS (not CHILDREN)
    static class TreeNode{
        public coord value;
        public TreeNode parent;
        //public List<TreeNode> childNodes;
     
        public TreeNode(coord value){
            this.value = value;
        }
    }

    // CHECK THAT A NODE HAS NOT BEEN VISITED
    static boolean nodeInVisited(List<TreeNode>visited, TreeNode newNode){
        for (int i = 0; i < visited.size(); i++){
            if(visited.get(i).value.x == newNode.value.x && visited.get(i).value.y == newNode.value.y)
                return true;
        }
        return false;
    }

    // CHECK THAT A NODE CAN BE TRAVERSED
    static boolean isValid(int mazeArr[][], List<TreeNode>visited, TreeNode newNode){
        int nodeX = newNode.value.x;
        int nodeY = newNode.value.y;
        int boundx = mazeArr.length - 2;
        int boundy = mazeArr[1].length - 2;
        
        if(!(nodeX < 1 || nodeY < 1 || nodeX > boundx || nodeY > boundy)) // Maze bounds/walls
            if(!nodeInVisited(visited, newNode)) // Visited?
                if(mazeArr[nodeX][nodeY] != 1)
                    return true;
        return false;
    }
    
    // BFS - MAIN TRAVERSAL
    public static void bfs(int mazeArr[][], coord cS, coord cF){
        // SET START/FINISH COORDINATES
        mazeArr[cS.x][cS.y] = 8;
        mazeArr[cF.x][cF.y] = 9;
        
        TreeNode rootTreeNode = new TreeNode(cS);
        List<TreeNode> visited = new ArrayList<TreeNode>();

        Queue<TreeNode> q = new LinkedList<>();
        q.add(rootTreeNode);
        visited.add(rootTreeNode);
        
        TreeNode lastNode = null;
        
        boolean solution = false;
        while (!q.isEmpty() && !solution){
            TreeNode v = q.peek();

            int x = v.value.x;
            int y = v.value.y;
            mazeArr[x][y] = 3; // Visited AND examined!
            q.remove();
            // Go to the adjacent cells
            for(int i = 0; i < 4; i++){
                TreeNode newNode = new TreeNode(new coord(x + dRow[i], y + dCol[i]));
                newNode.parent = v;
                if(isValid(mazeArr, visited, newNode)){
                    q.add(newNode);
                    visited.add(newNode);
                    if(mazeArr[newNode.value.x][newNode.value.y] == 9){
                        solution = true;
                        lastNode = newNode;
                    }
                    mazeArr[newNode.value.x][newNode.value.y] = 2; // Visited, but not examined
                }
            }
            print2D(mazeArr, cS, cF);
            doSleep();
        }
        if(solution){
            boolean pathDrawn = false;
            while (!pathDrawn){
                mazeArr[lastNode.value.x][lastNode.value.y] = 4;
                lastNode = lastNode.parent;
                if(lastNode == null)
                    pathDrawn = true;
            }
            print2D(mazeArr, cS, cF);
            doSleep();
            System.out.print("Solution found! \n");
        }
        else
            System.out.print("No solution found! \n");
    }
    
    // DFS - MAIN TRAVERSAL
    public static void dfs(int mazeArr[][], coord cS, coord cF){
        // SET START/FINISH COORDINATES
        mazeArr[cS.x][cS.y] = 8;
        mazeArr[cF.x][cF.y] = 9;
        
        TreeNode rootTreeNode = new TreeNode(cS);
        List<TreeNode> visited = new ArrayList<TreeNode>();

        Stack<TreeNode> q = new Stack<>();
        q.push(rootTreeNode);
        visited.add(rootTreeNode);
        
        TreeNode lastNode = null;
        
        boolean solution = false;
        while (!q.isEmpty() && !solution){
            TreeNode v = q.peek();

            int x = v.value.x;
            int y = v.value.y;
            mazeArr[x][y] = 3; // Visited AND examined!
            q.pop();
            // Go to the adjacent cells
            for(int i = 0; i < 4; i++){
                TreeNode newNode = new TreeNode(new coord(x + dRow[i], y + dCol[i]));
                newNode.parent = v;
                if(isValid(mazeArr, visited, newNode)){
                    q.push(newNode); //HERE
                    visited.add(newNode);
                    if(mazeArr[newNode.value.x][newNode.value.y] == 9){
                        solution = true;
                        lastNode = newNode;
                    }
                    mazeArr[newNode.value.x][newNode.value.y] = 2; // Visited, but not examined
                }
            }
            print2D(mazeArr, cS, cF);
            doSleep();
        }
        if(solution){
            boolean pathDrawn = false;
            while (!pathDrawn){
                mazeArr[lastNode.value.x][lastNode.value.y] = 4;
                lastNode = lastNode.parent;
                if(lastNode == null)
                    pathDrawn = true;
            }
            print2D(mazeArr, cS, cF);
            doSleep();
            System.out.print("Solution found! \n");
        }
        else
            System.out.print("No solution found! \n");
    }
    
	public static void main(String[] args){
	    boolean quit = false;
	    while(!quit){
	        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Options: b(BFS), d(DFS), q(quit):");

            char userInput = myObj.next().charAt(0);  // Read user input
	        if(userInput != 'q'){
        	    // CONSTRUCT MAZE
        		int[][] mazeArr = new int[mazeDim][mazeDim];
        		for(int i = 0; i < mazeDim; i++)
        		    for(int j = 0; j < mazeDim; j++)
        		        if(i == 0 || j == 0 || i == mazeDim - 1 || j == mazeDim - 1)
        		            mazeArr[i][j] = 1;
        		        else
        		            mazeArr[i][j] = (int)(Math.random()*1.4);
        		coord cS = new coord(1, 1);
        		coord cF = new coord(mazeDim-2, mazeDim-2);
        		
        		if(userInput == 'b')
        		    bfs(mazeArr, cS, cF);
        		else if(userInput == 'd')
        		    dfs(mazeArr, cS, cF);
	        }else{
	            quit = true;
	            System.out.println("Program terminated.");
	        }
	    }
	}
}