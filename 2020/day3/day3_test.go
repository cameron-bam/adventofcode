package day3

import "testing"

const testInput = `..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#`

type testCase struct {
	step coords
	want int
}

var testCases = []testCase{{coords{1, 1}, 2}, {coords{3, 1}, 7}, {coords{5, 1}, 3}, {coords{7, 1}, 4}, {coords{1, 2}, 2}}

func TestSolvePartOne(t *testing.T) {
	want := 7
	got := solvePartOne(testInput)

	if got != want {
		t.Errorf("solvePartOne(), wanted %d, got %d", want, got)
	}
}

func TestCountTrees(t *testing.T) {
	for _, tc := range testCases {
		got := countTrees(testInput, tc.step.x, tc.step.y)

		if got != tc.want {
			t.Errorf("countTrees(testInput,%d,%d), wanted %d, got %d", tc.step.x, tc.step.y, tc.want, got)
			break
		}
	}
}

func TestSolvePartTwo(t *testing.T) {
	want := 336
	got := solvePartTwo(testInput)

	if got != want {
		t.Errorf("solvePartTwo(), wanted %d, got %d", want, got)
	}
}
