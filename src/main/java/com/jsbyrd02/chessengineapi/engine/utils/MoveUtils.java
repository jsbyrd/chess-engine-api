package com.jsbyrd02.chessengineapi.engine.utils;

import com.jsbyrd02.chessengineapi.engine.Chessboard;
import com.jsbyrd02.chessengineapi.engine.pieces.King;
import com.jsbyrd02.chessengineapi.engine.pieces.Piece;
import com.jsbyrd02.chessengineapi.engine.pieces.Queen;

import java.util.ArrayList;

public class MoveUtils {
  public static Piece[][] simulateMove(Piece[][] board, Move move) {
    Piece[][] boardClone = new Piece[8][8];
    // Copy over current board onto a new board
    for (int rank = 0; rank < boardClone.length; rank++) {
      for (int file = 0; file < boardClone[rank].length; file++) {
        Piece potentialPiece = board[rank][file];
        boardClone[rank][file] = (potentialPiece != null) ? potentialPiece.deepCopy() : null;
      }
    }
    // Move piece to new location
    int oldRank = move.getOldPosition().getRank();
    int oldFile = move.getOldPosition().getFile();
    int newRank = move.getNewPosition().getRank();
    int newFile = move.getNewPosition().getFile();
    Piece pieceCopy = boardClone[oldRank][oldFile].deepCopy();
    boardClone[oldRank][oldFile] = null;

    // If it is a pawn promotion, auto-promote to Queen
    PieceType pieceType = pieceCopy.getPieceType();
    PieceColor pieceColor = pieceCopy.getPieceColor();
    if (pieceType == PieceType.PAWN && newRank == 0 && pieceColor == PieceColor.WHITE) {
      pieceCopy = new Queen(new Position(newRank, newFile), PieceType.QUEEN, pieceColor);
    }
    if (pieceType == PieceType.PAWN && newRank == 7 && pieceColor == PieceColor.BLACK) {
      pieceCopy = new Queen(new Position(newRank, newFile), PieceType.QUEEN, pieceColor);
    }
    boardClone[newRank][newFile] = pieceCopy;

    // Modify piece info
    pieceCopy.setPosition(new Position(newRank, newFile));
    PieceType type = pieceCopy.getPieceType();
    if (type == PieceType.PAWN || type == PieceType.KING || type == PieceType.ROOK) {
      pieceCopy.setHasMoved(true);
    }

    // If it is a castle move, also move the rook
    if (move.isCastleMove()) {
      int oldRookFile = (newFile == 2) ? 0 : 7;
      int oldRookRank = oldRank;
      int newRookFile = (newFile == 2) ? newFile + 1 : newFile - 1;
      int newRookRank = newRank;

      // Move Rook to new location
      pieceCopy = boardClone[oldRookRank][oldRookFile];
      boardClone[newRookRank][newRookFile] = pieceCopy;
      // Modify piece info
      pieceCopy.setPosition(new Position(newRookRank, newRookFile));
      pieceCopy.setHasMoved(true);
    }

    // If it is en passant, also remove the captured pawn
    if (move.isEnPassant()) {
      System.out.println("En Passant Move!");
      int enPassantRank = oldRank;
      int enPassantFile = newFile;
      boardClone[enPassantRank][enPassantFile] = null;
    }

    return boardClone;
  }

  public static ArrayList<Move> findAllMoves(Piece[][] board, PieceColor activeColor) {
    if (board == null) throw new RuntimeException("Invalid board passed in as a parameter");
    ArrayList<Move> allMoves = new ArrayList<>();
    // Iterate through every "square" on the board
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece piece = board[rank][file];
        // If a piece occupies that rank and file, generate all moves and add it to allMoves
        if (piece != null && piece.getPieceColor() == activeColor) {
          ArrayList<Move> moves = piece.generateMoves(board);
          allMoves.addAll(moves);
        }
      }
    }
    return allMoves;
  }

  public static ArrayList<Move> findAllAttackMoves(Piece[][] board, PieceColor activeColor) {
    if (board == null) throw new RuntimeException("Invalid board passed in as a parameter");
    ArrayList<Move> allAttackMoves = new ArrayList<>();
    // Iterate through every "square" on the board
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece piece = board[rank][file];
        // If a piece occupies that rank and file, generate all attack moves and add it to allAttackMoves
        if (piece != null && piece.getPieceColor() == activeColor) {
          ArrayList<Move> moves = piece.generateAttackMoves(board);
          for (int i = 0; i < moves.size(); i++) {
            allAttackMoves.add(moves.get(i));
          }
        }
      }
    }
    return allAttackMoves;
  }

  public static boolean isKingInCheck(Piece[][] board, PieceColor pieceColor) {
    PieceColor opponentColor = (pieceColor == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    ArrayList<Move> allOpponentAttackMoves = MoveUtils.findAllAttackMoves(board, opponentColor);

    if (allOpponentAttackMoves.size() == 0) {
      return false;
    }

    // Find current player's King
    Piece king = findKing(board, pieceColor);

    // Throw error if you couldn't find the king
    if (king == null) {
      return false;
    }

    // Check to see if the king's current position corresponds to a potential attack move from the other color
    boolean isKingAttacked = false;
    Position kingPosition = king.getPosition();
    for (Move attackMove : allOpponentAttackMoves) {
      Position movePosition = attackMove.getNewPosition();
      if (movePosition.isSamePosition(kingPosition)) {
        isKingAttacked = true;
      }
    }
    return isKingAttacked;
  }

  public static Piece findKing(Piece[][] board, PieceColor pieceColor) {
    // Find current player's King
    Piece king = null;
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece potentialPiece = board[rank][file];
        if (potentialPiece != null && potentialPiece.getPieceType() == PieceType.KING && potentialPiece.getPieceColor() == pieceColor) {
          king = potentialPiece;
        }
      }
    }
    return king;
  }
}
