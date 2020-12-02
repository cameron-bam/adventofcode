package day1

import "testing"

var input = []int{1721, 979, 366, 299, 675, 1456}

func TestFindProduct(t *testing.T) {
	want := 514579

	if got := findProduct(input, 2020); got != want {
		t.Errorf("Solve() = %d, want %d", got, want)
	}
}

func TestFindTripleProduct(t *testing.T) {
	want := 241861950

	if got := findTripleProduct(input, 2020); got != want {
		t.Errorf("Solve() = %d, want %d", got, want)
	}
}
