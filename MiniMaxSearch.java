import java.util.*;
public class MiniMaxSearch {
    private Map<Board, MiniMaxInfo> table;
    private PartType part;
    public int pruneCnt = 0; // variable to count the number of prunes
    private int maxDepth;

    public MiniMaxSearch(PartType part, int maxDepth) {
        this.table = new HashMap<>();
        this.part = part;
        this.maxDepth = maxDepth;
    }

    public MiniMaxInfo Search(Board state,  Map<Board,MiniMaxInfo> table) {
        if(part.equals(PartType.PARTA)) {
            return  miniMaxSearch(state,table);
        }
        else if(part.equals(PartType.PARTB)) {
            return alphaBetaSearch(state, Integer.MIN_VALUE, Integer.MAX_VALUE, table);
        }
        else {
            return alphaBetaHeuristicSearch(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, table);
        }
    }

    // Computes the appropriate move for each of the board state using the chosen search algorithm
    public int computeMove(Board state, Map<Board,MiniMaxInfo> table) {
        if(table.containsKey(state)) {
            if(table.get(state).action() != null) {
                return table.get(state).action();
            }
            else {
                table.remove(state);
            }
        }
        // does not get called because miniMaxSearch goes through entire table
        if(part.equals(PartType.PARTA)) {
            MiniMaxInfo info =  miniMaxSearch(state,table);
            return info.action();
        }
        else if(part.equals(PartType.PARTB)) {
            MiniMaxInfo info =  alphaBetaSearch(state, Integer.MIN_VALUE, Integer.MAX_VALUE, table);
            return info.action();
        }
        else if(part.equals(PartType.PARTC)) {
            table.clear();
            MiniMaxInfo info =  alphaBetaHeuristicSearch(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, table);
            return info.action();
        }
        return -1;
    }
    //
    public MiniMaxInfo getInfo(Board state, Map<Board,MiniMaxInfo> table) {
        if(table.containsKey(state)) {
            if(table.get(state).action() != null) {
                return table.get(state);
            }
            else {
                table.remove(state);
            }
        }
            return Search(state, table);
    }
    private boolean isTerminal(Board state) {
        return !state.getGameState().equals(GameState.IN_PROGRESS);
    }

    // Check if the search should be cut off due to reaching maximum depth.
    private boolean isCutOff(Board state, int depth) {
        if(depth >= maxDepth){
            return true;
        }
        return false;
    }

    // Calculate the utility of a terminal state.
    private int Utility(Board state) {
        int num = (int)(10000.0 * state.getRows() * state.getCols() / state.getNumberOfMoves());
        if(state.getGameState().equals(GameState.TIE)){
            return 0;
        }
        if(state.getGameState().equals(GameState.MAX_WIN)){
            return num;
        }
        if(state.getGameState().equals(GameState.MIN_WIN)) {
            return -num;
        }
        return 0;
    }

