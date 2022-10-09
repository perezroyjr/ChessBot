package com.stephengware.java.games.chess.bot;

import java.util.*;
import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.*;

/**
 * A chess bot designed to enter a chess tournament.
 * 
 * @author Roy Perez
 */

public class rperez2 extends Bot {

	private Player me;
	private int depth = 3;
	/**
	 * Constructs a new chess bot named "rperez2"
	 */
	public rperez2() {
		super("rperez2");
	}

	@Override
	protected State chooseMove(State root) {
		
		me = root.player;
		Result bestState;
		
		depth = 3;
			
		if(root.player == me) {
			bestState = findMax(root, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}else {
			bestState = findMin(root, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		
		State s = bestState.state;
		
		while(s.previous != root){
			s = s.previous;
		}
		return s;
	
	}
	
	private Result findMax(State state, int depth, double alpha, double beta) {
		
		Result maxBestChild = null;
		
		if(state.over  || depth <= 0) {
			return new Result(state, evaluate(state));
		}
		
		double max = Double.NEGATIVE_INFINITY;
		
		Iterator<State> iterator = state.next().iterator();
		while(!state.searchLimitReached() && iterator.hasNext()) {
			State child = iterator.next();
			Result currentChild = new Result(child, evaluate(child));

			currentChild = findMin(currentChild.state,depth-1, alpha, beta);
			
			if(currentChild.utility > max) {
				max = currentChild.utility;
				maxBestChild = currentChild;
			}
			
			if (max >= beta) return maxBestChild;
			alpha = Math.max(alpha, max);
			}
		
		
		return maxBestChild;
	}
	
	private Result findMin(State state, int depth, double alpha, double beta) {
		
		Result minBestChild = null;
		
		if(state.over || depth <= 0) {
			return new Result(state, evaluate(state));
		}
		
		double min = Double.POSITIVE_INFINITY;
		
		Iterator<State> iterator = state.next().iterator();
		while(!state.searchLimitReached() && iterator.hasNext()) {
			State child = iterator.next();
			Result currentChild = new Result(child, evaluate(child));

			currentChild = findMax(currentChild.state, depth - 1, alpha, beta);
			
			if(currentChild.utility < min) {
				min = currentChild.utility;
				minBestChild = currentChild;
			}
			if(min <= alpha) return minBestChild;
			beta = Math.min(beta, min);
		}
		
		return minBestChild;
	}
	
	public double evaluate(State state) {
		
		double value = 0.0;
		
		for(Piece piece : state.board) {
			if(piece.player == me) {
				if(piece.getClass() == Pawn.class) {
					value += 10.0;
				}
				if(piece.getClass() == Knight.class) {
					value += 30.05;
				}
				if(piece.getClass() == Bishop.class) {
					value += 30.33;
				}
				if(piece.getClass() == Rook.class) {
					value += 50.63;
				}
				if(piece.getClass() == Queen.class) {
					value += 125;
				}if(state.check) {
					if(!state.over) {
						value += 200;
					}
				}
				if(state.board.contains(piece)) {
					value += 0;
				}
				if(state.board.countPieces() <= 12) {
					this.depth = 4;
				}
				if(state.board.countPieces() <= 6) {
					this.depth = 5;
				}
				if(state.movesUntilDraw > 0) {
					 value += 50;
				}if(state.check) {
					if(state.over) {
						value -= 10000;;
					}
				}
				if(state.turn == 0 && state.board.pieceAt(3,3,me, Pawn.class)) {
							value += 100;
						
				}if(state.board.pieceAt(3,3,me, Knight.class) || state.board.pieceAt(3,3,me, Bishop.class) ) {
					value += 5;
					
				}if(state.board.pieceAt(4,3,me, Knight.class) || state.board.pieceAt(4,3,me, Bishop.class) ) {
					value += 5;
				}
				if(state.turn == 0 && me == Player.BLACK) {
					if(state.board.pieceAt(3,3,me, Pawn.class))
						value += 100;	
				}
				if(me == Player.BLACK) {
					if(state.board.pieceAt(3,3,me, Knight.class) || state.board.pieceAt(3,3,me, Bishop.class) ) {
						value += 5;
					}
				}if(state.board.hasMoved(state.board.getKing(me))){
						value -= 20;
				}
				if(state.board.countPieces(me) > state.board.countPieces(me.other())) {
						value += 100;
				}
				}else {
				if(piece.getClass() == Pawn.class) {
					value -= 10.0;
				}
				if(piece.getClass() == Knight.class) {
					value -= 30.05;
				}
				if(piece.getClass() == Bishop.class) {
					value -= 30.33;
				}
				if(piece.getClass() == Rook.class) {
					value -= 70.63;
				}
				if(piece.getClass() == Queen.class) {
					value -= 300;
				}
				if(state.check) {
					if(state.over) {
						value += 10000;;
					}
				}
				if(state.board.contains(piece)) {
					value -= 0;
				}
				if(state.board.countPieces() <= 12) {
					this.depth = 4;
				}
				if(state.board.countPieces() <= 6) {
					this.depth = 5;
				}
				if(state.board.pieceAt(3,4,me, Knight.class) || state.board.pieceAt(3,4,me, Bishop.class) ) {
					value -= 20;
					
				}
				if(state.board.pieceAt(4,4,me, Knight.class) || state.board.pieceAt(4,4,me, Bishop.class) ) {
					value -= 20;
				}
			
			
			}
		}
		return value;
	}
	
	private static final class Result {
		
		public  State state;
		public  double utility;

		public Result(State state, double utility) { 	
			this.state = state;
			this.utility = utility;
		}
	}
}
