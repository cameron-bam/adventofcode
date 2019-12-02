const readInput = require("../../common/readInput");

function add(program, pointer) {
    const addr1 = program[pointer + 1];
    const addr2 = program[pointer + 2];
    const addr3 = program[pointer + 3];
    const val = program[addr1] + program[addr2];
    program[addr3] = val;
    return pointer + 4;
}

function multiply(program, pointer) {
    const addr1 = program[pointer + 1];
    const addr2 = program[pointer + 2];
    const addr3 = program[pointer + 3];
    const val = program[addr1] * program[addr2];
    program[addr3] = val;
    return pointer + 4;
}

function intcodeComputer(program) {
    let pointer = 0;
    let lastPointer = -1

    while (program[pointer] != 99 && lastPointer != pointer) {
        lastPointer = pointer;
        switch(program[pointer]) {
            case 1: {
                pointer = add(program, pointer);
                break;
            }
            case 2: {
                pointer = multiply(program, pointer);
                break;
            }
            default:
                console.log(`Encountered unknown code ${program[pointer]} at position ${pointer}`);
                break;
        }
    }
}

function solve() {
   return readInput(__dirname + "/../input/day2/input")
    .then((data) => {
       return data
        .split(',')
        .map((intcode) => intcode.trim())
        .filter((intcode) => intcode.length > 0)
        .map(parseInt)
    })
    .then((program) => {
        program[1] = 12;
        program[2] = 2;
        intcodeComputer(program);
        const queryPosition = 0;
        console.log(`Day 2, part 1: Value at position ${queryPosition} is ${program[queryPosition]}`)
    })
}

module.exports = {intcodeComputer, solve}