import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameLoop extends JPanel implements MouseListener{

    //used for the location of where thr grid should be drawn
    int gridX = 325;
    int gridY = 10;

    //used to keep track of where tokens have been placed on the board
    int[][] board = new int[27][27];

    //used to keep track of tokens on the small board
    int[][] smallBoard = new int[9][9];

    //used to keep track of tokens on the big board
    int[][] bigBoard = new int[3][3];

    //used to store the last move made and everytime a Token is placed it will update to that locataion
    List<Integer> lastMoveMade = new ArrayList<>(Arrays.asList(null,null));

    //used to keep track of who's trun it is 
    String currentPlayer = "X";

    //creats player tokens
    Text playerToken = new Text();

    //used to get user names
    Scanner userIn = new Scanner(System.in);
    String xPlayer = "";
    String oPlayer = "";
    /* 
     * these are used to determine the range of where a token can be placed
     * are initilized so that the min and max are equal to the whole board
     */
    int minX = 325;
    int maxX = 1025;
    int minY = 10;
    int maxY = 710;


    public GameLoop() {
        System.out.println("X, please enter your name.");
        xPlayer = userIn.next();
        System.out.println("O, please enter your name.");
        oPlayer = userIn.next();
        setLayout(null);
        setPreferredSize(new Dimension(1350,730));
        setBackground(Color.BLACK);
        addMouseListener(this);
        setFocusable(true);
    }

    /*
     * runs every frame and will update what ever compnents are in it. 
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        playerComponents(g);

        gameGrid(g);

        gameState(g);

        drawPlayingBox(g);

        playerComponents(g);
    }

    /*
     * used to create User names of players.
     */
    public void playerComponents(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Text playerName = new Text();

        g.setColor(Color.GRAY);
        g.fillRect(12, 10, 300, 78);
        playerName.draw(g2, 80, Color.red, xPlayer, 20, 70);
        g.setColor(Color.GRAY);
        g.fillRect(1037, 10, 300, 78);
        playerName.draw(g2, 80, Color.blue, oPlayer, 1040, 70);
        

    }

    /*
     * creats the grid for the game
     */
    public void gameGrid(Graphics g) {

        // used for third layer of spaces
        g.setColor(Color.darkGray);
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 26; j++) {
                g.fillRect(gridX + 24*(j+1) + (j*2), gridY + 76*i + i*2, 2, 76);
            }
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 26; j++) {
                g.fillRect(gridX + 76*i + i*2, gridY + 24*(j+1) + (j*2), 76, 2);
            }
        }

        // used for second layer of spaces
        g.setColor(Color.gray);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 8; j++) {
                g.fillRect(gridX + 76*(j+1) + (j*2), gridY + 232*i + i*2, 2, 232);
            }
        }
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 8; j++) {
                g.fillRect(gridX + 232*i + i*2, gridY + 76*(j+1) + (j*2), 232, 2);
            }
        }

        g.setColor(Color.white);
        g.fillRect(gridX+232, gridY, 2, 700);
        g.fillRect(gridX+466, gridY, 2, 700);
        g.fillRect(gridX, gridY+232, 700, 2);
        g.fillRect(gridX, gridY+466, 700, 2);

    }

    /*
     * is used to draw a 3x3 box around the area where a move can be made
     * the if statemnt is to check if its the first move so the box can drawn around the whole board instead
     * uses another if statement to see whos turn it is and will change the color of the box to help the player understand whos turn it is
     */
    public void drawPlayingBox(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2));

        if(currentPlayer == "X") g2.setColor(Color.RED);
        else if(currentPlayer == "O") g2.setColor(Color.BLUE);
        
        if((minX==325 && maxX==1025 && minY==10 && maxY==710)) g2.drawRect(324, 9, 701, 701);
        else if(isBigBoardFull() == true) {
            minX = 325;
            maxX = 1025;
            minY = 10;
            maxY = 710;
            g2.drawRect(minX-1, minY-1, 700, 700);
        } else if(isSmallBoardFull() == true) {
            minX = 325 + (234 * nextBigBoardLocation()[0]);
            maxX = 325 + (234 * nextBigBoardLocation()[0]) + 234;
            minY = 10 + (234 * nextBigBoardLocation()[1]);
            maxY = 10 + (234 * nextBigBoardLocation()[1]) + 234;
            g2.drawRect(minX-1, minY-1, 234, 234);
        } else g2.drawRect(minX-1, minY-1, 78, 78);
    }

    /*
     * X and Y are for location 
     * nextPlayer is for what player is next 
     * playerNum is for what player the space should be 
     */
    public void setPlayPostion(int playerNum, int X, int Y, String nextPlayer) {
        board[X][Y] = playerNum;
        lastMoveMade.set(0, X);
        lastMoveMade.set(1, Y);
        System.out.println(currentPlayer + " Placed at " + lastMoveMade);
        smallGameWon();
        bigGameWon();
        currentPlayer = nextPlayer;
        minX = 325 + (234 * nextBigBoardLocation()[0]) + (78 * nextSmallBoardLocation()[0]);
        maxX = 325 + (234 * nextBigBoardLocation()[0]) + (78 * nextSmallBoardLocation()[0]) + 72;
        minY = 10 + (234 * nextBigBoardLocation()[1]) + (78 * nextSmallBoardLocation()[1]);
        maxY = 10 + (234 * nextBigBoardLocation()[1]) + (78 * nextSmallBoardLocation()[1]) + 72;
    }

    /*
     * will keep track of what moves have been made in the game
     * will update every time a player makes a move and when a game has been won
     */
    public void gameState(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(board[j][i] == 1) playerToken.draw(g2, 30, Color.RED, "✖", gridX-1 + (26*j), gridY+23 + (26*i));
                else if(board[j][i] == 2) playerToken.draw(g2, 33, Color.BLUE, "🞇", gridX-3 + (26*j), gridY+24 + (26*i));
            }
        }

        for(int i = 0; i < smallBoard.length; i++) {
            for(int j = 0; j < smallBoard.length; j++) {
                if(smallBoard[j][i] == 1) {
                    g2.setColor(Color.black);
                    g2.fillRect(gridX + 78*j, gridY + 78*i, 76, 76);
                    playerToken.draw(g2, 101, Color.RED, "✖", gridX-7 + (78*j), gridY+74 + (78*i));
                } else if(smallBoard[j][i] == 2) {
                    g2.setColor(Color.black);
                    g2.fillRect(gridX + 78*j, gridY + 78*i, 76, 76);
                    playerToken.draw(g2, 106, Color.BLUE, "🞇", gridX-10 + (78*j), gridY+76 + (78*i));
                }
            }
        }

        for(int i = 0; i < bigBoard.length; i++) {
            for(int j = 0; j < bigBoard.length; j++) {
                if(bigBoard[j][i] == 1) {
                    g2.setColor(Color.black);
                    g2.fillRect(gridX + 234*j, gridY + 234*i, 232, 232);
                    playerToken.draw(g2, 306, Color.RED, "✖", gridX-19 + (234*j), gridY+226 + (234*i));
                } else if(bigBoard[j][i] == 2) {
                    g2.setColor(Color.black);
                    g2.fillRect(gridX + 234*j, gridY + 234*i, 232, 232);
                    playerToken.draw(g2, 321, Color.BLUE, "🞇", gridX-28 + (234*j), gridY+231 + (234*i));
                }
            }
        }
    }

    /*
     * determines weather a small game has been one and once it has it will tell gameState to update that game to which player won it.
     * within the if statement is all 8 possible ways to win a game and if any of those are true then the if loop will make it so
     * a game has been won
     */
    public void smallGameWon() {

        int xOffSet = (currentBigBoard()[0]*9)+(currentSmallBoard()[0]*3);
        int yOffSet = (currentBigBoard()[1]*9)+(currentSmallBoard()[1]*3);

        if((board[xOffSet][yOffSet] == 1 && board[xOffSet+1][yOffSet] == 1 && board[xOffSet+2][yOffSet] == 1) ||
        (board[xOffSet][yOffSet+1] == 1 && board[xOffSet+1][yOffSet+1] == 1 && board[xOffSet+2][yOffSet+1] == 1) ||
        (board[xOffSet][yOffSet+2] == 1 && board[xOffSet+1][yOffSet+2] == 1 && board[xOffSet+2][yOffSet+2] == 1) ||
        (board[xOffSet][yOffSet] == 1 && board[xOffSet][yOffSet+1] == 1 && board[xOffSet][yOffSet+2] == 1) ||
        (board[xOffSet+1][yOffSet] == 1 && board[xOffSet+1][yOffSet+1] == 1 && board[xOffSet+1][yOffSet+2] == 1) ||
        (board[xOffSet+2][yOffSet] == 1 && board[xOffSet+2][yOffSet+1] == 1 && board[xOffSet+2][yOffSet +2] == 1) ||
        (board[xOffSet][yOffSet] == 1 && board[xOffSet+1][yOffSet+1] == 1 && board[xOffSet+2][yOffSet+2] == 1) ||
        (board[xOffSet+2][yOffSet] == 1 && board[xOffSet+1][yOffSet+1] == 1 && board[xOffSet][yOffSet+2] == 1)) {
            System.out.println("X has won a game!");
            smallBoard[(currentBigBoard()[0]*3)+currentSmallBoard()[0]][(currentBigBoard()[1]*3)+currentSmallBoard()[1]] = 1;
            fillBoard("X");
        } else if((board[xOffSet][yOffSet] == 2 && board[xOffSet+1][yOffSet] == 2 && board[xOffSet+2][yOffSet] == 2) ||
        (board[xOffSet][yOffSet+1] == 2 && board[xOffSet+1][yOffSet+1] == 2 && board[xOffSet+2][yOffSet+1] == 2) ||
        (board[xOffSet][yOffSet+2] == 2 && board[xOffSet+1][yOffSet+2] == 2 && board[xOffSet+2][yOffSet+2] == 2) ||
        (board[xOffSet][yOffSet] == 2 && board[xOffSet][yOffSet+1] == 2 && board[xOffSet][yOffSet+2] == 2) ||
        (board[xOffSet+1][yOffSet] == 2 && board[xOffSet+1][yOffSet+1] == 2 && board[xOffSet+1][yOffSet+2] == 2) ||
        (board[xOffSet+2][yOffSet] == 2 && board[xOffSet+2][yOffSet+1] == 2 && board[xOffSet+2][yOffSet+2] == 2) ||
        (board[xOffSet][yOffSet] == 2 && board[xOffSet+1][yOffSet+1] == 2 && board[xOffSet+2][yOffSet+2] == 2) ||
        (board[xOffSet+2][yOffSet] == 2 && board[xOffSet+1][yOffSet+1] == 2 && board[xOffSet][yOffSet+2] == 2)) {
            System.out.println("O has won a game!");
            smallBoard[(currentBigBoard()[0]*3)+currentSmallBoard()[0]][(currentBigBoard()[1]*3)+currentSmallBoard()[1]] = 2;
            fillBoard("O");
        }
    }

    /*
     * used for when a big game is won and if a game is won will tell gamestate to udate the game as being won
     * within the if statement is all 8 possible ways to win a game and if any of those are true then the if loop will make it so
     * a game has been won
     */
    public void bigGameWon() {

        int xOffSet = currentBigBoard()[0]*3;
        int yOffSet = currentBigBoard()[1]*3;

        if((smallBoard[xOffSet][yOffSet] == 1 && smallBoard[xOffSet+1][yOffSet] == 1 && smallBoard[xOffSet+2][yOffSet] == 1) ||
        (smallBoard[xOffSet][yOffSet+1] == 1 && smallBoard[xOffSet+1][yOffSet+1] == 1 && smallBoard[xOffSet+2][yOffSet+1] == 1) ||
        (smallBoard[xOffSet][yOffSet+2] == 1 && smallBoard[xOffSet+1][yOffSet+2] == 1 && smallBoard[xOffSet+2][yOffSet+2] == 1) ||
        (smallBoard[xOffSet][yOffSet] == 1 && smallBoard[xOffSet][yOffSet+1] == 1 && smallBoard[xOffSet][yOffSet+2] == 1) ||
        (smallBoard[xOffSet+1][yOffSet] == 1 && smallBoard[xOffSet+1][yOffSet+1] == 1 && smallBoard[xOffSet+1][yOffSet+2] == 1) ||
        (smallBoard[xOffSet+2][yOffSet] == 1 && smallBoard[xOffSet+2][yOffSet+1] == 1 && smallBoard[xOffSet+2][yOffSet +2] == 1) ||
        (smallBoard[xOffSet][yOffSet] == 1 && smallBoard[xOffSet+1][yOffSet+1] == 1 && smallBoard[xOffSet+2][yOffSet+2] == 1) ||
        (smallBoard[xOffSet+2][yOffSet] == 1 && smallBoard[xOffSet+1][yOffSet+1] == 1 && smallBoard[xOffSet][yOffSet+2] == 1)) {
            System.out.println("X has won a game!");
            bigBoard[currentBigBoard()[0]][currentBigBoard()[1]] = 1;
            fillSmallBoard("X");
        } else if((smallBoard[xOffSet][yOffSet] == 2 && smallBoard[xOffSet+1][yOffSet] == 2 && smallBoard[xOffSet+2][yOffSet] == 2) ||
        (smallBoard[xOffSet][yOffSet+1] == 2 && smallBoard[xOffSet+1][yOffSet+1] == 2 && smallBoard[xOffSet+2][yOffSet+1] == 2) ||
        (smallBoard[xOffSet][yOffSet+2] == 2 && smallBoard[xOffSet+1][yOffSet+2] == 2 && smallBoard[xOffSet+2][yOffSet+2] == 2) ||
        (smallBoard[xOffSet][yOffSet] == 2 && smallBoard[xOffSet][yOffSet+1] == 2 && smallBoard[xOffSet][yOffSet+2] == 2) ||
        (smallBoard[xOffSet+1][yOffSet] == 2 && smallBoard[xOffSet+1][yOffSet+1] == 2 && smallBoard[xOffSet+1][yOffSet+2] == 2) ||
        (smallBoard[xOffSet+2][yOffSet] == 2 && smallBoard[xOffSet+2][yOffSet+1] == 2 && smallBoard[xOffSet+2][yOffSet+2] == 2) ||
        (smallBoard[xOffSet][yOffSet] == 2 && smallBoard[xOffSet+1][yOffSet+1] == 2 && smallBoard[xOffSet+2][yOffSet+2] == 2) ||
        (smallBoard[xOffSet+2][yOffSet] == 2 && smallBoard[xOffSet+1][yOffSet+1] == 2 && smallBoard[xOffSet][yOffSet+2] == 2)) {
            System.out.println("O has won a game!");
            bigBoard[currentBigBoard()[0]][currentBigBoard()[1]] = 2;
            fillSmallBoard("O");
        }
    }

    /*
     * used to find the location of the next move within the big square
     * @return int[] - the X and Y of big board
     */
    public int[] nextBigBoardLocation() {
        int[] ret = new int[2];
        //find the X position
        if(lastMoveMade.get(0)%9 <= 2) ret[0] = 0;
        else if (lastMoveMade.get(0)%9 <= 5) ret[0] = 1;
        else ret[0] = 2;
        //finds the Y position
        if(lastMoveMade.get(1)%9 <= 2) ret[1] = 0;
        else if (lastMoveMade.get(1)%9 <= 5) ret[1] = 1;
        else ret[1] = 2;

        return ret;
    }

    /*
     * used to find out what the current bigBoard location is
     */
    public int[] currentBigBoard() {
        int[] ret = new int[2];
        // finds X
        if(lastMoveMade.get(0) <= 8) ret[0] = 0;
        else if(lastMoveMade.get(0) <= 17) ret[0] = 1;
        else ret[0] = 2;
        // finds Y 
        if(lastMoveMade.get(1) <= 8) ret[1] = 0;
        else if(lastMoveMade.get(1) <= 17) ret[1] = 1;
        else ret[1] = 2;

        return ret;
    }

    /*
     * used to find the location of the next move within the small square
     * @return int[] - the X and Y of small board
     */
    public int[] nextSmallBoardLocation() {
        int[] ret = new int[2];
        //finds the X position
        ret[0] = lastMoveMade.get(0)%3;
        //finds the Y position
        ret[1] = lastMoveMade.get(1)%3;

        return ret;
    }

    public int[] currentSmallBoard() {
        int[] ret = new int[2];
        //finds X
        if(lastMoveMade.get(0) <= 2 || (lastMoveMade.get(0)%9) <=2) ret[0] = 0;
        else if(lastMoveMade.get(0) <= 5 || (lastMoveMade.get(0)%9) <=5) ret[0] = 1;
        else ret[0] = 2;
        //finds Y
        if(lastMoveMade.get(1) <= 2 || (lastMoveMade.get(1)%9) <=2) ret[1] = 0;
        else if(lastMoveMade.get(1) <= 5 || (lastMoveMade.get(1)%9) <=5) ret[1] = 1;
        else ret[1] = 2;

        return ret;
    }

    /*
     * used to figure out if a small game board is full and if it is it will return true other wise it will retrun false
     */
    public boolean isSmallBoardFull() {
        int xOffSet = (nextBigBoardLocation()[0]*9)+(nextSmallBoardLocation()[0]*3);
        int yOffSet = (nextBigBoardLocation()[1]*9)+(nextSmallBoardLocation()[1]*3);
        int tokens = 0;

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(board[xOffSet+i][yOffSet+j] != 0) tokens++;
            }
        }

        if(tokens == 9) return true;
        else return false;
    }

    /*
     * used to figure out if a big game board is full and if so it will return true
     */
    public boolean isBigBoardFull() {
        int xOffSet = nextBigBoardLocation()[0]*3;
        int yOffSet = nextBigBoardLocation()[1]*3;
        int tokens = 0;

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(smallBoard[xOffSet+i][yOffSet+j] != 0) tokens++;
            }
        }

        if(tokens == 9) return true;
        else return false;
    }

    /*
     * used to change all positions of board to one token to show that the game has been won
     */
    public void fillBoard(String player) {
        int xOffSet = (currentBigBoard()[0]*9)+(currentSmallBoard()[0]*3);
        int yOffSet = (currentBigBoard()[1]*9)+(currentSmallBoard()[1]*3);

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(player == "X") board[xOffSet+i][yOffSet+j] = 1;
                else if(player == "O") board[xOffSet+i][yOffSet+j] = 2;
            }
        }
    }

    /*
     * changes positions on small board to show a small game has been won 
     * will also change the positions of the small game so no tokens can be placed by calling fillBoard
     * the second for loop will change all of the spaces in the big game to what ever player it is
     */
    public void fillSmallBoard(String player) {
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(player == "X") smallBoard[currentBigBoard()[0]*3+i][currentBigBoard()[1]*3+j] = 1;
                else if(player == "O") smallBoard[currentBigBoard()[0]*3+i][currentBigBoard()[1]*3+j] = 2;
            }
        }

        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if(player == "X") board[currentBigBoard()[0]*9+i][currentBigBoard()[1]*9+j] = 1;
                else if(player == "O") board[currentBigBoard()[0]*9+i][currentBigBoard()[1]*9+j] = 2;
            }
        }

    }
    
    
    
    /*
     * when ever a space on the board is clicked it will change the value of the position in the 2D array.
     * once a move has been placed the current players turn is swiched.
     */
    public void mouseClicked(MouseEvent e) {
        int X = (e.getX()-325) / 26;
        int Y = (e.getY()-10) / 26;
        if((e.getX() >= minX && e.getX() <= maxX) && (e.getY() >= minY && e.getY() <= maxY)) {
            if((currentPlayer == "X" && board[X][Y] == 0)) setPlayPostion(1,X,Y,"O"); 
            else if((currentPlayer == "O" && board[X][Y] == 0)) setPlayPostion(2,X,Y,"X");
        }
        repaint();
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}