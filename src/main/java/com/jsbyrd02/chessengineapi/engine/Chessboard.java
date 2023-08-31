package com.jsbyrd02.chessengineapi.engine;

import com.jsbyrd02.chessengineapi.engine.pieces.*;
import com.jsbyrd02.chessengineapi.engine.utils.PieceColor;
import com.jsbyrd02.chessengineapi.engine.utils.PieceType;
import com.jsbyrd02.chessengineapi.engine.utils.Position;

public class Chessboard {
  private Piece[][] board;
  private PieceColor activeColor;
  private boolean brq, brk, wrq, wrk; // Handles castling rights (brk = black rook king-side, etc...)
  private int enPassantRank, enPassantFile;
  private int halfMoveClock;
  private int fullMoveCount;

  public Chessboard() {

  }

  public Chessboard(String fen) {
    // Convert FEN to Piece[][]
    Piece[][] boardClone = new Piece[8][8];
    char[] fenChars = fen.toCharArray();
    int divider = 0; // Keeps track of when piece placement ends
    int rank = 0;
    int file = 0;

    // Find where the piece placement info stops
    for (int i = 0; i < fenChars.length; i++) {
      char c = fenChars[i];
      if (c == ' ') {
        divider = i;
        break;
      }
    }

    // From there, track down game info (castling rights, en passant, halfMoveClock, etc.)
    int startIndex = divider + 1;
    for (int i = 0; i < 5; i++) {
      int endIndex = startIndex;
      String info = "";
      while (endIndex < fenChars.length && fenChars[endIndex] != ' ') {
        info = info.concat(Character.toString(fenChars[endIndex]));
        endIndex++;
      }
      // System.out.println(i + ": " + info);
      switch (i) {
        // Find active color
        case 0:
          PieceColor color = (info.charAt(0) == 'w') ? PieceColor.WHITE : PieceColor.BLACK;
          this.setActiveColor(color);
          break;
        // Find castling rights
        case 1:
          // Assume the rook has moved until proven otherwise
          setBrq(false);
          setBrk(false);
          setWrq(false);
          setWrk(false);
          for (int j = 0; j < info.length(); j++) {
            char c = info.charAt(j);
            if (c == 'q') setBrq(true);
            if (c == 'k') setBrk(true);
            if (c == 'Q') setWrq(true);
            if (c == 'K') setWrk(true);
          }
          break;
        // Find en passant square
        case 2:
          if (info.charAt(0) == '-') {
            setEnPassantFile(-1);
            setEnPassantRank(-1);
            break;
          }
          if (info.length() < 2) throw new RuntimeException("En Passant square is incorrect");
          setEnPassantFile(info.charAt(0) - 'a');
          setEnPassantRank(Math.abs(8 - Character.getNumericValue(info.charAt(1))));
          break;
        // Set half clock
        case 3:
          setHalfMoveClock(Integer.parseInt(info));
          break;
        // Set full clock
        case 4:
          setFullMoveCount(Integer.parseInt(info));
          break;
        default:
          break;
      }
      startIndex = endIndex + 1;
    }

    // Piece Placement
    for (int i = 0; i < divider; i++) {
      char c = fenChars[i];
      if (c == '/') {
        file = 0;
        rank++;
        continue;
      }
      if (Character.isDigit(c)) {
        int digit = Character.getNumericValue(c);
        file += digit;
      } else {
        PieceColor pieceColor = (Character.isUpperCase(c)) ? PieceColor.WHITE : PieceColor.BLACK;
        char lowerCaseC = Character.toLowerCase(c);
        switch (lowerCaseC) {
          case 'p':
            boolean hasMoved = false;
            if (pieceColor == PieceColor.WHITE && rank != 6) hasMoved = true;
            if (pieceColor == PieceColor.BLACK && rank != 1) hasMoved = true;
            boolean enPassant = (rank == enPassantRank && file == enPassantFile);
            boardClone[rank][file] = new Pawn(new Position(rank, file), PieceType.PAWN, pieceColor, hasMoved, enPassant);
            break;
          case 'r':
            boolean hasRookMoved = true;
            if (file == 0 && rank == 0 && brq) hasRookMoved = false;
            if (file == 7 && rank == 0 && brk) hasRookMoved = false;
            if (file == 0 && rank == 7 && wrq) hasRookMoved = false;
            if (file == 7 && rank == 7 && wrk) hasRookMoved = false;
            boardClone[rank][file] = new Rook(new Position(rank, file), PieceType.ROOK, pieceColor, hasRookMoved);
            break;
          case 'k':
            boolean hasKingMoved = (pieceColor == PieceColor.WHITE && !(wrq || wrk)) ||
                                   (pieceColor == PieceColor.BLACK && !(brq || brk));
            boardClone[rank][file] = new King(new Position(rank, file), PieceType.KING, pieceColor, hasKingMoved);
            break;
          case 'b':
            boardClone[rank][file] = new Bishop(new Position(rank, file), PieceType.BISHOP, pieceColor);
            break;
          case 'q':
            boardClone[rank][file] = new Queen(new Position(rank, file), PieceType.QUEEN, pieceColor);
            break;
          case 'n':
            boardClone[rank][file] = new Knight(new Position(rank, file), PieceType.KNIGHT, pieceColor);
            break;
          default: break;
        }
        file++;
      }
    }
    this.setBoard(boardClone);
  }

  public boolean isBrq() {
    return brq;
  }

  public void setBrq(boolean brq) {
    this.brq = brq;
  }

  public boolean isBrk() {
    return brk;
  }

  public void setBrk(boolean brk) {
    this.brk = brk;
  }

  public boolean isWrq() {
    return wrq;
  }

  public void setWrq(boolean wrq) {
    this.wrq = wrq;
  }

  public boolean isWrk() {
    return wrk;
  }

  public void setWrk(boolean wrk) {
    this.wrk = wrk;
  }

  public Piece[][] getBoard() {
    return board;
  }

  public void setBoard(Piece[][] board) {
    this.board = board;
  }

  public PieceColor getActiveColor() {
    return activeColor;
  }

  public void setActiveColor(PieceColor activeColor) {
    this.activeColor = activeColor;
  }

  public int getEnPassantRank() {
    return enPassantRank;
  }

  public void setEnPassantRank(int enPassantRank) {
    this.enPassantRank = enPassantRank;
  }

  public int getEnPassantFile() {
    return enPassantFile;
  }

  public void setEnPassantFile(int enPassantFile) {
    this.enPassantFile = enPassantFile;
  }

  public int getHalfMoveClock() {
    return halfMoveClock;
  }

  public void setHalfMoveClock(int halfMoveClock) {
    this.halfMoveClock = halfMoveClock;
  }

  public int getFullMoveCount() {
    return fullMoveCount;
  }

  public void setFullMoveCount(int fullMoveCount) {
    this.fullMoveCount = fullMoveCount;
  }
}
