package fullboard;

public class Path {
	int[] moves = null;
	int[] start = null;
	int[] end = null;


	public Path copy() {
		return this;
	}


	public void move(int direction) {
		moves[moves.length - 1] = direction;
	}
}
