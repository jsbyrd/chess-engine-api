package com.jsbyrd02.chessengineapi.engine.utils;

import com.jsbyrd02.chessengineapi.engine.pieces.Piece;

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
    Piece pieceCopy = boardClone[oldRank][oldFile];
    boardClone[newRank][newFile] = pieceCopy;
    // Modify piece info
    pieceCopy.setPosition(new Position(newRank, newFile));
    PieceType type = pieceCopy.getPieceType();
    if (type == PieceType.PAWN || type == PieceType.KING || type == PieceType.ROOK) {
      pieceCopy.setHasMoved(true);
    }

    // TODO: Add castling & en passant stuff
    // TODO: Remove enPassant/isCastle fields from Move, do calculations in getter methods instead
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
      int enPassantRank = oldRank;
      int enPassantFile = newFile;
      boardClone[enPassantRank][enPassantFile] = null;
    }

    return boardClone;
  }

  public static ArrayList<Move> findAllMoves(Piece[][] board, PieceColor activeColor) {
    if (board == null || board.length != 64) throw new RuntimeException("Invalid board passed in as a parameter");
    ArrayList<Move> allAttackMoves = new ArrayList<>();
    // Iterate through every "square" on the board
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece piece = board[rank][file];
        // If a piece occupies that rank and file, generate all attack moves and add it to allAttackMoves
        if (piece != null && piece.getPieceColor() == activeColor) {
          ArrayList<Move> moves = piece.generateMoves(board);
          for (int i = 0; i < moves.size(); i++) {
            allAttackMoves.add(moves.get(i));
          }
        }
      }
    }
    return allAttackMoves;
  }

  public static ArrayList<Move> findAllAttackMoves(Piece[][] board, PieceColor activeColor) {
    if (board == null || board.length != 64) throw new RuntimeException("Invalid board passed in as a parameter");
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
      throw new RuntimeException("allOpponentAttackMoves is no good");
    }

    // Find current player's King
    Piece king = null;
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece potentialPiece = board[rank][file];
        if (potentialPiece != null && potentialPiece.getPieceType() == PieceType.KING) {
          king = potentialPiece;
        }
      }
    }

    // Throw error if you couldn't find the king
    if (king == null) throw new RuntimeException("Couldn't find the king");

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

}