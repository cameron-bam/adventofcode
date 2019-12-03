const readInput = require("../../common/readInput");

function processInput(data) {
    return data
    .split(',')
    .map((intcode) => intcode.trim())
    .filter((intcode) => intcode.length > 0)
    .map((intcode) => parseInt(intcode));
}

function getAddresses(program, pointer) {
    return [program[pointer + 1], program[pointer + 2], program[pointer + 3]]
}

function add(program, pointer) {
    const addrs = getAddresses(program, pointer);
    const val = program[addrs[0]] + program[addrs[1]];
    program[addrs[2]] = val;
    return pointer + addrs.length + 1;
}

function multiply(program, pointer) {
    const addrs = getAddresses(program, pointer);
    const val = program[addrs[0]] * program[addrs[1]];
    program[addrs[2]] = val;
    return pointer + addrs.length + 1;
}

function intcodeComputer(program) {
    let pointer = 0;

    while (program[pointer] != 99) {
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
                return;
        }
    }
}

function solve() {
   return readInput(__dirname + "/../input/day2/input")
    .then(processInput)
    .then((program) => {
        program[1] = 12;
        program[2] = 2;
        intcodeComputer(program);
        const queryPosition = 0;
        console.log(`Day 2, part 1: Value at position ${queryPosition} is ${program[queryPosition]}`)
    })
}

module.exports = {intcodeComputer, solve, processInput}