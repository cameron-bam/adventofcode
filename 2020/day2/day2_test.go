package day2

import "testing"

type TestCase struct {
	input    string
	expected bool
}

var testInput = []TestCase{
	TestCase{`1-3 a: abcde`, true},
	TestCase{`1-3 b: cdefg`, false},
	TestCase{`2-9 c: ccccccccc`, true},
}

var testInputTwo = []TestCase{
	TestCase{`1-3 a: abcde`, true},
	TestCase{`1-3 b: cdefg`, false},
	TestCase{`2-9 c: ccccccccc`, false},
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
