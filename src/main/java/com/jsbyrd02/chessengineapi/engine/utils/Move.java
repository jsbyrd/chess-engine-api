package com.jsbyrd02.chessengineapi.engine.utils;

import com.jsbyrd02.chessengineapi.engine.pieces.Piece;

public class Move {
  private Piece piece;
  private Position oldPosition;
  private Position newPosition;
  private boolean isCastleMove;
  private boolean isEnPassant;

  public Move(Piece piece, Position oldPosition, Position newPosition, boolean isCastleMove, boolean isEnPassant) {
    this.piece = piece;
    this.oldPosition = oldPosition;
    this.newPosition = newPosition;
    this.isCastleMove = isCastleMove;
    this.isEnPassant = isEnPassant;
  }

  public void printMoveInfo() {
    System.out.println("Piece: " + this.getPiece().getPieceType()
            + ", oldPosition: (" + this.getOldPosition().getFile() + ", " + this.getOldPosition().getRank() + ")"
            + ", newPosition: (" + this.getNewPosition().getFile() + ", " + this.getNewPosition().getRank() + ")");
  }

  public Piece getPiece() {
    return piece;
  }

  public void setPiece(Piece piece) {
    this.piece = piece;
  }

  public Position getOldPosition() {
    return oldPosition;
  }

  public void setOldPosition(Position oldPosition) {
    this.oldPosition = oldPosition;
  }

  public Position getNewPosition() {
    return newPosition;
  }

  public void setNewPosition(Position newPosition) {
    this.newPosition = newPosition;
  }

  public boolean isCastleMove() {
    return isCastleMove;
  }

  public void setCastleMove(boolean castleMove) {
    isCastleMove = castleMove;
  }

  public boolean isEnPassant() {
    return isEnPassant;
  }

  public void setEnPassant(boolean enPassant) {
    isEnPassant = enPassant;
  }
}
