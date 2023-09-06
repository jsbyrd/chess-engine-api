package com.jsbyrd02.chessengineapi.engine.utils;

public class MoveWithScore {
  private Move move;
  private double score;

  public MoveWithScore(Move move, double score) {
    this.move = move;
    this.score = score;
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
