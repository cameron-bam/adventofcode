package day3

import (
	"fmt"
	"strings"
)

type coords struct {
	x int
	y int
}

func countTrees(input string, xStep int, yStep int) int {
	rows := strings.Split(input, "\n")
	curPosition := coords{0, 0}
	trees := 0

	for i, row := range rows {
		if i < curPosition.y {
			continue
		}

		positions := strings.Split(row, "")

		for j, position := range positions {
			if j >= curPosition.x {
				if i == curPosition.y && j == curPosition.x && position == "#" {
					trees++
				}

				curPosition.y = (curPosition.y + yStep) % len(rows)
				curPosition.x = (curPosition.x + xStep) % len(positions)
				break
			}
		}
	}

	return trees
}

func solvePartTwo(input string) int {
	return countTrees(input, 1, 1) *
		countTrees(input, 3, 1) *
		countTrees(input, 5, 1) *
		countTrees(input, 7, 1) *
		countTrees(input, 1, 2)
}

func solvePartOne(input string) int {
	return countTrees(input, 3, 1)
}

// Solve prints the solution for https://adventofcode.com/2020/day/3
func Solve() {
	fmt.Printf("Day 3 Solution, part I: %d\n", solvePartOne(input))
	fmt.Printf("Day 3 Solution, part II: %d\n", solvePartTwo(input))
}
