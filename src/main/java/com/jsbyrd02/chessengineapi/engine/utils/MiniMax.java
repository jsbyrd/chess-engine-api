package com.jsbyrd02.chessengineapi.engine.utils;

import com.jsbyrd02.chessengineapi.engine.Chessboard;
import com.jsbyrd02.chessengineapi.engine.pieces.Piece;

import java.util.ArrayList;

public class MiniMax {
  public Move execute(Chessboard chessboard, int depth) {
    long startTime = System.currentTimeMillis();
    Move bestMove = null;
    double highestSeenValue = Integer.MIN_VALUE;
    double lowestSeenValue = Integer.MAX_VALUE;
    double currentValue;

    PieceColor activeColor = chessboard.getActiveColor();
    Piece[][] currentBoard = chessboard.getBoard();
    ArrayList<Move> moves = MoveUtils.findAllMoves(currentBoard, activeColor);

    for (Move move: moves) {
      Piece[][] simulatedBoardState = MoveUtils.simulateMove(currentBoard, move);
      currentValue = (activeColor == PieceColor.WHITE) ?
              min(simulatedBoardState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE) :
              max(simulatedBoardState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
      if (activeColor == PieceColor.WHITE && currentValue >= highestSeenValue) {
        highestSeenValue = currentValue;
        bestMove = move;
      } else if (activeColor == PieceColor.BLACK && currentValue <= lowestSeenValue) {
        lowestSeenValue = currentValue;
        bestMove = move;
      }
    }

    long executionTime = System.currentTimeMillis() - startTime;
    System.out.println("Execution time: " + executionTime + "ms");

    return bestMove;
  }

  public double max(Piece[][] board, int depth, double alpha, double beta) {
    Piece king = MoveUtils.findKing(board, PieceColor.BLACK);
    if (king == null) {
      return Integer.MAX_VALUE;
    }

    if (depth == 0) {
      return Evaluation.evaluatePosition(board);
    }

    ArrayList<Move> moves = MoveUtils.findAllMoves(board, PieceColor.WHITE);
    if (moves.isEmpty()) {
      if (MoveUtils.isKingInCheck(board, PieceColor.WHITE)) return Integer.MIN_VALUE;
      else return 0;
    }

    for (Move move: moves) {
      Piece[][] simulatedBoardState = MoveUtils.simulateMove(board, move);
      double score = min(simulatedBoardState, depth - 1, alpha, beta);
      if (score >= beta) {
        return beta;
      }
      if (score > alpha) {
        alpha = score;
      }
    }
    return alpha;
  }

  public double min(Piece[][] board, int depth, double alpha, double beta) {
    Piece king = MoveUtils.findKing(board, PieceColor.WHITE);
    if (king == null) {
      return Integer.MIN_VALUE;
    }

    if (depth == 0) {
      return Evaluation.evaluatePosition(board);
    }

    ArrayList<Move> moves = MoveUtils.findAllMoves(board, PieceColor.BLACK);
    if (moves.isEmpty()) {
      if (MoveUtils.isKingInCheck(board, PieceColor.WHITE)) return Integer.MIN_VALUE;
      else return 0;
    }

    for (Move move: moves) {
      Piece[][] simulatedBoardState = MoveUtils.simulateMove(board, move);

      ArrayList<Move> opponentMoves = MoveUtils.findAllMoves(simulatedBoardState, PieceColor.WHITE);
      if (opponentMoves.size() == 0) {
        if (MoveUtils.isKingInCheck(simulatedBoardState, PieceColor.WHITE)) return Integer.MAX_VALUE;
        else return 0;
      }

      double score = max(simulatedBoardState, depth - 1, alpha, beta);
      if (score <= alpha) {
        return alpha;
      }
      if (score < beta) {
        beta = score;
      }
    }
    return beta;
  }
}
