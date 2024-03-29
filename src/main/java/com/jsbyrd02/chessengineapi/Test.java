package com.jsbyrd02.chessengineapi;

import com.jsbyrd02.chessengineapi.engine.Chessboard;
import com.jsbyrd02.chessengineapi.engine.pieces.Piece;
import com.jsbyrd02.chessengineapi.engine.utils.Evaluation;
import com.jsbyrd02.chessengineapi.engine.utils.MiniMax;
import com.jsbyrd02.chessengineapi.engine.utils.Move;
import com.jsbyrd02.chessengineapi.engine.utils.MoveUtils;

import java.util.ArrayList;

public class Test {
  public static void main(String[] args) {
    String fen = "5Kbk/6pp/6P1/8/8/8/7R/8 w - - 0 1";
    Chessboard chessboard = new Chessboard(fen);

    ArrayList<Move> moves = MoveUtils.findAllMoves(chessboard.getBoard(), chessboard.getActiveColor());

    MiniMax miniMax = new MiniMax();
    Move bestMove = miniMax.execute(chessboard, 7);
    bestMove.printMoveInfo();
    Chessboard.displayBoard(chessboard.getBoard());
  }
  public static void printChessboardInfo(Chessboard chessboard) {
    System.out.println("Active color: " + chessboard.getActiveColor());
    System.out.println("En Passant: " + chessboard.getEnPassantFile() + ", " + chessboard.getEnPassantRank());
    System.out.println("Half Move Clock: " + chessboard.getHalfMoveClock());
    System.out.println("Full Move Count: " + chessboard.getFullMoveCount());
    System.out.println(chessboard.isBrq() + ", " + chessboard.isBrk() + ", " + chessboard.isBrq() + ", " + chessboard.isBrk());

    Piece[][] board = chessboard.getBoard();
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board.length; file++) {
        Piece piece = board[rank][file];
        if (piece != null) piece.displayPieceInfo();
      }
    }
  }
}
