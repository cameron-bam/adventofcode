package day4

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
)

type fieldValidator struct {
	required bool
	validate func(string) bool
}

var emptyValidator = func(field string) bool { return true }
var emptyRequiredValidator = fieldValidator{true, emptyValidator}
var emptyOptionalValidator = fieldValidator{false, emptyValidator}

func getReqFieldsPartOne() map[string]fieldValidator {

	fields := make(map[string]fieldValidator)

	fields["byr"] = emptyRequiredValidator
	fields["iyr"] = emptyRequiredValidator
	fields["eyr"] = emptyRequiredValidator
	fields["hgt"] = emptyRequiredValidator
	fields["hcl"] = emptyRequiredValidator
	fields["ecl"] = emptyRequiredValidator
	fields["pid"] = emptyRequiredValidator
	fields["cid"] = emptyOptionalValidator

	return fields
}

func getReqFieldsPartTwo() map[string]fieldValidator {
	fields := make(map[string]fieldValidator)

	fields["byr"] = fieldValidator{true, func(field string) bool {
		if len(field) != 4 {
			return false
		}

		year, err := strconv.Atoi(field)

		if err != nil {
			return false
		}

		return year >= 1920 && year <= 2002
	}}
	fields["iyr"] = fieldValidator{true, func(field string) bool {
		if len(field) != 4 {
			return false
		}

		year, err := strconv.Atoi(field)

		if err != nil {
			return false
		}

		return year >= 2010 && year <= 2020
	}}
	fields["eyr"] = fieldValidator{true, func(field string) bool {
		if len(field) != 4 {
			return false
		}

		year, err := strconv.Atoi(field)

		if err != nil {
			return false
		}

		return year >= 2020 && year <= 2030
	}}
	fields["hgt"] = fieldValidator{true, func(field string) bool {
		unit := field[len(field)-2:]
		height, err := strconv.Atoi(field[:len(field)-2])

		if err != nil {
			return false
		}

		switch unit {
		case "cm":
			return height >= 150 && height <= 193
		case "in":
			return height >= 59 && height <= 76
		default:
			return false
		}
	}}
	fields["hcl"] = fieldValidator{true, func(field string) bool {
		if field[0:1] != "#" {
			return false
		}

		color := field[1:]

		matched, _ := regexp.MatchString(`^[0-9a-f]{6}$`, color)

		return matched
	}}
	fields["ecl"] = fieldValidator{true, func(field string) bool {
		matched, _ := regexp.MatchString(`^(amb|blu|brn|gry|grn|hzl|oth)$`, field)
		return matched
	}}
	fields["pid"] = fieldValidator{true, func(field string) bool {
		matched, _ := regexp.MatchString(`^[0-9]{9}$`, field)
		return matched
	}}
	fields["cid"] = fieldValidator{false, nil}

	return fields
}

func validatePassport(passport string, reqFields map[string]fieldValidator) bool {
	re := regexp.MustCompile(`\s+`)
	fields := re.Split(passport, -1)

	for _, field := range fields {
		key := field[0:3]
		value := field[4:]

		validator := reqFields[key]

		if validator.required && !validator.validate(value) {
			return false
		}

		delete(reqFields, key)
	}

	for key := range reqFields {
		if reqFields[key].required {
			return false
		}
	}

	return true
}

func validatePassports(input string, reqFields func() map[string]fieldValidator) int {
	passports := strings.Split(input, "\n\n")
	var count = 0

	for _, passport := range passports {
		if validatePassport(passport, reqFields()) {
			count++
		}
	}

	return count
}

func solvePartOne() {
	fmt.Printf("Day 4 solution, part I: %d\n", validatePassports(input, getReqFieldsPartOne))
}

func solvePartTwo() {
	fmt.Printf("Day 4 solution, part II: %d\n", validatePassports(input, getReqFieldsPartTwo))
}

// Solve prints the solutions for https://adventofcode.com/2020/day/4
func Solve() {
	solvePartOne()
	solvePartTwo()
}
