//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2016.bcigdem1.dots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static spark.Spark.*;

public class BoardController {

    private static final String API_CONTEXT = "/dots/api";

    private final BoardService boardService;

    private final Logger logger = LoggerFactory.getLogger(BoardController.class);

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/games", "application/json", (request, response) -> {
            response.status(201);
            return boardService.createNewGame(request.body());
        }, new JsonTransformer());

        put(API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
            try {
                response.status(200);
                return boardService.joinGame(request.params(":id"));
            } catch (BoardService.BoardServiceException ex) {
                logger.error(ex.getMessage());
                response.status(Integer.parseInt(ex.getMessage().split(" ")[0]));
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        post(API_CONTEXT + "/games/:gameId/hmove", "application/json", (request, response) -> {
            try {
                response.status(200);
                boardService.hmove(request.params(":id"), request.body());
            } catch (BoardService.BoardServiceException ex) {
                logger.error(ex.getMessage());
                response.status(Integer.parseInt(ex.getMessage().split(" ")[0]));
            }
            return Collections.EMPTY_MAP;
        }, new JsonTransformer());

        post(API_CONTEXT + "/games/:gameId/vmove", "application/json", (request, response) -> {
            try {
                response.status(200);
                boardService.vmove(request.params(":id"), request.body());
            } catch (BoardService.BoardServiceException ex) {
                logger.error(ex.getMessage());
                response.status(Integer.parseInt(ex.getMessage().split(" ")[0]));
            }
            return Collections.EMPTY_MAP;
        }, new JsonTransformer());

        get(API_CONTEXT + "/games/:gameId/board", "application/json", (request, response) -> {
            try {
                response.status(200);
                return boardService.getBoard(request.params(":id"));
            } catch (BoardService.BoardServiceException ex) {
                logger.error(ex.getMessage());
                response.status(Integer.parseInt(ex.getMessage().split(" ")[0]));
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

        get(API_CONTEXT + "/games/:gameId/state", "application/json", (request, response) -> {
            try {
                response.status(200);
                return boardService.getState(request.params(":id"));
            } catch (BoardService.BoardServiceException ex) {
                logger.error(ex.getMessage());
                response.status(Integer.parseInt(ex.getMessage().split(" ")[0]));
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());
    }
}
