package gui.backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



/**
 * The SudokuChecker class is responsible for calculating the solution to a
 * given Sudoku puzzle.
 * 
 * TODO: The Sudoku algorithm used is efficient and effective for solving easy
 * and medium puzzles, but it is not optimized for hard puzzles. It is
 * recommended to use a different algorithm for hard puzzles, likely a tree &
 * back-track approach.
 */
public class SudokuChecker {
	private Cell[][] grid;
	private Cell[][] origGrid;

	/**
	 * Create a new SudokuChecker object, initializing the grid to the given 9x9
	 * grid of numbers. This should either check each cell as the user inputs a
	 * value, or be used to check the validity of a puzzle when the user requests
	 * it.
	 * 
	 * @param grid
	 */
	public SudokuChecker(Cell[][] grid) {
		this.grid = grid;

		// Create a copy of the original grid to be used for resetting the
		// grid to its original state.
		origGrid = new Cell[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				origGrid[i][j] = new Cell(i, j, grid[i][j].getValue());
			}
		}
	}

	/**
	 * Check if the given value can be placed in the given cell of the grid.
	 * 
	 * False means that the value is incorrect, and true means that the value is
	 * correct.
	 * 
	 * @param row
	 * @param col
	 * @param value
	 * @return boolean
	 */
	public boolean checkValue(int row, int col, int value) {
		// Check the row
		for (int i = 0; i < 9; i++) {
			if (grid[row][i].getValue() == value && i != col)
				return false;
		}

		// Check the column
		for (int i = 0; i < 9; i++) {
			if (grid[i][col].getValue() == value && i != row)
				return false;
		}

		// Check the box
		int boxRow = row / 3;
		int boxCol = col / 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[boxRow * 3 + i][boxCol * 3 + j].getValue() == value
						&& (boxRow * 3 + i != row || boxCol * 3 + j != col)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Get the possible values for each cell in the Sudoku puzzle.
	 * 
	 * Intended to be used for auto-filling in possible values in the GUI.
	 * 
	 * @return
	 */
	public Cell[][] getPossibleValues(Cell[][] grid) {
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col].getValue() != 0)
					continue;

				// Replace intersection with union?
				ArrayList<Integer> intersection = intersection(getRowRemainingNumbers(row),
						getColRemainingNumbers(col));

				intersection = intersection(intersection, getBoxRemainingNumbers(row, col));

				// Join the arrays and find the intersection of the three arrays
				ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
				for (int i = 0; i < intersection.size(); i++) {
					availableNumbers.add(intersection.get(i));
				}

				grid[row][col].setPossibleValues(arrayListToArray(availableNumbers));
			}
		}

		this.grid = grid;
		return grid;
	}

	/**
	 * Get the solution to the Sudoku puzzle.
	 * 
	 * @return Cell[][]
	 */
	public Cell[][] getSolution() {
		solve();
		return grid;
	}

	/**
	 * Given two arrays of numbers, return the intersection of the two arrays.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private ArrayList<Integer> intersection(ArrayList<Integer> a, ArrayList<Integer> b) {
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i = 0; i < a.size(); i++) {
			if (b.contains(a.get(i)))
				intersection.add(a.get(i));
		}

		return intersection;
	}

	/**
	 * Determine what numbers are available to be placed in the given cell of the
	 * grid. This is effectively an intersection of the numbers available in the
	 * row, column, and box of the cell.
	 * 
	 * @param row
	 * @param col
	 * @return an array of numbers that are available to be placed in the given cell
	 */
	private void getAvailableNumbers(int row, int col) {
		ArrayList<Integer> intersection = intersection(getRowRemainingNumbers(row), getColRemainingNumbers(col));

		intersection = intersection(intersection, getBoxRemainingNumbers(row, col));

		if (intersection.size() == 0)
			return;
		else if (intersection.size() == 1) {
			int value = intersection.get(0);

			grid[row][col].setValue(value, true);
			updatePossibleValues(row, col);
			return;
		} else {
			// Join the arrays and find the intersection of the three arrays.
			ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
			for (int i = 0; i < intersection.size(); i++)
				availableNumbers.add(intersection.get(i));

			grid[row][col].setPossibleValues(arrayListToArray(availableNumbers));
		}
	}

	/**
	 * Update the possible values for cells in the same row, column, and box as the
	 * given cell. This should always be called once a cell's value has been set, to
	 * remove that value from the possible values of other cells.
	 * 
	 * @param row
	 * @param col
	 */
	private void updatePossibleValues(int row, int col) {
		int value = grid[row][col].getValue();

		for (int i = 0; i < 9; i++) {
			if (grid[row][i].getValue() == 0) {
				grid[row][i].removePossibleValue(value);
				if (grid[row][i].getPossibleValues().length == 1) {
					grid[row][i].setValue(grid[row][i].getPossibleValues()[0], true);
					updatePossibleValues(row, i);
				}
			}
			if (grid[i][col].getValue() == 0) {
				grid[i][col].removePossibleValue(value);
				if (grid[i][col].getPossibleValues().length == 1) {
					grid[i][col].setValue(grid[i][col].getPossibleValues()[0], true);
					updatePossibleValues(i, col);
				}
			}
		}

		int boxRow = row / 3;
		int boxCol = col / 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (grid[boxRow * 3 + i][boxCol * 3 + j].getValue() == 0)
					grid[boxRow * 3 + i][boxCol * 3 + j].removePossibleValue(value);
			}
		}
	}

	/**
	 * Solve the Sudoku puzzle.
	 * 
	 * @param grid
	 */
	private boolean solve() {
		return solve(0, 0);
	}

	/**
	 * Overloaded method that solves the Sudoku puzzle moving from the position given to the end
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean solve(int row, int col) {
		int nextCol = (col + 1) % 9;
		int nextRow = (nextCol == 0) ? row + 1 : row;
		// Base case - progressed past the last row and column.
		if (row == 9) {
//			grid.printTable();
			return true;
		}
		// If the cell has a value it is skipped.
		if (grid[row][col].getValue() != 0) {
			solve(nextRow, nextCol);
		} else {
			// An empty cell prompts a generation of possible numbers
			List<Integer> possibleNumbers = getAllRemainingNumbers(row, col);
			if (possibleNumbers.size() == 0) // If there are no available numbers the solve() returns false
				return false;
			// each possible number is given in ascending order
			for (Integer el : possibleNumbers) {
				grid[row][col].setValue(el.intValue(), true);
					
				// here is the check to see if the next possible number needs to be tested
				if (solve(nextRow, nextCol))
					return true;
				// reset the cell so that it is not assumed to be solved after failing the
				// current tested values }
			}
		}
		return false;
	}
	
	/**
	 * This may be redundant but I needed an method that could be called to get a list of all
	 * remaining numbers after eliminating 1-9 by standard Sudoku rules.
	 * This method calls the 3 methods already in this class to build a HashSet of 
	 * known values which are used to verify which numbers remain as possible solutions.
	 * @param row
	 * @param col
	 * @return ArrayList<Integer> results;
	 */
	private ArrayList<Integer> getAllRemainingNumbers(int row, int col) {
		HashSet<Integer> nums = new HashSet<>();
		ArrayList<Integer> results = new ArrayList<>();
		nums.addAll(getColRemainingNumbers(col));
		nums.addAll(getRowRemainingNumbers(row));
		nums.addAll(getBoxRemainingNumbers(row, col));
		for (int i = 1; i <= 9; i++) {
			if (!nums.contains(i)) {
				results.add(i);
			}
		}
		return results;
	}

	/**
	 * Get any number between 1 and 9 that is not in the row.
	 * 
	 * @param row
	 * @return
	 */
	private ArrayList<Integer> getRowRemainingNumbers(int row) {
		ArrayList<Integer> remainingNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++) {
			boolean found = false;
			for (int j = 0; j < 9; j++) {
				if (grid[row][j].getValue() == i) {
					found = true;
					break;
				}
			}
			if (!found)
				remainingNumbers.add(i);
		}

		return remainingNumbers;
	}

	/**
	 * Get any number between 1 and 9 that is not in the column.
	 * 
	 * @param col
	 * @return
	 */
	private ArrayList<Integer> getColRemainingNumbers(int col) {
		ArrayList<Integer> remainingNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++) {
			boolean found = false;
			for (int j = 0; j < 9; j++) {
				if (grid[j][col].getValue() == i) {
					found = true;
					break;
				}
			}
			if (!found)
				remainingNumbers.add(i);
		}

		return remainingNumbers;
	}

	/**
	 * Get any number between 1 and 9 that is not in the box.
	 * 
	 * A box is a 3x3 subgrid of the 9x9 grid.
	 * 
	 * @param box
	 * @return
	 */
	private ArrayList<Integer> getBoxRemainingNumbers(int row, int col) {
		ArrayList<Integer> remainingNumbers = new ArrayList<Integer>();
		int boxRow = row / 3;
		int boxCol = col / 3;
		for (int i = 1; i <= 9; i++) {
			boolean found = false;
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					if (grid[boxRow * 3 + j][boxCol * 3 + k].getValue() == i) {
						found = true;
						break;
					}
				}
			}
			if (!found)
				remainingNumbers.add(i);
		}

		return remainingNumbers;
	}


	/**
	 * Given a list of numbers, return an array of the numbers.
	 * 
	 * A helper method to keep the code using arrays instead of lists whenever
	 * possible.
	 * 
	 * @param list
	 * @return
	 */
	private int[] arrayListToArray(ArrayList<Integer> list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			array[i] = list.get(i);

		return array;
	}

	/**
	 * Given a 9x9 grid of numbers, return true if the grid is a valid Sudoku puzzle
	 * solution, and false otherwise.
	 * 
	 * A valid Sudoku puzzle is one where each row, column, and 3x3 subgrid contains
	 * the numbers 1-9 exactly once.
	 * 
	 * @return true if the grid is a valid Sudoku puzzle, and false otherwise
	 */
	private boolean isValidSolution() {
		// Check that every cell has a value between 1 and 9.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j].getValue() < 1 || grid[i][j].getValue() > 9)
					return false;
			}
		}

		// Check that every row contains the numbers 1-9 exactly once.
		for (int i = 0; i < 9; i++) {
			int[] row = new int[9];
			for (int j = 0; j < 9; j++)
				row[j] = grid[i][j].getValue();

			if (!isValidSet(row)) {
				System.out.println("Row " + i + " is invalid.");
				return false;
			}
		}

		// Check that every column contains the numbers 1-9 exactly once.
		for (int i = 0; i < 9; i++) {
			int[] col = new int[9];
			for (int j = 0; j < 9; j++)
				col[j] = grid[j][i].getValue();

			if (!isValidSet(col)) {
				System.out.println("Column " + i + " is invalid.");
				return false;
			}
		}

		// Check that every 3x3 subgrid contains the numbers 1-9 exactly once.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int[] box = new int[9];
				for (int k = 0; k < 3; k++) {
					for (int l = 0; l < 3; l++)
						box[k * 3 + l] = grid[i * 3 + k][j * 3 + l].getValue();
				}
				if (!isValidSet(box)) {
					System.out.println("Box at row " + i + " and column " + j + " is invalid.");
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Given an array of 9 numbers, return true if the array contains the numbers
	 * 1-9 exactly once, and false otherwise.
	 * 
	 * @param set an array of 9 numbers
	 * @return true if the array contains the numbers 1-9 exactly once, and false
	 *         otherwise
	 */
	private boolean isValidSet(int[] set) {
		boolean[] found = new boolean[9];
		for (int i = 0; i < 9; i++) {
			if (set[i] < 1 || set[i] > 9)
				return false;

			else if (found[set[i] - 1])
				return false;

			else
				found[set[i] - 1] = true;

		}
		return true;
	}
}