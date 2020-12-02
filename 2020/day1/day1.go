package day1

import (
	"fmt"
	"strconv"
	"strings"
)

func findProduct(input []int, target int) int {
	var vals map[int]int
	vals = make(map[int]int)

	for i := 0; i < len(input); i++ {
		cur := input[i]

		if _, ok := vals[target-cur]; ok {
			return (target - cur) * cur
		} else {
			vals[cur] = i
		}
	}

	return -1
}

func findTripleProduct(input []int, target int) int {
	for i := 0; i < len(input); i++ {
		cur := input[i]
		product := findProduct(input, target-cur)

		if product > -1 {
			return product * cur
		}
	}

	return -1
}

func readInput() []int {
	dataStr := strings.Fields(Input)

	var vals []int = []int{}

	for _, val := range dataStr {
		j, err := strconv.Atoi(val)

		if err != nil {
			panic(err)
		}

		vals = append(vals, j)
	}

	return vals
}

// Solve prints the solution for https://adventofcode.com/2020/day/1
func Solve() {
	fmt.Printf("Day 1 Solution, part I: %d\n", findProduct(readInput(), 2020))
	fmt.Printf("Day 1 Solution, part II: %d\n", findTripleProduct(readInput(), 2020))
}
