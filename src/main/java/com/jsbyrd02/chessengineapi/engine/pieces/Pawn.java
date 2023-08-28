package com.jsbyrd02.chessengineapi.engine.pieces;

import com.jsbyrd02.chessengineapi.engine.utils.*;

import java.util.ArrayList;

public class Pawn extends Piece {

  public Pawn(Position position, PieceType pieceType, PieceColor pieceColor, boolean hasMoved, boolean enPassant) {
    super(position, pieceType, pieceColor);
    setHasMoved(hasMoved);
    setEnPassant(enPassant);
  }

  public ArrayList<Move> generateMoves(Piece[][] board) {
    ArrayList<Move> moves = new ArrayList<>();
    int moveIncrement = (this.getPieceColor() == PieceColor.WHITE) ? -1 : 1;
    int rank = this.getPosition().getRank();
    int file = this.getPosition().getFile();

    if (rank > 0 && rank < 7) {
      // Move forward 1 space so long as no piece is in front & king isn't in check
      if (board[rank + moveIncrement][file] == null) {
        Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file), false, false);
        Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
        if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
          moves.add(potentialMove);
        }
      }
      // Move forward 2 spaces so long as it's the first move, no piece is in front & king isn't in check
      if (!this.isHasMoved()) {
        if (board[rank + moveIncrement][file] == null && board[rank + (moveIncrement * 2)][file] == null) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + (moveIncrement * 2), file), false, false);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }
      // Check for capture on file = file - 1 (Left from white's perspective)
      if (file > 0 && file < 8) {
        Piece potentialVictim = board[rank + moveIncrement][file - 1];
        if (potentialVictim != null && this.isSameColor(potentialVictim)) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file), false, false);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }

      // Check for capture on file = file + 1 (Right from white's perspective)
      if (file >= 0 && file < 7) {
        Piece potentialVictim = board[rank + moveIncrement][file + 1];
        if (potentialVictim != null && this.isSameColor(potentialVictim)) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file), false, false);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }

      // Figure out if any pawn that is eligible for en passant exists
      Piece enPassantVictim = null;
      for (int r = 0; r < board.length; r++) {
        for (int f = 0; f < board[r].length; f++) {
          Piece potentialPawn = board[r][f];
          if (potentialPawn == null && potentialPawn.getPieceType() == PieceType.PAWN && potentialPawn.isEnPassant()) {
            enPassantVictim = potentialPawn;
            break;
          }
        }
      }
      // Add en passant if applicable
      if (enPassantVictim != null) {
        int enPassantRank = enPassantVictim.getPosition().getRank();
        int enPassantFile = enPassantVictim.getPosition().getFile();

        // Check for en passant on file = file - 1 (Left from white's perspective)
        if (enPassantFile == file - 1 && enPassantRank == rank) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file - 1), false, true);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
        // Check for en passant on file = file + 1 (Right from white's perspective)
        if (enPassantFile == file + 1 && enPassantRank == rank) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file + 1), false, true);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }

    }


    return moves;
  }

  public ArrayList<Move> generateAttackMoves(Piece[][] board) {
    ArrayList<Move> moves = new ArrayList<>();
    int moveIncrement = (this.getPieceColor() == PieceColor.WHITE) ? -1 : 1;
    int rank = this.getPosition().getRank();
    int file = this.getPosition().getFile();

    if (rank > 0 && rank < 7) {
      // Check for capture on file = file - 1 (Left from white's perspective)
      if (file > 0 && file < 8) {
        Piece potentialVictim = board[rank + moveIncrement][file - 1];
        if (potentialVictim != null && this.isSameColor(potentialVictim)) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file), false, false);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }

      // Check for capture on file = file + 1 (Right from white's perspective)
      if (file >= 0 && file < 7) {
        Piece potentialVictim = board[rank + moveIncrement][file + 1];
        if (potentialVictim != null && this.isSameColor(potentialVictim)) {
          Move potentialMove = new Move(this, this.getPosition(), new Position(rank + moveIncrement, file), false, false);
          Piece[][] simulatedBoard = MoveUtils.simulateMove(board, potentialMove);
          if (!MoveUtils.isKingInCheck(simulatedBoard, this.getPieceColor())) {
            moves.add(potentialMove);
          }
        }
      }
    }

    return moves;
  }

  public Pawn deepCopy() {
    return new Pawn(this.getPosition(), this.getPieceType(), this.getPieceColor(), this.isHasMoved(), this.isEnPassant());
  }


}
