package c_minimax;

import java.util.HashMap;

//author: Gary Kalmanovich; rights reserved

public class TicTacToePosition implements InterfacePosition {
    // This implementation packs the position into an int
    // The int is between 0 and 4^9-1. This is not very compact, but efficient
    // "Color" is convention, only integers are returned or set.
    // 0-empty, 1-x (cross), 2-o (nought)
    
    // Rightmost 18=9*2 store color (each 2 bits stores 0,1,2; so it is a little lossy)
    // Leftmost 1 bit stores player (1 or 2)

    private int position;
    
    TicTacToePosition() {
        position = 0;
    }

    TicTacToePosition( InterfacePosition pos ) {
        position = (int) pos.getRawPosition();
    }

    @Override public int nC() { return 3; }
    @Override public int nR() { return 3; }

    @Override
    public long getRawPosition() { 
        return position;
    }

    @Override
    public int getColor( InterfaceIterator iPos ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        return getColor( iC, iR);
    }

    public int getColor( int iC, int iR ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        // TODO Return a color at iC, iR from position (i.e., decode position)
        // position is simply an int that encodes the position and whose move it is.

	return (position >>> (iC*2 + iR*6)) & 3;
    }

    @Override
    public void setColor( InterfaceIterator iPos, int color ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        if ( getColor(iC,iR) != 0 ) { 
            System.err.println("Error: This position ("+iC+","+iR+") is already filled.");
        } else {
            // TODO add a "color" to the current position at iC, iR.
            // position is simply an int that encodes the position and whose move it is.
	    position = position | (color << (iC*2 + iR*6));
        }
    }

    //Begin added methods

    public int setColorUnsafe( int iC, int iR, int color, int pos) {
	//first zero out the correct bits via an and
	int flusher = (3 << (iC*2 + iR*6)) ^ (3 << (iC*2 + iR*6));
	pos = pos & flusher;
	pos = pos | (color << (iC*2 + iR*6));
	return pos;
    }
	    
    public int ver(int iC) {
	// Takes a column and checks if the ver is the same
	int retVal = 0;
	if ((getColor(iC,0) == getColor(iC,1)) && (getColor(iC,0) == getColor(iC,2))) { retVal = getColor(iC,0);}
	return retVal;
    }

    public int hor(int iR) {
	int retVal = 0;
	if ((getColor(0,iR) == getColor(1,iR)) && (getColor(0,iR) == getColor(2,iR))) { retVal = getColor(0,iR);}
	return retVal;
    }

    public boolean isDone() {
	boolean retVal = true;
	for( int i = 0; i < 3 ; i++) {
	    for( int j = 0; j < 3 ; j++) { 
		if (getColor(i,j) == 0) {retVal = false;}
	    }
	}
	return retVal;
    }

    //Due to the way Java works I am not making this return anything
    //but I think what the code did would be more clear if it did
    //basically it flips the position int across the y axis, which
    //Due to the way setColor works modifies the state by default
    public int flip(int pos) {
	for(int iR = 0; iR < 3;iR++){
	    int temp = getColor(0,iR);
	    pos = setColorUnsafe(0,iR, getColor(2,iR), pos);
	    pos = setColorUnsafe(2,iR, temp, pos);
	}
	return pos;
    }

    //Makes a 90 degree clockwise rotation -- not that direction matters
    //This is kind of ugly but who cares

    //  x1| x2 | x3    copy x1 into temp1 then x3 into temp2
    // _____________   put  x1 into position of x3
    //    |    | x4    set temp1 = temp2
    // _____________   copy x3 into temp2
    //    |    |       ect.
   
    public int rotate(int pos) {

	//First Do corners
	int temp1 = getColor(0,0);
	pos = setColorUnsafe(0,0,getColor(0,2),pos);
	int temp2 = getColor(2,0);

	pos = setColorUnsafe(2,0,temp1,pos);
	temp1 = temp2;
	temp2 = getColor(2,2);

	pos = setColorUnsafe(2,2,temp1,pos);
	temp1 = temp2;

	pos = setColorUnsafe(0,2,temp1,pos);

	// Now Sides
	temp1 = getColor(0,1);
	pos = setColorUnsafe(0,1,getColor(1,2),pos);
	temp2 = getColor(1,0);
	
	pos = setColorUnsafe(1,0,temp1,pos);
	temp1 = temp2;
	temp2 = getColor(2,1);

	pos = setColorUnsafe(2,1,temp1,pos);
	temp1 = temp2;

	pos = setColorUnsafe(1,2,temp1,pos);

	return pos;
    }

    //End added Methods

    @Override
    public int isWinner(HashMap<Integer,Integer> hashStates) {
        //      If winner, determine that and return winner, ("winner" is the color enumeration of the winner)
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1

        // TODO using the getColor(0..2,0..2) method, determine if there is a winner
        // a draw, or neither.
        // Return the "color" of the winner if there is a winner; 0 if draw; -1 if in play
	
	// Note that one of five squares must be occupied for there to be a winner
	// Namely the intersection of the top row, and the left column

	Integer hashedState = hashStates.get(position); 
	
	if ( hashedState != null) {
	    //System.out.println("Saw This Before!!!");
	    return hashedState.intValue();
	}
	else{
	    int winColor = 0;

	    // First Check the Diagonals
	    if( (getColor(0,0) == getColor(1,1)) && (getColor(0,0) == getColor(2,2))) {
		if(getColor(1,1) > 0) {winColor = getColor(1,1);}
	    }
	
	    if( (getColor(0,2) == getColor(1,1)) && (getColor(0,2) == getColor(2,0))) {
		if(getColor(1,1) > 0) {winColor = getColor(1,1);}
	    }

	    // Now Check the rows and columns
	    for(int i = 0; i < 3; i++) {
		if (winColor   > 0) {break;}
		if (ver(i) != 0) {winColor = ver(i);}
		if (hor(i) != 0) {winColor = hor(i);}
	    }

	    int retVal = 0;
	    
	    if (winColor > 0) { retVal = winColor;}
	    else {
		if (isDone()) {retVal = 0;}
		else {retVal = -1;}
	    }
	    int pos = position;
	    //We need four rotates with alternating flips
	    hashStates.put(pos,retVal);
	    pos = flip(pos);
	    hashStates.put(pos,retVal);
	    pos = rotate(pos); // 1
	    hashStates.put(pos,retVal);
	    pos = flip(pos);
	    hashStates.put(pos,retVal);
	    pos = rotate(pos); // 2
	    hashStates.put(pos,retVal);
	    pos = flip(pos);
	    hashStates.put(pos,retVal);
	    pos = rotate(pos); // 3
	    hashStates.put(pos,retVal);
	    pos = flip(pos);
	    hashStates.put(pos,retVal);
	    pos = rotate(pos); // 4
	    hashStates.put(pos,retVal);
	    
	    return retVal;
	}
    }

    @Override
    public void reset() {
        position = 0;
    }

    @Override
    public void setPlayer(int iPlayer) { // Only 1 or 2 are valid
        if ( !(0<iPlayer && iPlayer<3) ) {
            System.err.println("Error(TicTacToePosition::setPlayer): iPlayer ("+iPlayer+") out of bounds!!!");
        } else {
            int  currentPlayer = getPlayer();
            if ( currentPlayer != iPlayer ) {
                position ^= 1L << 31;
            }
        }
    }

    @Override
    public int getPlayer() {
        return ((int)(position>>>31))+1;
    }

    @Override
    public float valuePosition() {
        // Not yet used
        return 0;
    }

}