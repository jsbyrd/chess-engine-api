package com.jsbyrd02.chessengineapi.engine.pieces;

import com.jsbyrd02.chessengineapi.engine.utils.*;

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

      if (!Position.isLegalPosition(newRank, newFile)) continue;

      Position newPosition = new Position(newRank, newFile);
      // Check to see if there is a piece occupying that particular position
      Piece pontentialPiece = board[newRank][newFile];
      Move potentialMove = new Move(this, this.getPosition(), newPosition, false, false);
      // Check to see if this move attacks another piece
      if (pontentialPiece != null) {
        // Make sure you aren't attacking your own pieces
        if (!this.isSameColor(pontentialPiece)) {
          // Make sure the king isn't walking into an attack
          Piece[][] simulatedBoardState = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoardState, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
        continue;
      }
      // If no piece occupies the new position, add this position as a valid move so long as it isn't walking
      // into an attack
      Piece[][] simulatedBoardState = MoveUtils.simulateMove(board, potentialMove);
      if (!MoveUtils.isKingInCheck(simulatedBoardState, this.getPieceColor())) {
        moves.add(potentialMove);
      }
    }

    // Generate Castling Moves
    if (!this.isHasMoved()) {
      PieceColor opponentColor = (this.getPieceColor() == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
      ArrayList<Move> allAttackMoves = MoveUtils.findAllAttackMoves(board, opponentColor);

      // King-side Castle
      boolean canCastle = true;
      int oldRookFile = 7;
      int newKingFile = 6;
      int kingRank = this.getPosition().getRank();
      Piece possibleRook = board[kingRank][oldRookFile];
      if (possibleRook != null && possibleRook.getPieceType() == PieceType.ROOK && !possibleRook.isHasMoved()) {
        // King cannot castle with pieces in the way
        if (board[kingRank][newKingFile] != null || board[kingRank][newKingFile-1] != null) {
          canCastle = false;
        }
        // King cannot "castle into" a check
        Position position1 = new Position(kingRank, 5);
        Position position2 = new Position(kingRank, 6);
        for (Move move : allAttackMoves) {
          if (move.getNewPosition().isSamePosition(position1) || move.getNewPosition().isSamePosition(position2)) {
            canCastle = false;
          }
        }
        if (canCastle) {
          Move kingSideCastleMove = new Move(this, this.getPosition(), new Position(kingRank, newKingFile), true, false);
          moves.add(kingSideCastleMove);
        }
      }

      // Queen-side Castle
      canCastle = true;
      oldRookFile = 0;
      newKingFile = 2;
      possibleRook = board[kingRank][oldRookFile];
      if (possibleRook != null && possibleRook.getPieceType() == PieceType.ROOK && !possibleRook.isHasMoved()) {
        // King cannot castle with pieces in the way
        if (board[kingRank][newKingFile] != null || board[kingRank][newKingFile+1] != null) {
          canCastle = false;
        }
        // King cannot "castle into" a check
        Position position1 = new Position(kingRank, 3);
        Position position2 = new Position(kingRank, 2);
        for (Move move : allAttackMoves) {
          if (move.getNewPosition().isSamePosition(position1) || move.getNewPosition().isSamePosition(position2)) {
            canCastle = false;
          }
        }
        if (canCastle) {
          Move kingSideCastleMove = new Move(this, this.getPosition(), new Position(kingRank, newKingFile), true, false);
          moves.add(kingSideCastleMove);
        }
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

  public King deepCopy() {
    return new King(this.getPosition(), this.getPieceType(), this.getPieceColor(), this.isHasMoved());
  }
}


