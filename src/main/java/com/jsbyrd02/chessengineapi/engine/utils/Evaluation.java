package com.jsbyrd02.chessengineapi.engine.utils;

import com.jsbyrd02.chessengineapi.engine.pieces.Piece;

import java.util.HashMap;

import static com.jsbyrd02.chessengineapi.engine.utils.MoveUtils.findAllMoves;

public class Evaluation {
  // Gives a positive score for white winning, negative score for black winning
  public static double evaluatePosition(Piece[][] board) {
    // Score = PieceTypeMultiplier(P-P') -0.5(Bad Pawns (Stacked, Doubled, Blocked)) +0.1(Mobility)
    // Keeps track of doubled/stacked pawns
    HashMap<Integer, Integer> whiteDoubledPawns = new HashMap<>();
    HashMap<Integer, Integer> blackDoubledPawns = new HashMap<>();
    double score = 0;

    // Calculate material score
    for (int rank = 0; rank < board.length; rank++) {
      for (int file = 0; file < board[rank].length; file++) {
        Piece potentialPiece = board[rank][file];
        if (potentialPiece == null) continue;

        int addOrSubFromScore = (potentialPiece.getPieceColor() == PieceColor.WHITE) ? 1 : -1;
        PieceType pieceType = potentialPiece.getPieceType();
        switch (pieceType) {
          case QUEEN -> score += 9 * addOrSubFromScore;
          case ROOK -> score += 5 * addOrSubFromScore;
          case BISHOP, KNIGHT -> score += 3 * addOrSubFromScore;
          case PAWN -> {
            score += 1 * addOrSubFromScore;
            int pawnFile = potentialPiece.getPosition().getFile();
            int currentFileCount;
            if (potentialPiece.getPieceColor() == PieceColor.WHITE) {
              currentFileCount = whiteDoubledPawns.getOrDefault(pawnFile, 0);
              whiteDoubledPawns.put(pawnFile, currentFileCount + 1);
            } else {
              currentFileCount = blackDoubledPawns.getOrDefault(pawnFile, 0);
              blackDoubledPawns.put(pawnFile, currentFileCount + 1);
            }
            score += 1 * addOrSubFromScore;
          }
          default -> {
          }
        }
      }
    }
    // Calculate score from doubled pawns
    for (int file = 0; file < board.length; file++) {
      int numWhitePawnsOnFile = whiteDoubledPawns.getOrDefault(file, 0);
      int numBlackPawnsOnFile = blackDoubledPawns.getOrDefault(file, 0);
      score -= numWhitePawnsOnFile * 0.5;
      score += numBlackPawnsOnFile * 0.5;
    }

    int numLeftPawns;
    int numPawns;
    int numRightPawns;
    // Calculate score from isolated pawns (white pawns)
    for (int file = 0; file < board.length; file++) {
      numPawns = whiteDoubledPawns.getOrDefault(file, 0);
      if (numPawns == 0) continue;
      if (file == 0) {
        numRightPawns = whiteDoubledPawns.getOrDefault(file + 1, 0);
        if (numRightPawns == 0) score -= 0.5 * numPawns;
        continue;
      }
      if (file == 7) {
        numLeftPawns = whiteDoubledPawns.getOrDefault(file - 1, 0);
        if (numLeftPawns == 0) score -= 0.5 * numPawns;
        continue;
      }
      numLeftPawns = whiteDoubledPawns.getOrDefault(file - 1, 0);
      numRightPawns = whiteDoubledPawns.getOrDefault(file + 1, 0);
      if (numLeftPawns == 0 && numRightPawns == 0) score -= 0.5 * numPawns;
    }
    // Calculate score from isolated pawns (black pawns)
    for (int file = 0; file < board.length; file++) {
      numPawns = blackDoubledPawns.getOrDefault(file, 0);
      if (numPawns == 0) continue;
      if (file == 0) {
        numRightPawns = blackDoubledPawns.getOrDefault(file + 1, 0);
        if (numRightPawns == 0) score += 0.5 * numPawns;
        continue;
      }
      if (file == 7) {
        numLeftPawns = blackDoubledPawns.getOrDefault(file - 1, 0);
        if (numLeftPawns == 0) score += 0.5 * numPawns;
        continue;
      }
      numLeftPawns = blackDoubledPawns.getOrDefault(file - 1, 0);
      numRightPawns = blackDoubledPawns.getOrDefault(file + 1, 0);
      if (numLeftPawns == 0 && numRightPawns == 0) score += 0.5 * numPawns;
    }

    // Calculate mobility score
    int numWhiteMoves = MoveUtils.findAllMoves(board, PieceColor.WHITE).size();
    int numBlackMoves = MoveUtils.findAllMoves(board, PieceColor.BLACK).size();
    score += numWhiteMoves * 0.1;
    score -= numBlackMoves * 0.1;

    return score;
  }
}
