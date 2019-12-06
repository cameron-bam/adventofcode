const {intcodeComputer} = require("../lib/intcodeComputer");
const assert = require("assert");

const program1 = [3,9,8,9,10,9,4,9,99,-1,8];
const program2 = [3,9,7,9,10,9,4,9,99,-1,8];
const program3 = [3,3,1108,-1,8,3,4,3,99];
const program4 = [3,3,1107,-1,8,3,4,3,99];

describe("Day 5, part 2: Jump and Comparision instructions", () => {
    it(`Should return true for program [${program1}] and input equal to 8`, () => {
        const output = [];
        intcodeComputer(program1, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Excpected output to equal 1!");
    });

    it(`Should return false for program [${program1}] and input not equal to 8`, () => {
        const output = [];
        intcodeComputer(program1, [7], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Excpected output to equal 0!");
    });

    it(`Should return true for program [${program2}] and input less than 8`, () => {
        const output = [];
        intcodeComputer(program2, [7], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program2}] and input not less than 8`, () => {
        const output = [];
        intcodeComputer(program2, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return true for program [${program3}] and input equal to 8`, () => {
        const output = [];
        intcodeComputer(program3, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program3}] and input not equal to 8`, () => {
        const output = [];
        intcodeComputer(program3, [9], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return true for program [${program4}] and input less than 8`, () => {
        const output = [];
        intcodeComputer(program4, [6], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program4}] and input not less than 8`, () => {
        const output = [];
        intcodeComputer(program4, [9], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });
});