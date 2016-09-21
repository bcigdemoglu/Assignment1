//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2016.bcigdem1.dots;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class BoardService {

    private HashMap<String, Board> activeGames;
    private final Logger logger = LoggerFactory.getLogger(BoardService.class);

    /**
     * Construct the model.
     */
    public BoardService() throws BoardServiceException {
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

        Board newBoard = new Board(createdGameId);
        activeGames.put(createdGameId, newBoard);

        Player p = new Player(newPlayerParams.getPlayerId(), createdGameId, newPlayerParams.getPlayerType());
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

        Player p = new Player(newPlayerParams.getPlayerId(), gameId, board.getRemainingColor());
        if (board.checkPlayerInGame(p.getPlayerId()) || board.isBoardFull()) {
            logger.error("BoardService.joinGame: Player already joined / game full");
            throw new BoardServiceException("410 BoardService.joinGame: Player already joined / game full");
        }

        board.addPlayer(p);
        board.startGame();
        return p;
    }

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

    public Board getBoard(String gameId) throws BoardServiceException {
        Board board = activeGames.get(gameId);
        if (board == null) {
            logger.error("BoardService.getBoard: Invalid game ID");
            throw new BoardServiceException("404 BoardService.getBoard: Invalid game ID");
        }
        return board;
    }

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

    public static class BoardServiceException extends Exception {
        public BoardServiceException(String message) {
            super(message);
        }
    }

}
