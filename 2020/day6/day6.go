package day6

import (
	"fmt"
	"strings"
)

func countAnswers(input string) int {
	forms := strings.Split(input, "\n")
	total := 0
	groupAnswers := make(map[string]int)

	for _, form := range forms {
		if form == "" {
			total += len(groupAnswers)
			groupAnswers = make(map[string]int)
			continue
		}

		for _, answer := range form {
			groupAnswers[string(answer)] = 1
		}
	}

	return total + len(groupAnswers)
}

func countAllYesAnswers(input string) int {
	forms := strings.Split(input, "\n")
	total := 0
	isFirstInGroup := true
	groupAnswers := make(map[string]int)

	for _, form := range forms {
		if form == "" {
			total += len(groupAnswers)
			groupAnswers = make(map[string]int)
			isFirstInGroup = true
			continue
		}

		if isFirstInGroup {
			for _, answer := range form {
				groupAnswers[string(answer)] = 1
			}

			isFirstInGroup = false
		} else {
			combinedYes := make(map[string]int)

			for _, answer := range form {
				_, exists := groupAnswers[string(answer)]

				if exists {
					combinedYes[string(answer)] = 1
				}
			}

			groupAnswers = combinedYes
		}
	}

	return total + len(groupAnswers)
}

func partOne() int {
	return countAnswers(input)
}

func partTwo() int {
	return countAllYesAnswers(input)
}

// Solve prints solutions for https://adventofcode.com/2020/day/6
func Solve() {
	fmt.Printf("Day 6 solution, part I: %d\n", partOne())
	fmt.Printf("Day 6 solution, part II: %d\n", partTwo())
}
