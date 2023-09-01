package com.jsbyrd02.chessengineapi.rest;

import com.jsbyrd02.chessengineapi.Test;
import com.jsbyrd02.chessengineapi.engine.Chessboard;
import com.jsbyrd02.chessengineapi.engine.utils.Move;
import com.jsbyrd02.chessengineapi.engine.utils.MoveUtils;
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

    // Check info regarding chessboard
    // Test.printChessboardInfo(chessboard);

    ArrayList<Move> activeColorMoves = MoveUtils.findAllMoves(chessboard.getBoard(), chessboard.getActiveColor());
    int numMoves = activeColorMoves.size();

    return "Num moves: " + numMoves;
  }
}