    // checks if there are 2 in a row and assigns a score of +3 to MAX or -3 to MIN and if there is three in a row it
    // adds + 10 to MAX and -10 to MIN. Also it adds the number of adjacent blank spaces to the score
    private int Eval(Board state) {
        int score = 0;
        for(int i = 0; i < state.getRows(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveRowsIdx(i,0, Player.MAX);
            int zeros = state.findEmptySpacesInSameRow(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score += 3 +zeros;
            }
            if(cnt.get(0)  == 3) {
                score += 10 + zeros;
            }
            cnt = state.countConsecutiveRowsIdx(i,0, Player.MIN);
            zeros = state.findEmptySpacesInSameRow(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0)  == 2) {
                score -= 3 + zeros;
            }
            if(cnt.get(0)  == 3) {
                score -= 10 + zeros;
            }
        }
        for(int i = 0; i < state.getCols(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveColsIdx(i,0, Player.MAX);
            if(cnt.get(0) == 2) {
                score += 3;
            }
            if(cnt.get(0) == 3) {
                score += 10;
            }
            cnt = state.countConsecutiveColsIdx(0,i, Player.MIN);
            if(cnt.get(0) == 2) {
                score -= 3;
            }
            if(cnt.get(0) == 3) {
                score -= 10;
            }
        }
        for(int i = 0; i < state.getRows(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveDiagonalUpIdx(i,0, Player.MAX);
            int zeros = state.findEmptySpacesInSameDiagUp(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score += 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score += 10 + zeros;
            }
            cnt = state.countConsecutiveDiagonalUpIdx(i,0, Player.MIN);
            zeros = state.findEmptySpacesInSameDiagUp(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score -= 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score -= 10 + zeros;
            }
        }
        for(int i = 0; i < state.getCols(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveDiagonalUpIdx(0,i, Player.MAX);
            int zeros = state.findEmptySpacesInSameDiagUp(0,i, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score += 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score += 10 + zeros;
            }
            cnt = state.countConsecutiveDiagonalUpIdx(0,i, Player.MIN);
            zeros = state.findEmptySpacesInSameDiagUp(0,i, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score -= 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score -= 10 + zeros;
            }
        }

        for(int i = 0; i < state.getRows(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveDiagonalDownIdx(i, state.getCols() - 1, Player.MAX);
            int zeros = state.findEmptySpacesInSameDiagDown(i,state.getCols() - 1, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score += 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score += 10 + zeros;
            }
            cnt = state.countConsecutiveDiagonalDownIdx(i,state.getCols() - 1, Player.MIN);
            zeros = state.findEmptySpacesInSameDiagDown(i,state.getCols() - 1, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score -= 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score -= 10 + zeros;
            }
        }
        for(int i = 0; i < state.getRows(); i++){
            ArrayList<Integer> cnt = state.countConsecutiveDiagonalDownIdx(i,0, Player.MAX);
            int zeros = state.findEmptySpacesInSameDiagDown(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score += 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score += 10 + zeros;
            }
            cnt = state.countConsecutiveDiagonalDownIdx(i,0, Player.MIN);
            zeros = state.findEmptySpacesInSameDiagDown(i,0, cnt.get(0), cnt.get(1));
            if(cnt.get(0) == 2) {
                score -= 3 + zeros;
            }
            if(cnt.get(0) == 3) {
                score -= 10 + zeros;
            }
        }
        return score;
    }

    public Player toMove(Board state) {
        return state.getPlayerToMoveNext();
    }

    private List<Integer> Actions(Board state) {
        List<Integer> validActions = new ArrayList<>();
        for (int col = 0; col < state.getCols(); col++) {
            if (!state.isColumnFull(col)) {
                validActions.add(col);
            }
        }
        return validActions; // Returns a list of valid actions (moves) that can be performed from a given state
    }

    public MiniMaxInfo miniMaxSearch(Board state, Map<Board, MiniMaxInfo> table) {
        if (table.containsKey(state)) {
            return table.get(state);
        } else if (isTerminal(state)) {
            int util = Utility(state);
            MiniMaxInfo info = new MiniMaxInfo(util, null);
            table.put(state, info);
            return info;
        } else if (toMove(state).equals(Player.MAX)) {
            int value = Integer.MIN_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = miniMaxSearch(child_state, table);
                int v2 = child_info.value();
                if (v2 > value) {
                    value = v2;
                    best_move = action;
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        } else { // TO-MOVE(state) == MIN
            int value = Integer.MAX_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = miniMaxSearch(child_state, table);
                int v2 = child_info.value();
                if (v2 < value) {
                    value = v2;
                    best_move = action;
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        }
    }

    public MiniMaxInfo alphaBetaSearch(Board state, int alpha, int beta, Map<Board,MiniMaxInfo> table) {
        if (table.containsKey(state)) {
            return table.get(state);
        }
        else if (isTerminal(state)) {
            int util = Utility(state);
            MiniMaxInfo info = new MiniMaxInfo(util, null);
            table.put(state, info);
            return info;
        } else if (toMove(state).equals(Player.MAX)) {
            int value = Integer.MIN_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = alphaBetaSearch(child_state, alpha, beta, table);
                int v2 = child_info.value();
                if (v2 > value) {
                    value = v2;
                    best_move = action;
                    if(value > alpha) {
                        alpha = value;
                    }
                }
                if(value >= beta) {
                    pruneCnt++;
                    return new MiniMaxInfo(value, best_move);
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        } else { // TO-MOVE(state) == MIN
            int value = Integer.MAX_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = alphaBetaSearch(child_state, alpha, beta, table);
                int v2 = child_info.value();
                if (v2 < value) {
                    value = v2;
                    best_move = action;
                    if(value < beta) {
                        beta = value;
                    }
                }
                if(value <= alpha){
                    pruneCnt++;
                    return new MiniMaxInfo(value, best_move);
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        }
    }


    // Heuristic!!!!
    public MiniMaxInfo alphaBetaHeuristicSearch(Board state, int alpha, int beta, int depth, Map<Board,MiniMaxInfo> table) {
        if (table.containsKey(state)) {
            return table.get(state);
        } else if (isTerminal(state)) {
            int util = Utility(state);
            MiniMaxInfo info = new MiniMaxInfo(util, null);
            table.put(state, info);
            return info;
        } else if(isCutOff(state, depth)) {
            //Evil Eval
            int heuristic = Eval(state);
            MiniMaxInfo info = new MiniMaxInfo(heuristic, null);
            table.put(state, info); // add this state to the transposition table
            return info;
        }
        else if (toMove(state).equals(Player.MAX)) {
            int value = Integer.MIN_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = alphaBetaHeuristicSearch(child_state, alpha, beta, depth + 1, table);
                int v2 = child_info.value();
                if (v2 > value) {
                    value = v2;
                    best_move = action;
                    if(value > alpha) {
                        alpha = value;
                    }
                }
                if(value >= beta) {
                    return new MiniMaxInfo(value, best_move);
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        } else { // TO-MOVE(state) == MIN
            int value = Integer.MAX_VALUE;
            Integer best_move = null;
            List<Integer> actions = Actions(state);

            for (int i = 0; i < actions.size(); i++) {
                Integer action = actions.get(i);
                Board child_state = state.makeMove(action);
                MiniMaxInfo child_info = alphaBetaHeuristicSearch(child_state, alpha, beta, depth + 1, table);
                int v2 = child_info.value();
                if (v2 < value) {
                    value = v2;
                    best_move = action;
                    if(value < beta) {
                        beta = value;
                    }
                }
                if(value <= alpha){
                    return new MiniMaxInfo(value, best_move);
                }
            }
            MiniMaxInfo info = new MiniMaxInfo(value, best_move);
            table.put(state, info);
            return info;
        }
    }
}

