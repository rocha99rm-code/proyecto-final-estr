import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class GridGameApplet extends Applet implements KeyListener, Runnable {

    int rows = 10, cols = 10;
    int[][] board = new int[rows][cols]; // Array representing the game grid

    int playerX = 5, playerY = 5;

    Stack<Point> undoStack = new Stack<>(); // Stack to store previous positions
    Queue<Point> itemQueue = new LinkedList<>(); // Queue of items to spawn

    Thread gameLoop;

    public void init() {
        setSize(300, 300);
        addKeyListener(this);

        // Preload collectible items into queue
        itemQueue.add(new Point(2, 2));
        itemQueue.add(new Point(7, 7));
        itemQueue.add(new Point(1, 8));
        itemQueue.add(new Point(8, 3));

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public void run() {
        while (true) {
            spawnItem(); // Try to add an item from queue
            repaint();
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }

    // Try to place next item from queue into board
    void spawnItem() {
        if (!itemQueue.isEmpty()) {
            Point p = itemQueue.poll();
            board[p.x][p.y] = 2; // 2 = item
        }
    }

    public void paint(Graphics g) {
        int cellSize = 25;

        // Draw grid and pieces
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // Player
                if (i == playerX && j == playerY) {
                    g.setColor(Color.BLUE);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                // Items
                else if (board[i][j] == 2) {
                    g.setColor(Color.RED);
                    g.fillOval(j * cellSize + 5, i * cellSize + 5, 15, 15);
                }
                // Empty
                else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        // Save old position in stack for undo
        undoStack.push(new Point(playerX, playerY));

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    if (playerX > 0) playerX--; break;
            case KeyEvent.VK_DOWN:  if (playerX < rows - 1) playerX++; break;
            case KeyEvent.VK_LEFT:  if (playerY > 0) playerY--; break;
            case KeyEvent.VK_RIGHT: if (playerY < cols - 1) playerY++; break;

            case KeyEvent.VK_U: // Undo movement
                if (!undoStack.isEmpty()) {
                    Point prev = undoStack.pop();
                    playerX = prev.x;
                    playerY = prev.y;
                }
                break;
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
