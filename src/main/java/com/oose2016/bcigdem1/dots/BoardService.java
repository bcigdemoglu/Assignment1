//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2016.bcigdem1.dots;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;


/**
 * Service.
 */
public class BoardService {

    private HashMap<String, Board> activeGames;
    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    /**
     * Construct the model by creating a map of gameids and boards.
     */
    public BoardService() {
        activeGames = new HashMap<>();
    }

    /**
     * Start a game.
     *
     * @return NewGameParams object
     */
    public Player createNewGame(String body) {
        NewPlayerParams newPlayerParams = new Gson().fromJson(body, NewPlayerParams.class);
        String createdGameId = UUID.randomUUID().toString();

        Board newBoard = new Board();
        activeGames.put(createdGameId, newBoard);
        logger.info(new Gson().toJson(activeGames));
        Player p = new Player(newPlayerParams.getPlayerId(), newPlayerParams.getPlayerType(), createdGameId);
        newBoard.addPlayer(p);
        return p;
    }

    /**
     * Join an existing game.
     *
     * @return NewGameParams object
     */
    public Player joinGame(String gameId) throws BoardServiceException {
        NewPlayerParams newPlayerParams = new NewPlayerParams();

        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.joinGame: Invalid game ID");
            throw new BoardServiceException("404 BoardService.joinGame: Invalid game ID");
        }

        Player p = new Player(newPlayerParams.getPlayerId(), board.getRemainingColor(), gameId);
        if (board.isBoardFull() || board.checkPlayerInGame(p.getPlayerId())) {
            logger.error("BoardService.joinGame: Player already joined / game full");
            throw new BoardServiceException("410 BoardService.joinGame: Player already joined / game full");
        }

        board.addPlayer(p);
        board.startGame();
        return p;
    }

    /**
     * @param gameId Game id
     * @param body Horizontal move request
     * @throws BoardServiceException if move is not possible
     */
    public void hmove(String gameId, String body) throws BoardServiceException {
        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.hmove: Invalid game ID");
            throw new BoardServiceException("404 BoardService.hmove: Invalid game ID");
        }

        MoveAttempt move = new Gson().fromJson(body, MoveAttempt.class);
        if (!board.checkPlayerInGame(move.getPlayerId())) {
            logger.error("BoardService.hmove: Invalid player ID");
            throw new BoardServiceException("404 BoardService.hmove: Invalid player ID");
        }

        Player p = board.getPlayerById(move.getPlayerId());
        if (!board.getState().getWhoseTurn().equals(p.getPlayerType())) {
            logger.error("BoardService.hmove: Incorrect turn / not this player's turn");
            throw new BoardServiceException("422 BoardService.hmove: Incorrect turn / not this player's turn");
        }

        boolean moveIsLegal = board.addHorizontalLine(move.getRow(), move.getCol());
        if (!moveIsLegal) {
            logger.error("BoardService.hmove: Illegal move");
            throw new BoardServiceException("422 BoardService.hmove: Illegal move");
        }

        int score = board.checkHorizontalBox(move.getRow(), move.getCol(), p.getPlayerId());
        if (score > 0) {
            board.getState().incScore(score, p.getPlayerType());
        } else{
            board.getState().nextTurn();
        }
    }

    /**
     * @param gameId Game id
     * @param body Vertical move request
     * @throws BoardServiceException if move is not possible
     */
    public void vmove(String gameId, String body) throws BoardServiceException {
        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.vmove: Invalid game ID");
            throw new BoardServiceException("404 BoardService.vmove: Invalid game ID");
        }

        MoveAttempt move = new Gson().fromJson(body, MoveAttempt.class);
        if (!board.checkPlayerInGame(move.getPlayerId())) {
            logger.error("BoardService.vmove: Invalid player ID");
            throw new BoardServiceException("404 BoardService.vmove: Invalid player ID");
        }

        Player p = board.getPlayerById(move.getPlayerId());
        if (!board.getState().getWhoseTurn().equals(p.getPlayerType())) {
            logger.error("BoardService.vmove: Incorrect turn / not this player's turn");
            throw new BoardServiceException("422 BoardService.vmove: Incorrect turn / not this player's turn");
        }

        boolean moveIsLegal = board.addVerticalLine(move.getRow(), move.getCol());
        if (!moveIsLegal) {
            logger.error("BoardService.vmove: Illegal move");
            throw new BoardServiceException("422 BoardService.vmove: Illegal move");
        }

        int score = board.checkVerticalBox(move.getRow(), move.getCol(), p.getPlayerId());
        if (score > 0) {
            board.getState().incScore(score, p.getPlayerType());
        } else{
            board.getState().nextTurn();
        }
    }

    /**
     * @param gameId Game id
     * @return State of the board
     * @throws BoardServiceException Invalid game ID
     */
    public Board getBoard(String gameId) throws BoardServiceException {
        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.getBoard: Invalid game ID");
            throw new BoardServiceException("404 BoardService.getBoard: Invalid game ID");
        }
        return board;
    }

    /**
     * @param gameId Game id
     * @return State of the game
     * @throws BoardServiceException Invalid game ID
     */
    public State getState(String gameId) throws BoardServiceException {
        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.getState: Invalid game ID");
            throw new BoardServiceException("404 BoardService.getState: Invalid game ID");
        }
        return board.getState();
    }

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    /**
     * Thrown when the game cannot proceed
     */
    public static class BoardServiceException extends Exception {
        public BoardServiceException(String message) {
            super(message);
        }
    }

}
