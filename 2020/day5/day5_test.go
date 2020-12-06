package day5

import "testing"

type testCase struct {
	pass string
	id   int
}

var testInput []testCase = []testCase{
	{"BFFFBBFRRR", 567},
	{"FFFBBBFRRR", 119},
	{"BBFFBBFRLL", 820}}

func TestGetId(t *testing.T) {
	for _, tc := range testInput {
		got := getID(tc.pass)

		if got != tc.id {
			t.Errorf("getId(%s), wanted %d, got %d\n", tc.pass, tc.id, got)
		}
	}
}
