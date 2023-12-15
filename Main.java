//I have neither given nor received unauthorized aid on this program.
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Empty string to store the y/n value if the user wants to playAgain
        String playAgain = "";

        int depth; // stores the search depth (for Part C)


        //User prompt for parts
        System.out.print("Run part A, B, or C? ");
        String part = scanner.nextLine();

        System.out.print("Include debugging info? (y/n) ");
        boolean debug = scanner.nextLine().equalsIgnoreCase("y");

        //get game info from user
        System.out.print("Enter rows: ");
        int rows = scanner.nextInt();

        System.out.print("Enter columns: ");
        int cols = scanner.nextInt();

        System.out.print("Enter number in a row to win: ");
        int consecToWin = scanner.nextInt();


        Map<Board, MiniMaxInfo> table = new HashMap<>();

        MiniMaxSearch minimaxSearch;
        MiniMaxInfo initialInfo;


        if(part.equalsIgnoreCase("A")) {
            minimaxSearch = new MiniMaxSearch(PartType.PARTA, 0);
        }
        else if(part.equalsIgnoreCase("B")){
            minimaxSearch = new MiniMaxSearch(PartType.PARTB, 0);
        }
        else {
            scanner = new Scanner(System.in);
            System.out.print("Number of moves to look ahead (depth): ");
            depth = scanner.nextInt();
            minimaxSearch = new MiniMaxSearch(PartType.PARTC, depth);
        }

        long startTime = System.currentTimeMillis();
        minimaxSearch.Search(new Board(rows, cols, consecToWin), table);
        long endTime = System.currentTimeMillis();

        if(part.equalsIgnoreCase("A")) {
            System.out.println("Search completed in " + (endTime - startTime) / 1000.0 + " seconds.");
            System.out.println("Transposition table has " + table.size() + " states.");
        }
        else if(part.equalsIgnoreCase("B")) {
            System.out.println("Search completed in " + (endTime - startTime) / 1000.0 + " seconds.");
            System.out.println("Transposition table has " + table.size() + " states.");
            System.out.println("The tree was pruned " + minimaxSearch.pruneCnt + " times.");
        }
        else if(part.equalsIgnoreCase("C")) {

        }

        ArrayList<String> stateValues = new ArrayList<>();

        Board board = new Board(rows, cols, consecToWin);

        if(part.equalsIgnoreCase("A") || part.equalsIgnoreCase("B")) {
            while(board.getGameState().equals(GameState.IN_PROGRESS)) {
                int computerMove = minimaxSearch.computeMove(board, table);
                board = board.makeMove(computerMove);
            }
            if(board.getGameState().equals(GameState.TIE)) {
                System.out.println("Neither player has a guaranteed win; game will end in tie with perfect play on both sides.");
            }
            else if(board.getWinner().equals(Player.MAX)) {

                System.out.println("First player has a guaranteed win with perfect play.");
            }
            else {
                System.out.println("Second player has a guaranteed win with perfect play.");
            }
        }

        // If debugging is enabled, print the transposition table
        if (debug == true) {
            System.out.println("Transposition table: ");
            // Convert the entrySet to a List
            List<Map.Entry<Board, MiniMaxInfo>> transpositionTableEntries  = new ArrayList<>(table.entrySet());
            for (int i = 0; i < transpositionTableEntries.size(); i++) {
                // Get the current entry
                Map.Entry<Board, MiniMaxInfo> entry = transpositionTableEntries.get(i);
                // Extract the key and value from the entry
                Board key = entry.getKey();
                MiniMaxInfo value = entry.getValue();
                stateValues.add(key + " -> " + value);
            }
            Collections.sort(stateValues);
            for (int i = 0; i < stateValues.size(); i++) {
                System.out.println(stateValues.get(i));
            }
        }

        System.out.print("Who plays first? 1=human, 2=computer: ");
        int firstPlayer = scanner.nextInt();

        while (!playAgain.equalsIgnoreCase("n")) {
            board = new Board(rows, cols, consecToWin);
            while (board.getGameState().equals(GameState.IN_PROGRESS)) {
                System.out.println(board.to2DString());

                MiniMaxInfo info = minimaxSearch.getInfo(board, table);
                if(firstPlayer == 1){
                    if (board.getPlayerToMoveNext() == Player.MAX) {
                        System.out.println("Minimax value for this state: " + info.value() + ", optimal move: " + info.action());
                        System.out.println("It is MAX's turn!");
                        System.out.print("Enter move: ");
                        int humanMove = scanner.nextInt();
                        scanner.nextLine();
                        board = board.makeMove(humanMove);
                    } else {
                        System.out.println("Minimax value for this state: " + info.value() + ", optimal move: " + info.action());
                        System.out.println("It is MIN's turn!");
                        int computerMove = minimaxSearch.computeMove(board, table);
                        board = board.makeMove(computerMove);
                        System.out.println("Computer chooses move: " + computerMove);
                    }
                }
                else if(firstPlayer == 2) {
                    if (board.getPlayerToMoveNext() == Player.MAX) {
                        System.out.println("Minimax value for this state: " + info.value() + ", optimal move: " + info.action());
                        System.out.println("It is MAX's turn!");
                        int computerMove = minimaxSearch.computeMove(board, table);
                        board = board.makeMove(computerMove);
                        System.out.println("Computer chooses move: " + computerMove);
                    } else {
                        System.out.println("Minimax value for this state: " + info.value() + ", optimal move: " + info.action());
                        System.out.println("It is MIN's turn!");
                        System.out.print("Enter move: ");
                        int humanMove = scanner.nextInt();
                        scanner.nextLine();
                        board = board.makeMove(humanMove);
                    }
                }
                if(part.equalsIgnoreCase("C"))
                {
                    System.out.println("Search completed in " + (endTime - startTime) / 1000.0 + " seconds.");
                    System.out.println("Transposition table has " + table.size() + " states.");
                }
            }

            System.out.println("Game over!");
            System.out.println(board.to2DString());

            if(firstPlayer == 1) {
                if (board.getWinner().equals(Player.MAX)) {
                    System.out.println("The winner is MAX (human)");
                } else if (board.getWinner().equals(Player.MIN)) {
                    System.out.println("The winner is MIN (computer)");
                } else {
                    System.out.println("Tie Game! ");
                }
            }
            if(firstPlayer == 2)
            {
                if (board.getWinner().equals(Player.MAX)) {
                    System.out.println("The winner is MAX (computer)");
                } else if (board.getWinner().equals(Player.MIN)) {
                    System.out.println("The winner is MIN (human) ");
                } else {
                    System.out.println("Tie Game! ");
                }
            }


            System.out.print("Play again? (y/n): ");
            playAgain = scanner.next();
        }
    }
}
