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
                // if this game state had been saved before
                if (hashedStates.containsKey(posNew.getRawPosition())) {
                	Integer savedScore = hashedStates.get(posNew.getRawPosition());
                	context.setBestMoveSoFar(iPos,savedScore);
                	((TicTacToePosition) posNew).insertIntoHash(hashedStates, 
                			((Long)posNew.getRawPosition()).intValue(), savedScore);
            		return; //prune after finding the saved best move
                } else {
	                int isWin = posNew.isWinner();
	                //if the game is finished
	            	if (isWin != -1) {
	            		if (isWin == player) {
	                		context.setBestMoveSoFar(iPos,1);
	                	} else if(isWin == opponent){
	                    	context.setBestMoveSoFar(iPos,-1);
	                    } else {
	                    	context.setBestMoveSoFar(iPos,0);
	                    }
	            		return;
	            	}
	                float score = 0;
	                //if the game is still going...
	                if(isWin == -1){
	                    posNew.setPlayer(opponent);
	                    //create a new context so we can get its score from the children
	                    InterfaceSearchInfo newContext = new TicTacToeSearchInfo();
	                    getBestMove(posNew,newContext);
	                    score = -1 * newContext.getBestScoreSoFar();
		                //we want a max 
		                if(score > context.getBestScoreSoFar()){
		                	((TicTacToeSearchInfo)context).setBestMoveSoFar(iPos,score, posNew);
		                	if (score == 1) {
		                		((TicTacToePosition) posNew).insertIntoHash(hashedStates, 
		                				((Long)posNew.getRawPosition()).intValue(), 1);
		                		return;
		                	}
		                }
	                }
                }
            }
        }
        if (player == 1) {
	        TicTacToePosition bestGameState = (TicTacToePosition) ((TicTacToeSearchInfo)context).getBestGameState();
	        if (bestGameState.getTurnCount() < 8) {
		        bestGameState.insertIntoHash(hashedStates, ((int)bestGameState.getRawPosition()), (int) context.getBestScoreSoFar());
	        }
        }
        return;
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
	
	InterfacePosition bestGameState  = null;
    InterfaceIterator bestMoveSoFar  = null;
    float             bestScoreSoFar = Float.NEGATIVE_INFINITY;
    
    public InterfacePosition getBestGameState() {
    	return bestGameState;
    }
    
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
    
    public void setBestMoveSoFar(InterfaceIterator newMove, float newScore, InterfacePosition gameState) {
        bestMoveSoFar  = new TicTacToeIterator(newMove);
        bestScoreSoFar = newScore;
        bestGameState  = gameState;
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
