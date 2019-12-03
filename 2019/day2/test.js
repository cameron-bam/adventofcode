const {intcodeComputer, runProgram} = require("./intcodeComputer");
const {nounVerbMath} = require("./part2.soln");
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

describe("Day 2, part 2 - find noun and verb that produce a value", () => {
    it("Should not modify instructions", () => {
        const instructions = [1,0,0,4,99,5,6,0,99];
        runProgram(instructions, 1, 1);
        assert.deepEqual(instructions, [1,0,0,4,99,5,6,0,99]);
    });

    it("Should run idempotently", () => {
        const instructions = [1,0,0,4,99,5,6,0,99];
        const res1 = runProgram(instructions, 1, 1);
        const res2 = runProgram(instructions, 1, 1);
        assert.strictEqual(res1, 30, "Expected result to be 30!");
        assert.strictEqual(res2, 30, "Expected executions to be idempotent!");
    });

    it("Should calculate 100 * noun + verb correctly", () => {
        assert.strictEqual(nounVerbMath(12, 2), 1202, "Expected 100 * 12 + 2 = 1202!");
    })
});