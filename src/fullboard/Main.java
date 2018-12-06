package fullboard;

import static sbcc.Core.*;

import java.io.*;
import java.util.*;

import sbcc.*;

import static java.lang.System.*;
import static junit.framework.Assert.*;
import static org.apache.commons.lang3.StringUtils.*;

public class Main {

	static boolean found = false;


	public static void main(String[] args) throws IOException {
		var filename = readLine();
		List<String> file;
		try {
			file = readFileAsLines(filename);
		} catch (Exception e) {
			println("File not found.");
			println("Complete");
			return;
		}

		var maps = generateMaps(file);
		var s = nanoTime();
		for (var m : maps) {
			solveBoard(m);
		}
		// println((nanoTime() - s) / 1e6);
		println("Complete");

	}


	public static void solveBoard(Board main) {
		found = false;

		var maps = new ArrayList<Board>();
		var newMaps = new ArrayList<Board>();
		getChildren(main, maps);

		while (!found && maps.size() > 0) {
			newMaps = new ArrayList<Board>();
			for (var i = 0; i < maps.size(); i++) {
				getChildren(maps.get(i), newMaps);
			}
			maps = newMaps;
		}

		var builder = new StringBuilder();
		var moves = 0;

		Collections.sort(maps, new LexicographicComparator());

		if (maps.size() == 0) {
			builder.append(main.toString());
		} else {
			for (var b : maps) {
				if (b.solved()) {
					moves = b.moves;
					builder.append("solution\n");
					builder.append(b.toString());
					builder.append("endsolution\n");
				}
			}
		}

		builder.append("endmap");
		println("map\n" + (moves == 0 ? "No solution\n" : moves + " moves\n") + builder.toString());
	}


	public static void getChildren(Board b, ArrayList<Board> children) {
		if (b.position == null) {
			for (var i = 0; i < b.mapData.length; i++) {
				for (var j = 0; j < b.mapData[i].length; j++) {
					if (b.mapData[i][j] == ' ') {
						var newMap = b.copy();
						newMap.position = new int[] { i, j };
						newMap.mapData[i][j] = 'S';
						children.add(newMap);
					}
				}
			}
		} else {
			for (var i = 0; i < 4; i++) {
				var newMap = b.copy();
				makeMoves(newMap, children, i);
			}

		}
	}


	public static void makeMoves(Board newMap, ArrayList<Board> children, int i) {
		if (newMap.move(i)) {
			/*
			 * if (newMap.moves < newMap.width / 2) { children.add(newMap); } else
			 */if (newMap.isValid()) {
				children.add(newMap);
			} else if (newMap.solved()) {
				newMap.mapData[newMap.position[0]][newMap.position[1]] = 'F';
				children.add(newMap);
				found = true;
			}
		}
	}


	public static ArrayList<Board> generateMaps(List<String> file) {
		var builder = new StringBuilder();
		var boards = new ArrayList<Board>();
		var width = 0;
		for (var line : file) {
			line = line.trim();
			if (line.equals("map")) {
				builder = new StringBuilder();
			} else if (line.equals("endmap")) {
				boards.add(new Board(builder.toString(), width - 1));
			} else {
				builder.append(line);
				width = line.length();
			}
		}
		return boards;
	}

}

class LexicographicComparator implements Comparator<Board> {
	@Override
	public int compare(Board o1, Board o2) {
		// TODO Auto-generated method stub
		return o1.toString().compareTo(o2.toString());
	}
}
