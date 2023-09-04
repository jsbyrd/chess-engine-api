package com.jsbyrd02.chessengineapi.engine.pieces;

import com.jsbyrd02.chessengineapi.engine.utils.Move;
import com.jsbyrd02.chessengineapi.engine.utils.PieceColor;
import com.jsbyrd02.chessengineapi.engine.utils.PieceType;
import com.jsbyrd02.chessengineapi.engine.utils.Position;

import java.util.ArrayList;

public class Knight extends Piece {

  public Knight(Position position, PieceType pieceType, PieceColor pieceColor) {
    super(position, pieceType, pieceColor);
  }

  public ArrayList<Move> generateMoves(Piece[][] board) {
    ArrayList<Move> moves = new ArrayList<>();
    int[] fileIncrement = {1, 2, 2, 1, -1, -2, -2, -1};
    int[] rankIncrement = {2, 1, -1, -2, -2, -1, 1, 2};

    for (int i = 0; i < fileIncrement.length; i++) {
      int newFile = this.getPosition().getFile() + fileIncrement[i];
      int newRank = this.getPosition().getRank() + rankIncrement[i];
      if (!Position.isLegalPosition(newRank, newFile)) continue;

      Position newPosition = new Position(newRank, newFile);
      // Check to see if there is a piece occupying that particular position
      Piece pontentialPiece = board[newRank][newFile];
      Move potentialMove = new Move(this, this.getPosition(), newPosition, false, false);
      // Check to see if this move attacks another piece
      if (pontentialPiece != null) {
        // Make sure you aren't attacking your own pieces
        if (!this.isSameColor(pontentialPiece)) {
          // Make sure this particular piece isn't pinned to king
          if (!this.isPinned(board, potentialMove)) {
            moves.add(potentialMove);
          }
        }
        continue;
      }
      // If no piece occupies the new position, add this position as a valid move
      moves.add(potentialMove);
    }
    return moves;
  }

  public ArrayList<Move> generateAttackMoves(Piece[][] board) {
    ArrayList<Move> moves = new ArrayList<>();
    int[] fileIncrement = {1, 2, 2, 1, -1, -2, -2, -1};
    int[] rankIncrement = {2, 1, -1, -2, -2, -1, 1, 2};

    for (int i = 0; i < fileIncrement.length; i++) {
      int newFile = this.getPosition().getFile() + fileIncrement[i];
      int newRank = this.getPosition().getRank() + rankIncrement[i];
      if (!Position.isLegalPosition(newRank, newFile)) continue;

      Position newPosition = new Position(newRank, newFile);
      // Check to see if there is a piece occupying that particular position
      Piece pontentialPiece = board[newRank][newFile];
      Move potentialMove = new Move(this, this.getPosition(), newPosition, false, false);
      // Check to see if this move attacks another piece
      if (pontentialPiece != null) {
        // Make sure you aren't attacking your own pieces
        if (!this.isSameColor(pontentialPiece)) {
          moves.add(potentialMove);
        }
        continue;
      }
      // If no piece occupies the new position, add this position as a valid move
      moves.add(potentialMove);
    }
    return moves;
  }

  public Knight deepCopy() {
    return new Knight(this.getPosition(), this.getPieceType(), this.getPieceColor());
  }
}


