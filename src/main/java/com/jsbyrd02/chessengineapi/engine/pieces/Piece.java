package com.jsbyrd02.chessengineapi.engine.pieces;

import com.jsbyrd02.chessengineapi.engine.utils.*;

import java.util.ArrayList;

public abstract class Piece {
  private Position position;
  private PieceType pieceType;
  private PieceColor pieceColor;
  private boolean hasMoved;
  private boolean enPassant;

  public Piece() {

  }

  public Piece(Position position, PieceType pieceType, PieceColor pieceColor) {
    this.position = position;
    this.pieceType = pieceType;
    this.pieceColor = pieceColor;
    this.hasMoved = false;
    this.enPassant = false;
  }

  // Abstract methods
  public abstract ArrayList<Move> generateMoves(Piece[][] board);

  public abstract ArrayList<Move> generateAttackMoves(Piece[][] board);

  public abstract Piece deepCopy();

  public void displayPieceInfo() {
    int rank = this.position.getRank();
    int file = this.position.getFile();
    System.out.println("Position: (" + rank + ", " + file + "), PieceType: " + this.pieceType + ", PieceColor: " + this.pieceColor);
  }

  public boolean isSameColor(Piece otherPiece) {
    return this.pieceColor == otherPiece.getPieceColor();
  }

  public boolean isPinned(Piece[][] board, Move potentialMove) {
    Piece[][] simulatedBoardState = MoveUtils.simulateMove(board, potentialMove);
    return MoveUtils.isKingInCheck(simulatedBoardState, this.pieceColor);
  }

  public boolean isHasMoved() {
    return hasMoved;
  }

  public void setHasMoved(boolean hasMoved) {
    this.hasMoved = hasMoved;
  }

  public boolean isEnPassant() {
    return enPassant;
  }

  public void setEnPassant(boolean enPassant) {
    this.enPassant = enPassant;
  }

  public Position getPosition() {
    return position;
  }

  public PieceType getPieceType() {
    return pieceType;
  }

  public PieceColor getPieceColor() {
    return pieceColor;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void setPieceType(PieceType pieceType) {
    this.pieceType = pieceType;
  }

  public void setPieceColor(PieceColor pieceColor) {
    this.pieceColor = pieceColor;
  }
}
