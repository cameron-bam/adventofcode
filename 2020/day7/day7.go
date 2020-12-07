package day7

import (
	"fmt"
	"strconv"
	"strings"
)

type bagRules = map[string]map[string]int

func parseRules(input string) bagRules {
	rules := strings.Split(input, "\n")

	allRules := make(bagRules)

	for _, rule := range rules {
		rule = strings.Replace(rule, ".", "", -1)
		parts := strings.Split(rule, "contain")

		color := parts[0]
		color = color[:len(color)-6]

		conditions := make(map[string]int)

		allRules[color] = conditions

		for _, condition := range strings.Split(parts[1], ",") {
			condition = strings.TrimSpace(condition)

			if condition == "no other bags" {
				break
			}

			indexOfNum := strings.Index(condition, " ")
			endOfColor := strings.Index(condition, " bag")
			count, err := strconv.Atoi(condition[0:indexOfNum])

			if err != nil {
				panic(err)
			}

			condColor := condition[indexOfNum:]
			condColor = condColor[indexOfNum : endOfColor-1]

			conditions[condColor] = count
		}
	}

	return allRules
}

func bagContainsColor(rules bagRules, bagToCheck, bagToFind string) bool {
	conditions, exists := rules[bagToCheck]

	if !exists {
		return false
	}

	for innerBag := range conditions {
		if innerBag == bagToFind || bagContainsColor(rules, innerBag, bagToFind) {
			return true
		}
	}

	return false
}

func findAllContainingBags(rules bagRules, color string) int {
	total := 0

	for bag := range rules {
		if bagContainsColor(rules, bag, color) {
			total++
		}
	}

	return total
}

func countAllInnerBags(rules bagRules, color string) int {
	total := 0
	rule, exists := rules[color]

	if !exists {
		return total
	}

	for bag, number := range rule {
		total += number + number*countAllInnerBags(rules, bag)
	}

	return total
}

func partOne(input string) int {
	rules := parseRules(input)
	return findAllContainingBags(rules, "shiny gold")
}

func partTwo(input string) int {
	rules := parseRules(input)
	return countAllInnerBags(rules, "shiny gold")
}

// Solve prints solutions for https://adventofcode.com/2020/day/7
func Solve() {
	fmt.Printf("Day 7 solution, part I: %d\n", partOne(input))
	fmt.Printf("Day 7 solution, part II: %d\n", partTwo(input))
}
