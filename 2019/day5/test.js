const {intcodeComputer, runProgram} = require("../lib/intcodeComputer");
const assert = require("assert");

const program1 = [3,9,8,9,10,9,4,9,99,-1,8];
const program2 = [3,9,7,9,10,9,4,9,99,-1,8];
const program3 = [3,3,1108,-1,8,3,4,3,99];
const program4 = [3,3,1107,-1,8,3,4,3,99];
const program5 = [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9];
const program6 = [3,3,1105,-1,9,1101,0,0,12,4,12,99,1];
const program7 = [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
    1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
    999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99];

describe("Day 5, part 2: Jump and Comparision instructions", () => {
    it(`Should return true for program [${program1}] and input equal to 8`, () => {
        const output = [];
        runProgram(program1, undefined, undefined, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Excpected output to equal 1!");
    });

    it(`Should return false for program [${program1}] and input not equal to 8`, () => {
        const output = [];
        runProgram(program1, undefined, undefined, [7], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Excpected output to equal 0!");
    });

    it(`Should return true for program [${program2}] and input less than 8`, () => {
        const output = [];
        runProgram(program2, undefined, undefined, [7], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program2}] and input not less than 8`, () => {
        const output = [];
        runProgram(program2, undefined, undefined, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return true for program [${program3}] and input equal to 8`, () => {
        const output = [];
        runProgram(program3, undefined, undefined, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program3}] and input not equal to 8`, () => {
        const output = [];
        runProgram(program3, undefined, undefined, [9], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return true for program [${program4}] and input less than 8`, () => {
        const output = [];
        runProgram(program4, undefined, undefined, [6], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return false for program [${program4}] and input not less than 8`, () => {
        const output = [];
        runProgram(program4, undefined, undefined, [9], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return 0 for program [${program5}] and input equal to 0`, () => {
        const output = [];
        runProgram(program5, undefined, undefined, [0], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return 1 for program [${program5}] and input not equal to 0`, () => {
        const output = [];
        runProgram(program5, undefined, undefined, [10], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should return 0 for program [${program6}] and input equal to 0`, () => {
        const output = [];
        runProgram(program6, undefined, undefined, [0], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 0, "Expected output to equal 0!");
    });

    it(`Should return 1 for program [${program6}] and input not equal to 0`, () => {
        const output = [];
        runProgram(program6, undefined, undefined, [10], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1, "Expected output to equal 1!");
    });

    it(`Should output 999 for program [${program7}] and value less than 8`, () => {
        const output = [];
        runProgram(program7, undefined, undefined, [7], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 999, "Expected output 999!");
    });

    it(`Should output 1000 for program [${program7}] and value equal to 8`, () => {
        const output = [];
        runProgram(program7, undefined, undefined, [8], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1000, "Expected output 1000!");
    });

    it(`Should output 1001 for program [${program7}] and value greater than 8`, () => {
        const output = [];
        runProgram(program7, undefined, undefined, [9], output);
        assert.strictEqual(output.length, 1, "Expected exactly 1 output!");
        assert.strictEqual(output[0], 1001, "Expected output 1001!");
    })
});