package day8

import "testing"

const testInput = `nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6`

func TestRunProgram(t *testing.T) {
	want := 5
	got, _ := runProgram(testInput)

	if got != want {
		t.Errorf("runProgram(testInput), wanted %d, got %d", want, got)
	}
}

func TestFixProgram(t *testing.T) {
	want := 8
	got := fixProgram(testInput)

	if got != want {
		t.Errorf("fixProgram(testInput), wanted %d, got %d", want, got)
	}
}
