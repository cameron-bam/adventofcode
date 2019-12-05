const readInput = require("../../common/readInput");

function getParam(paramIndex, program, pointer) {
    const opcode = program[pointer];
    const bit = Math.pow(2, paramIndex);
    const paramMode = (parseInt(Math.floor(opcode / 100).toString(), 2) & bit) / bit;
    
    const addr = program[pointer + 1 + paramIndex];

    if (paramMode == 1) return addr;

    return program[addr];
}

function add(program, pointer) {
    const val = getParam(0, program, pointer) + getParam(1, program, pointer);
    program[program[pointer + 3]] = val;
    return pointer + 4;
}

function multiply(program, pointer) {
    const val = getParam(0, program, pointer) * getParam(1, program, pointer);
    program[program[pointer + 3]] = val;
    return pointer + 4;
}

function readValue(program, pointer) {
    const paramVal = getParam(0, program, pointer);
    console.log(`Intcode Computer: read value ${paramVal}`);
    return pointer + 2;
}

function storeInput(program, pointer, userInput) {
    program[program[pointer + 1]] = userInput.shift();
    return pointer + 2;
}

function intcodeComputer(program, userInput = []) {
    let pointer = 0;

    while (program[pointer] != 99) {
        switch(program[pointer] % 100) {
            case 1: {
                pointer = add(program, pointer);
                break;
            }
            case 2: {
                pointer = multiply(program, pointer);
                break;
            }
            case 3: {
                pointer = storeInput(program, pointer, userInput);
                break;
            }
            case 4: {
                pointer = readValue(program, pointer);
                break;
            }
            default:
                console.log(`Encountered unknown code ${program[pointer]} at position ${pointer}`);
                return;
        }
    }
}

function runProgram(program, noun, verb, userInput = []) {
    const programCopy = program.slice(0);
    programCopy[1] = noun;
    programCopy[2] = verb;
    intcodeComputer(programCopy, userInput);

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
