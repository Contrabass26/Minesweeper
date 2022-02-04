import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Drawing extends JPanel {

    static int[] border = {0, 0};
    static int square_size = 1;
    static Random random = new Random();
    static int[][] green = new int[DataManager.BOARD_SIZE][DataManager.BOARD_SIZE];

    static final double FONT_DIV = 1.5;
    static final double[] FONT_POS = {0.5, 1.2};
    static final int BANG_SIZE = 130;
    static final double[] BANG_PROP = {0.2, 0.5};

    static final Color[] TEXT_COLOURS = {
            new Color(56, 56, 153),
            new Color(43, 189, 43),
            new Color(206, 34, 34),
            new Color(200, 0, 255),
            new Color(255, 128, 0),
            new Color(128, 0, 255),
            new Color(217, 217, 44),
            new Color(0, 0, 0)
    };
    static final Color MINE = new Color(222, 30, 30);
    static final Color CLEARED = new Color(184, 219, 178);
    static final Color FLAGGED = new Color(255, 128, 0);
    static final Color BANG = new Color(0, 0, 0);

    public void paint(Graphics g) {
        square_size = Math.min(getWidth(), getHeight()) / DataManager.BOARD_SIZE;
        border = new int[]{
                (getWidth() - square_size * DataManager.BOARD_SIZE) / 2,
                (getHeight() - square_size * DataManager.BOARD_SIZE) / 2
        };
        // Calculate optimal font size
        int size = (int) (square_size / FONT_DIV);
        g.setFont(new Font("Verdana", Font.PLAIN, size));
        for (int x = 0; x < DataManager.BOARD_SIZE; x++) {
            for (int y = 0; y < DataManager.BOARD_SIZE; y++) {
                if (green[x][y] == 0) green[x][y] = random.nextInt(20) + 210;
                Color back_colour = new Color(107, green[x][y], 107);
                if (DataManager.revealed[x][y]) {
                    if (DataManager.board[x][y] == 9) {
                        back_colour = MINE;
                    } else {
                        back_colour = CLEARED;
                    }
                } else if (DataManager.flagged[x][y]) {
                    back_colour = FLAGGED;
                }
                if (Arrays.equals(Window.board_coords, new int[]{x, y})) {
                    back_colour = new Color(back_colour.getRed(), back_colour.getGreen(), back_colour.getBlue(), 128);
                }
                g.setColor(back_colour);
                g.fillRect(border[0] + square_size * x, border[1] + square_size * y, square_size, square_size);
                if (DataManager.board[x][y] % 9 != 0 && DataManager.revealed[x][y]) {
                    g.setColor(TEXT_COLOURS[DataManager.board[x][y] - 1]);
                    g.drawString(String.valueOf(DataManager.board[x][y]), (int) (border[0] + x * square_size + size * FONT_POS[0]), (int) (border[1] + y * square_size + size * FONT_POS[1]));
                }
            }
        }
        // Exploded text (if applicable)
        if (DataManager.exploded) {
            g.setFont(new Font("Verdana", Font.PLAIN, BANG_SIZE));
            g.setColor(BANG);
            g.drawString("BANG!", (int) (BANG_PROP[0] * getWidth()), (int) (BANG_PROP[1] * getHeight()));
        }
    }
}
