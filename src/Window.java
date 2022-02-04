import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Window extends JFrame implements MouseListener, MouseMotionListener {

    static int[] WN_SIZE = {700, 700};
    static int DOUBLE_CLICK_TIME = 400;

    boolean double_clicking = false;
    static int[] board_coords = new int[]{-1, -1};
    Drawing draw_pnl;

    public Window() {
        // Set up window
        super();
        setSize(WN_SIZE[0], WN_SIZE[1]);
        setTitle("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Set layout
        setLayout(new GridLayout(1, 1));
        // Generate board
        DataManager.generate();
        // Add drawing panel
        draw_pnl = new Drawing();
        draw_pnl.setFocusable(true);
        draw_pnl.requestFocusInWindow();
        draw_pnl.addMouseListener(this);
        draw_pnl.addMouseMotionListener(this);
        add(draw_pnl);
        // Finalise setup
        setVisible(true);
    }

    public int[] board_coords(MouseEvent e) {
        int x = Math.floorDiv(e.getX() - Drawing.border[0], Drawing.square_size);
        int y = Math.floorDiv(e.getY() - Drawing.border[1], Drawing.square_size);
        return new int[]{x, y};
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Coordinates on board
        int[] coords = board_coords(e);
        if (!DataManager.revealed[coords[0]][coords[1]]) {
            // Call appropriate method depending on click type
            try {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    DataManager.left_click(coords);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    DataManager.right_click(coords);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                // Board coordinates were out of bounds; do nothing
            }
        } else {
            if (!double_clicking) {
                double_clicking = true;
                Thread background_thread = new Thread(() -> {
                    try {
                        Thread.sleep(DOUBLE_CLICK_TIME);
                    } catch (InterruptedException ex) {
                        // Do nothing
                    }
                    double_clicking = false;
                });
                background_thread.start();
            } else {
                DataManager.double_click(coords);
                double_clicking = false;
            }
        }
        draw_pnl.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        board_coords = board_coords(e);
        draw_pnl.repaint();
    }
}
