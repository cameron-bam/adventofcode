package day6

import "testing"

const testInput = `abc

a
b
c

ab
ac

a
a
a
a

b`

func TestCountAnswers(t *testing.T) {
	want := 11
	got := countAnswers(testInput)

	if got != want {
		t.Errorf("countAnswers(testInput), wanted %d, got %d", want, got)
	}
}

func TestCountYesAnswers(t *testing.T) {
	want := 6
	got := countAllYesAnswers(testInput)

	if want != got {
		t.Errorf("countAllYesAnswers(testInput), wanted %d, got %d", want, got)
	}
}
