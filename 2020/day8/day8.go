package day8

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
)

type accumulator struct {
	val int
}

func (acc *accumulator) increment(val int) {
	acc.val += val
}

func processInstruction(program []string, visited map[int]int, acc *accumulator, loc int) (int, error) {
	_, hasVisited := visited[loc]

	if hasVisited {
		return -1, fmt.Errorf("Infinite loop detected! Location %d has already been visited", loc)
	}

	visited[loc] = 1

	instruction := strings.Split(program[loc], " ")
	code := instruction[0]
	val, err := strconv.Atoi(instruction[1])

	if err != nil {
		return -1, err
	}

	switch code {
	case "jmp":
		return loc + val, nil
	case "acc":
		acc.increment(val)
		fallthrough
	default:
		return loc + 1, nil
	}
}

func runProgram(input string) (int, error) {
	acc := accumulator{0}
	program := strings.Split(input, "\n")

	var err error
	var loc int = 0
	visited := make(map[int]int)
	exitLoc := len(program)

	for err == nil && loc < exitLoc {
		loc, err = processInstruction(program, visited, &acc, loc)
	}

	return acc.val, err
}

func partOne() int {
	acc, _ := runProgram(input)
	return acc
}

func fixProgram(input string) int {
	exp := regexp.MustCompile("(nop|jmp)")
	matches := exp.FindAllStringIndex(input, -1)

	for _, match := range matches {
		strMatch := input[match[0]:match[1]]
		begin := input[:match[0]]
		rest := input[match[0]:]

		if strMatch == "nop" {
			rest = strings.Replace(rest, "nop", "jmp", 1)
		} else {
			rest = strings.Replace(rest, "jmp", "nop", 1)
		}

		acc, err := runProgram(strings.Join([]string{begin, rest}, ""))

		if err == nil {
			return acc
		}
	}

	return -1
}

func partTwo() int {
	return fixProgram(input)
}

// Solve prints the solutions for https://adventofcode.com/2020/day/8
func Solve() {
	fmt.Printf("Day 8 solution, part I: %d\n", partOne())
	fmt.Printf("Day 8 solution, part II: %d\n", partTwo())
}
