package day1

import "testing"

func TestSolve(t *testing.T) {
	want := "soln"

	if got := Solve(); got != want {
		t.Errorf("Solve() = %q, want %q", got, want)
	}
}
