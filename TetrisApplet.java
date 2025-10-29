import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TetrisApplet extends Applet implements KeyListener, Runnable {

    final int ROWS = 20, COLS = 10;
    int[][] board = new int[ROWS][COLS]; 

    // Tetris pieces (each rotation is stored)
    final int[][][] SHAPES = {
        {{1,1,1,1}}, // I
        {{1,1},{1,1}}, // O
        {{0,1,0},{1,1,1}}, // T
        {{1,0,0},{1,1,1}}, // J
        {{0,0,1},{1,1,1}}, // L
        {{0,1,1},{1,1,0}}, // S
        {{1,1,0},{0,1,1}}, // Z
    };

    int currentX = 0, currentY = 3;
    int shapeIndex, rotation;
    Thread gameLoop;

    public void init() {
        setSize(300, 600);
        addKeyListener(this);
        spawnPiece();

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
        while (true) {
            try { Thread.sleep(600); } catch (Exception e) {}
            moveDown();
            repaint();
        }
    }

    void spawnPiece() {
        shapeIndex = new Random().nextInt(SHAPES.length);
        rotation = 0;
        currentX = 0;
        currentY = COLS / 2 - 1;
    }

    int[][] getPiece() {
        return SHAPES[shapeIndex];
    }

    boolean canMove(int nx, int ny) {
        int[][] p = getPiece();
        for (int r = 0; r < p.length; r++) {
            for (int c = 0; c < p[0].length; c++) {
                if (p[r][c] == 1) {
                    int x = nx + r, y = ny + c;
                    if (x < 0 || x >= ROWS || y < 0 || y >= COLS) return false;
                    if (board[x][y] != 0) return false;
                }
            }
        }
        return true;
    }

    void placePiece() {
        int[][] p = getPiece();
        for (int r = 0; r < p.length; r++) {
            for (int c = 0; c < p[0].length; c++) {
                if (p[r][c] == 1) {
                    board[currentX + r][currentY + c] = shapeIndex + 1;
                }
            }
        }
        clearLines();
        spawnPiece();
    }

    void clearLines() {
        for (int r = 0; r < ROWS; r++) {
            boolean full = true;
            for (int c = 0; c < COLS; c++)
                if (board[r][c] == 0) full = false;

            if (full) {
                for (int i = r; i > 0; i--)
                    board[i] = board[i - 1].clone();
            }
        }
    }

    void moveDown() {
        if (canMove(currentX + 1, currentY)) currentX++;
        else placePiece();
    }

    public void paint(Graphics g) {
        int size = 25;
        // Draw board
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                g.setColor(board[r][c] == 0 ? Color.BLACK : Color.CYAN);
                g.fillRect(c * size, r * size, size, size);
                g.setColor(Color.GRAY);
                g.drawRect(c * size, r * size, size, size);
            }
        }

        // Draw current piece
        g.setColor(Color.RED);
        int[][] p = getPiece();
        for (int r = 0; r < p.length; r++)
            for (int c = 0; c < p[0].length; c++)
                if (p[r][c] == 1)
                    g.fillRect((currentY+c)*size,(currentX+r)*size,size,size);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT: 
                if (canMove(currentX, currentY - 1)) currentY--; 
                break;
            case KeyEvent.VK_RIGHT: 
                if (canMove(currentX, currentY + 1)) currentY++; 
                break;
            case KeyEvent.VK_DOWN:
                moveDown(); 
                break;
            case KeyEvent.VK_UP: // rotate
                rotatePiece();
                break;
        }
        repaint();
    }

    void rotatePiece() {
        // simple rotation by rotating matrix
        int[][] p = getPiece();
        int R = p.length, C = p[0].length;
        int[][] rotated = new int[C][R];
        for (int r = 0; r < R; r++)
            for (int c = 0; c < C; c++)
                rotated[c][R-1-r] = p[r][c];

        // temporarily replace and check validity
        SHAPES[shapeIndex] = rotated;
        if (!canMove(currentX, currentY)) {
            SHAPES[shapeIndex] = p; // undo
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}