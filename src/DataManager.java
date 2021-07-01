import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataManager {

    public static final int BOARD_SIZE = 25;
    public static final double MINES_PROP = 0.2;

    public static int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    public static boolean[][] revealed = new boolean[BOARD_SIZE][BOARD_SIZE];
    public static boolean[][] flagged = new boolean[BOARD_SIZE][BOARD_SIZE];
    public static boolean exploded = false;

    public static Random random = new Random();

    public static void generate() {
        // Place mines
        int num_mines = (int) (MINES_PROP * BOARD_SIZE * BOARD_SIZE);
        for (int i = 0; i < num_mines; i++) {
            int x = random.nextInt(BOARD_SIZE);
            int y = random.nextInt(BOARD_SIZE);
            board[x][y] = 9;
        }
        // Calculate every other space
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                // If not a mine already
                if (board[x][y] != 9) {
                    // Calculate how many mines around space
                    board[x][y] = mines_around(new int[]{x, y});
                }
            }
        }
    }

    public static int mines_around(int[] coords) {
        int mines = 0;
        // Go through each surrounding space
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    // If the space is a mine
                    if (board[coords[0] + x][coords[1] + y] == 9) {
                        // Add to mine count
                        mines ++;
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Requested space is off the board, continue regardless
                }
            }
        }
        return mines;
    }

    public static void left_click(int[] coords) {
        if (flagged[coords[0]][coords[1]] || revealed[coords[0]][coords[1]]) return;
        List<int[]> queue = new ArrayList<>();
        queue.add(coords);
        while (queue.size() != 0) {
            try {
                int[] current = queue.get(0);
                revealed[current[0]][current[1]] = true;
                if (board[current[0]][current[1]] == 9) {
                    Thread background_thread = new Thread(() -> {
                        try {
                            exploded = true;
                            Main.wn.draw_pnl.repaint();
                            Thread.sleep(1000);
                            System.exit(0);
                        } catch (InterruptedException e) {
                            // Do nothing
                        }
                    });
                    background_thread.start();
                    return;
                } else if (board[current[0]][current[1]] == 0) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            try {
                                if (board[current[0] + x][current[1] + y] != 9 && !revealed[current[0] + x][current[1] + y]) {
                                    queue.add(new int[]{current[0] + x, current[1] + y});
                                }
                            } catch (IndexOutOfBoundsException e) {
                                // Do nothing
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // Do nothing, click was outside of board
            }
            queue.remove(0);
        }
    }

    public static void right_click(int[] coords) {
        try {
            if (!revealed[coords[0]][coords[1]]) {
                flagged[coords[0]][coords[1]] = !flagged[coords[0]][coords[1]];
            }
        } catch (IndexOutOfBoundsException e) {
            // Do nothing, click was outside of board
        }
    }

    public static void double_click(int[] coords) {
        if (revealed[coords[0]][coords[1]]) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    try {
                        left_click(new int[]{coords[0] + x, coords[1] + y});
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Desired space was off the board; move on
                    }
                }
            }
        }
    }
}
