package com.jsbyrd02.chessengineapi.engine.utils;

public class Position {
  private int rank;
  private int file;

  public Position(int rank, int file) {
    this.rank = rank;
    this.file = file;
  }

  public boolean isSamePosition(Position otherPosition) {
    return (this.rank == otherPosition.rank) && (this.file == otherPosition.file);
  }

  public static boolean isLegalPosition(Position position) {
    int rank = position.rank;
    int file = position.file;
    return (rank >= 0 && rank < 8 && file >= 0 && rank < 8);
  }

  public int getRank() {
    return rank;
  }

  public int getFile() {
    return file;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public void setFile(int file) {
    this.file = file;
  }
}
