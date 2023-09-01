package com.jsbyrd02.chessengineapi.rest;

public class PositionDetailsRequestModel {
  private String fen;
  private int depth;

  public String getFen() {
    return fen;
  }

  public void setFen(String fen) {
    this.fen = fen;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }
}
