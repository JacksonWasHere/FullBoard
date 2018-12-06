package fullboard;

import static sbcc.Core.*;

import java.io.*;
import java.util.*;

import static java.lang.System.*;
import static junit.framework.Assert.*;
import static org.apache.commons.lang3.StringUtils.*;

public class Board {
	String rawInput;
	char[][] mapData;
	int[] position;
	int width = 0;
	int moves = 0;
	char[] arrows = new char[] { '↓', '→', '↑', '←', };


	public Board(String rawInput, int width) {
		super();
		this.rawInput = rawInput;
		this.width = width;
		this.mapData = new char[width + 1][rawInput.length() / width - 1];

		for (var i = 0; i < this.mapData.length; i++) {
			for (var j = 0; j < this.mapData[i].length; j++) {
				this.mapData[i][j] = rawInput.charAt(j + i * this.mapData[i].length);
			}
		}
	}


	public boolean move(int direction) {
		var xdir = (direction >= 2 ? -1 : 1) * (direction + 1) % 2;
		var ydir = (direction >= 2 ? -1 : 1) * direction % 2;
		var changed = false;
		while (this.mapData[this.position[0] + xdir][this.position[1] + ydir] == ' ') {
			this.position[0] += xdir;
			this.position[1] += ydir;
			changed = true;
			this.mapData[this.position[0]][this.position[1]] = this.arrows[direction];
		}
		if (changed) {
			this.moves++;
		}
		return changed;
	}


	public boolean isValid() {
		var x = this.position[0];
		var y = this.position[1];
		var d = this.mapData;

		if (this.moves > 3) {
			var c = new char[d.length][d[0].length];
			for (var i = 0; i < d.length; i++)
				c[i] = d[i].clone();
			if (!islandCount(c)) {
				return false;
			}
		}

		if (d[x + 1][y] == ' ') {
			return true;
		} else if (d[x][y + 1] == ' ') {
			return true;
		} else if (d[x - 1][y] == ' ') {
			return true;
		} else if (d[x][y - 1] == ' ') {
			return true;
		}

		return false;
	}


	public boolean islandCount(char[][] d) {
		var count = 0;
		for (var i = 0; i < d.length; i++) {
			for (var j = 0; j < d[i].length; j++) {
				if (d[i][j] == ' ') {
					count++;
					if (count < 2) {
						merge(d, i, j);
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}


	public void merge(char[][] d, int i, int j) {
		int m = d.length;
		int n = d[0].length;

		if (i < 0 || i >= m || j < 0 || j >= n || d[i][j] != ' ')
			return;

		d[i][j] = 'X';

		merge(d, i, j - 1);
		merge(d, i + 1, j);
		merge(d, i, j + 1);
		merge(d, i - 1, j);
	}


	public Board copy() {
		var copy = new Board(this.rawInput, this.width);
		copy.mapData = this.mapData.clone();
		for (int i = 0; i < this.mapData.length; i++)
			copy.mapData[i] = this.mapData[i].clone();

		try {
			copy.position = this.position.clone();
		} catch (NullPointerException e) {
			copy.position = null;
		}
		copy.moves = this.moves;
		return copy;
	}


	@Override
	public String toString() {
		var builder = new StringBuilder();
		for (var i = 0; i < this.mapData.length; i++) {
			for (var j = 0; j < this.mapData[i].length; j++) {
				builder.append(this.mapData[i][j]);
			}
			builder.append('\n');
		}
		return builder.toString();
	}


	public boolean solved() {
		// TODO Create method to check if solved
		for (var i : this.mapData) {
			for (var j : i) {
				if (j == ' ') {
					return false;
				}
			}
		}
		return true;
	}

}
