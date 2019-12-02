const {intcodeComputer} = require("./part1.soln");
const assert = require("assert");

describe("Day 2, part 1 - intcode computer", () => {
    it("should add and store values", () => {
        const instructions = [1,0,0,0,99];
        intcodeComputer(instructions);
        assert.deepEqual(instructions, [2,0,0,0,99]);
    });

    it("should multiply and store values", () => {
        const instructions1 = [2,3,0,3,99];
        intcodeComputer(instructions1);
        assert.deepEqual(instructions1, [2,3,0,6,99]);

        const instructions2 = [2,4,4,5,99,0];
        intcodeComputer(instructions2);
        assert.deepEqual(instructions2, [2,4,4,5,99,9801]);
    });

    it("should process multiple instructions", () => {
        const instructions = [1,1,1,4,99,5,6,0,99];
        intcodeComputer(instructions);
        assert.deepEqual(instructions, [30,1,1,4,2,5,6,0,99]);
    });
});