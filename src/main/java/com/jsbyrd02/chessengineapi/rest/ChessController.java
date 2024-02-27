package com.jsbyrd02.chessengineapi.rest;

import com.jsbyrd02.chessengineapi.Test;
import com.jsbyrd02.chessengineapi.engine.Chessboard;
import com.jsbyrd02.chessengineapi.engine.utils.Move;
import com.jsbyrd02.chessengineapi.engine.utils.MoveUtils;
import com.jsbyrd02.chessengineapi.engine.utils.MiniMax;
import com.jsbyrd02.chessengineapi.engine.utils.PieceColor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ChessController {
  @PostMapping("/moves")
  public String getBestMove (@RequestBody PositionDetailsRequestModel positionDetailsRequestModel) {
    if (positionDetailsRequestModel == null) throw new RuntimeException("No request body found");
    String fen = positionDetailsRequestModel.getFen();
    int depth = positionDetailsRequestModel.getDepth();
    // Not really the best error handling, but it exists.
    if (fen == null || fen.length() == 0) throw new RuntimeException("Invalid fen string");
    if (depth <= 0) throw new RuntimeException("Invalid depth");
    // Create new Chessboard object
    Chessboard chessboard = new Chessboard(fen);
    Chessboard.displayBoard(chessboard.getBoard());
    ArrayList<Move> moves = MoveUtils.findAllMoves(chessboard.getBoard(), chessboard.getActiveColor());

    // Find best move via MiniMax
    MiniMax minimax = new MiniMax();
    Move bestMove = minimax.execute(chessboard, positionDetailsRequestModel.getDepth());
    // Format response as a 4-char string. First two strings represent old position, next two strings represent new position
    String response = "";
    bestMove.printMoveInfo();
    response = response.concat(String.valueOf((char) (bestMove.getOldPosition().getFile() + 'a')));
    response = response.concat(Integer.toString(Math.abs(bestMove.getOldPosition().getRank() - 8)));
    response = response.concat(String.valueOf((char) (bestMove.getNewPosition().getFile() + 'a')));
    response = response.concat(Integer.toString(Math.abs(bestMove.getNewPosition().getRank() - 8)));

    return response;
  }
}
