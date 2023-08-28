package com.jsbyrd02.chessengineapi.engine.pieces;

import com.jsbyrd02.chessengineapi.engine.utils.Move;
import com.jsbyrd02.chessengineapi.engine.utils.PieceColor;
import com.jsbyrd02.chessengineapi.engine.utils.PieceType;
import com.jsbyrd02.chessengineapi.engine.utils.Position;

import java.util.ArrayList;

public class King extends Piece {

  public King(Position position, PieceType pieceType, PieceColor pieceColor, boolean hasMoved) {
    super(position, pieceType, pieceColor);
    this.setHasMoved(hasMoved);
  }

  public ArrayList<Move> generateMoves(Piece[][] board) {
    ArrayList<Move> moves = new ArrayList<>();
    int[] fileIncrement = {0, 1, 1, 1, 0, -1, -1, -1};
    int[] rankIncrement = {1, 1, 0, -1, -1, -1, 0, 1};

    for (int i = 0; i < fileIncrement.length; i++) {
      int newFile = this.getPosition().getFile() + fileIncrement[i];
      int newRank = this.getPosition().getRank() + rankIncrement[i];
      Position newPosition = new Position(newRank, newFile);

      if (!Position.isLegalPosition(newPosition)) continue;

      // Check to see if there is a piece occupying that particular position
      Piece pontentialPiece = board[newRank][newFile];
      Move potentialMove = new Move(this, this.getPosition(), newPosition, false, false);
      // Check to see if this move attacks another piece
      if (pontentialPiece != null) {
        // Make sure you aren't attacking your own pieces
        if (!this.isSameColor(pontentialPiece)) {
          // Make sure the king isn't walking into an attack
          if (!this.isPinned(board, potentialMove)) {
            moves.add(potentialMove);
          }
        }
        break;
      }
      // If no piece occupies the new position, add this position as a valid move so long as it isn't walking
      // into an attack
      if (!this.isPinned(board, potentialMove)) {
        moves.add(potentialMove);
      }
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
      Position newPosition = new Position(newRank, newFile);
      while (Position.isLegalPosition(newPosition)) {
        // Check to see if there is a piece occupying that particular position
        Piece pontentialPiece = board[newRank][newFile];
        Move potentialMove = new Move(this, this.getPosition(), newPosition, false, false);
        // Check to see if this move attacks another piece
        if (pontentialPiece != null) {
          // Make sure you aren't attacking your own pieces
          if (!this.isSameColor(pontentialPiece)) {
            moves.add(potentialMove);
          }
          break;
        }
        // If no piece occupies the new position, add this position as a valid move
        moves.add(potentialMove);
        // Increment rank and file
        newFile += fileIncrement[i];
        newRank += rankIncrement[i];
      }
    }
    // TODO: ADD CASTLING MOVES


    return moves;
  }

  public King deepCopy() {
    return new King(this.getPosition(), this.getPieceType(), this.getPieceColor(), this.isHasMoved());
  }
}


