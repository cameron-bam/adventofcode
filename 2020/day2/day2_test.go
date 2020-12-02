package day2

import "testing"

type TestCase struct {
	input    string
	expected bool
}

var testInput = []TestCase{
	{`1-3 a: abcde`, true},
	{`1-3 b: cdefg`, false},
	{`2-9 c: ccccccccc`, true},
}

var testInputTwo = []TestCase{
	{`1-3 a: abcde`, true},
	{`1-3 b: cdefg`, false},
	{`2-9 c: ccccccccc`, false},
}

func TestTestPassword(t *testing.T) {
	for _, tc := range testInput {
		result := testPassword(tc.input)
		if result != tc.expected {
			t.Errorf(`testPassword(), expected %t, got %t`, tc.expected, result)
		}
	}
}

func TestTestPasswordTwo(t *testing.T) {
	for _, tc := range testInputTwo {
		result := testPasswordTwo(tc.input)
		if result != tc.expected {
			t.Errorf(`testPassword(), expected %t, got %t`, tc.expected, result)
		}
	}
}
