package day5

import (
	"fmt"
	"strings"
)

func max(a, b int) int {
	if a > b {
		return a
	}

	return b
}

func min(a, b int) int {
	if a < b {
		return a
	}

	return b
}

func getID(pass string) int {
	var rowPow int = 6
	var colPow int = 2
	var rowID int = 0
	var colID int = 0

	for _, bit := range pass {
		switch string(bit) {
		case "B":
			rowID += 1 << rowPow
			fallthrough
		case "F":
			rowPow--
		case "R":
			colID += 1 << colPow
			fallthrough
		case "L":
			colPow--
		}
	}

	return rowID*8 + colID
}

func partOne() int {
	passes := strings.Split(input, "\n")
	curMax := 0

	for _, pass := range passes {
		curMax = max(curMax, getID(pass))
	}

	return curMax
}

func manageNeighbors(seats map[int]int, ID int) bool {
	neighbors, ok := seats[ID]

	if ok {
		if neighbors == 1 {
			delete(seats, ID)
		} else {
			seats[ID] = 1
		}
	}

	return ok
}

func partTwo() int {
	passes := strings.Split(input, "\n")
	seats := make(map[int]int)
	curMax := 0
	curMin := 10000

	for _, pass := range passes {
		ID := getID(pass)
		curMax = max(ID, curMax)
		curMin = min(ID, curMin)
		neighbors := 0

		if manageNeighbors(seats, ID-1) {
			neighbors++
		}

		if manageNeighbors(seats, ID+1) {
			neighbors++
		}

		if neighbors < 2 {
			seats[ID] = neighbors
		}
	}

	delete(seats, curMax)
	delete(seats, curMin)

	sum := 0

	for k := range seats {
		sum += k
	}

	return sum / 2
}

// Solve prints the solutions for https://adventofcode.com/2020/day/5
func Solve() {
	fmt.Printf("Day 5 solution, part I: %d\n", partOne())
	fmt.Printf("Day 5 solution, part II: %d\n", partTwo())
}
