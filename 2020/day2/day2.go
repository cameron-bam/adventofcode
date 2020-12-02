package day2

import (
	"fmt"
	"strconv"
	"strings"
)

type rule struct {
	char string
	min  int
	max  int
}

func parseRules(rules string) rule {
	parts := strings.Split(rules, " ")
	charRange := strings.Split(parts[0], "-")

	min, e := strconv.Atoi(charRange[0])

	if e != nil {
		panic(e)
	}

	max, e := strconv.Atoi(charRange[1])

	if e != nil {
		panic(e)
	}

	return rule{parts[1], min, max}
}

func testPassword(input string) bool {
	parts := strings.Split(input, ":")
	rules := strings.TrimSpace(parts[0])
	password := strings.TrimSpace(parts[1])

	parsedRule := parseRules(rules)

	count := strings.Count(password, parsedRule.char)

	return count >= parsedRule.min && count <= parsedRule.max
}

func testPasswordTwo(input string) bool {
	parts := strings.Split(input, ":")
	rules := strings.TrimSpace(parts[0])
	password := strings.TrimSpace(parts[1])

	parsedRule := parseRules(rules)

	return (string(password[parsedRule.min-1]) == parsedRule.char) != (string(password[parsedRule.max-1]) == parsedRule.char)
}

func partOne() {
	var count = 0
	var passwordsToTest = strings.Split(input, "\n")

	for _, test := range passwordsToTest {
		if testPassword(test) {
			count++
		}
	}

	fmt.Printf("Day 2 Solution, part I: %d\n", count)
}

func partTwo() {
	var count = 0
	var passwordsToTest = strings.Split(input, "\n")

	for _, test := range passwordsToTest {
		if testPasswordTwo(test) {
			count++
		}
	}

	fmt.Printf("Day 2 Solution, part II: %d\n", count)
}

// Solve prints the solution for https://adventofcode.com/2020/day/2
func Solve() {
	partOne()
	partTwo()
}
