package c_minimax;

import java.util.HashMap;

import c_minimax.InterfaceIterator;
import c_minimax.InterfacePosition;
import c_minimax.InterfaceStrategy;
import c_minimax.TicTacToeIterator;
import c_minimax.TicTacToePosition;

//author: Gary Kalmanovich; rights reserved

public class TicTacToeStrategy implements InterfaceStrategy {
	
    private HashMap<Integer,Integer> hashedStates = new HashMap<Integer,Integer>();
	
    @Override
    public void getBestMove(InterfacePosition position, InterfaceSearchInfo context) {
        // Note, return information is embedded in context
    	
        int player = position.getPlayer();
        int opponent = 3-player; // There are two players, 1 and 2.
        for ( InterfaceIterator iPos = new TicTacToeIterator(); iPos.isInBounds(); iPos.increment() ) {
            InterfacePosition posNew = new TicTacToePosition(position,hashedStates);
            if (posNew.getColor(iPos) == 0) { // This is a free spot
                posNew.setColor(iPos, player);
                int isWin = posNew.isWinner();
                float score = 0;
                // TODO Based on isWin, come up with a score or what to do to get the score
                //iterate through all other positions if we don't have a win, loss or draw
                if(isWin == -1){
                	//define our stuff
                    posNew.setPlayer(opponent);

                    //create a new context so we can get its score from the children
                    InterfaceSearchInfo newContext = new TicTacToeSearchInfo();
                    getBestMove(posNew,newContext);
                    score = -1 * newContext.getBestScoreSoFar();
                }else{
                	score = isWin == 0 ? 0 : 1;
                }
                //we want a max 
                if(score > context.getBestScoreSoFar()){
                	context.setBestMoveSoFar(iPos,score);
                	if (score > 0)
                		return; //prune after finding the first good move
                }
                //reset the board so we can make the next move properly
                //posNew.setColor(iPos, 0);
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
