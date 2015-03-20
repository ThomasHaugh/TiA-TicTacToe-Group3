package c_minimax;

import java.util.HashMap;
//author: Gary Kalmanovich; rights reserved

public class TicTacToeStrategy implements InterfaceStrategy {

    private HashMap<Integer,Integer> hashedStates = new HashMap<Integer,Integer>();
    
    @Override
    public void getBestMove(InterfacePosition position, InterfaceSearchInfo context) {
        // Note, return information is embedded in context

        int player = position.getPlayer();
        int opponent = 3-player; // There are two players, 1 and 2.
        for ( InterfaceIterator iPos = new TicTacToeIterator(); iPos.isInBounds(); iPos.increment() ) {
            InterfacePosition posNew = new TicTacToePosition(position);
            if (posNew.getColor(iPos) == 0) { // This is a free spot
                posNew.setColor(iPos, player);
		float score = 0;
                int isWin = posNew.isWinner(hashedStates);
                // TODO Based on isWin, come up with a score or what to do to get the score
		posNew.setPlayer(opponent);
		
		if (isWin == player) {score = 1;}
		if (isWin ==  -1) {
		    InterfaceSearchInfo context2 = new TicTacToeSearchInfo();
		    getBestMove(posNew,context2);
		    score = -1*context2.getBestScoreSoFar();
		}

		if (score == 1) {
		    context.setBestMoveSoFar(iPos, score );
		    break;
		}
	       
                if (context.getBestScoreSoFar() <  score ) {
                    context.setBestMoveSoFar(iPos, score );
                }
	    }
	}
    }	    

    @Override
    public void setContext(InterfaceSearchInfo strategyContext) {
        // Not used in this strategy
    }

    @Override
    public InterfaceSearchInfo getContext() {
        // Not used in this strategy
        return null;
    }
}


class TicTacToeSearchInfo implements InterfaceSearchInfo {

    InterfaceIterator bestMoveSoFar  = null;
    float             bestScoreSoFar = Float.NEGATIVE_INFINITY;

    @Override
    public InterfaceIterator getBestMoveSoFar() {
        return bestMoveSoFar;
    }

    @Override
    public float getBestScoreSoFar() {
        return bestScoreSoFar;
    }

    @Override
    public void setBestMoveSoFar(InterfaceIterator newMove, float newScore) {
        bestMoveSoFar  = new TicTacToeIterator(newMove);
        bestScoreSoFar = newScore;
    }

    @Override
    public int getMinDepthSearchForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMinDepthSearchForThisPos(int minDepth) {
        // Not used in this strategy
    }

    @Override
    public int getMaxDepthSearchForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMaxDepthSearchForThisPos(int minDepth) {
        // Not used in this strategy
    }

    @Override
    public int getMaxSearchTimeForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMaxSearchTimeForThisPos(int maxTime) {
        // Not used in this strategy
    }

    @Override
    public float getOpponentBestScoreOnPreviousMoveSoFar() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setOpponentBestScoreOnPreviousMoveSoFar(float scoreToBeat) {
        // Not used in this strategy
    }

}
