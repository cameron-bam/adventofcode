const readInput = require("../../common/readInput");

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

function runProgram(program, noun, verb) {
    const programCopy = program.slice(0);
    programCopy[1] = noun;
    programCopy[2] = verb;
    intcodeComputer(programCopy);

    return programCopy[0]
}

function readProgramFromFile(fileName) {
    return readInput(fileName)
    .then((data) => {
        return data
        .split(',')
        .map((intcode) => intcode.trim())
        .filter((intcode) => intcode.length > 0)
        .map((intcode) => parseInt(intcode));
    })
}

module.exports = {intcodeComputer, runProgram, readProgramFromFile}
