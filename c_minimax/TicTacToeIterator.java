package c_minimax;

//author: Gary Kalmanovich; rights reserved

public class TicTacToeIterator implements InterfaceIterator {

    private int   iterator  = 0;

    TicTacToeIterator() {  } 
    TicTacToeIterator(InterfaceIterator iter) { this.set(iter); } 
    
    @Override public int          iC() { return iterator%3; }
    @Override public int          iR() { return iterator/3; }
    @Override public int          nC() { return          3; }
    @Override public int          nR() { return          3; }
    @Override public void  increment() {        iterator++; }
    @Override public void  resetBack() {      iterator = 0; }
    @Override public void set( InterfaceIterator iter ) { iterator = ((TicTacToeIterator)iter).iterator; }
    @Override public void set(int iC, int iR) { iterator = 3*iR+iC; }
    @Override public boolean isInBounds() { return 0<=iterator && iterator<3*3; }

}
