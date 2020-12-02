const readInput = require("../common/readInput");

const UNKNOWN_INSTRUCTION = "UNKNOWN_CODE";
const USER_INPUT_INTERRUPT = "USER_INPUT_INTERRUPT";
const OK = "OK";

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

function readValue(program, pointer, userOutput) {
    userOutput.push(getParam(0, program, pointer));
    return pointer + 2;
}

function storeInput(program, pointer, userInput) {
    if (userInput.length === 0) throw new Error("User input expected!");
    program[program[pointer + 1]] = userInput.shift();
    return pointer + 2;
}

function jumpIfTrue(program, pointer) {
    const param1 = getParam(0, program, pointer);
    if (param1 != 0) return getParam(1, program, pointer);
    return pointer + 3;
}

function jumpIfFalse(program, pointer) {
    const param1 = getParam(0, program, pointer);
    if (param1 === 0) return getParam(1, program, pointer);
    return pointer + 3;
}

function lessThan(program, pointer) {
    program[program[pointer + 3]] = getParam(0, program, pointer) < getParam(1, program, pointer) ? 1 : 0;
    return pointer + 4;
}

function equals(program, pointer) {
    program[program[pointer + 3]] = getParam(0, program, pointer) === getParam(1, program, pointer) ? 1 : 0;
    return pointer + 4;
}

function intcodeComputer(program, userInput = [], userOutput = [], pointer = 0) {
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
                try {
                    pointer = storeInput(program, pointer, userInput);
                } catch (e) {
                    return {exitCode: USER_INPUT_INTERRUPT, pointer};
                }
                break;
            }
            case 4: {
                pointer = readValue(program, pointer, userOutput);
                break;
            }
            case 5: {
                pointer = jumpIfTrue(program, pointer);
                break;
            }
            case 6: {
                pointer = jumpIfFalse(program, pointer);
                break;
            }
            case 7: {
                pointer = lessThan(program, pointer);
                break;
            }
            case 8: {
                pointer = equals(program, pointer);
                break;
            }
            default:
                console.log(`Encountered unknown code ${program[pointer]} at position ${pointer}`);
                return {exitCode: UNKNOWN_INSTRUCTION, pointer};
        }
    }

    return {exitCode: OK, pointer};
}

function runProgramAsyncInput(program, userInput = [], output = [], pointer = 0) {
    const programCopy = program.slice(0);

    const {exitCode, pointer: lastPointer} = intcodeComputer(programCopy, userInput, output, pointer);

    if (exitCode === USER_INPUT_INTERRUPT) return (nextUserInput = [], nextOutput = []) => runProgramAsyncInput(programCopy, nextUserInput, nextOutput, lastPointer);

    return exitCode;
}

function runProgram(program, noun, verb, userInput = [], ouput = []) {
    const programCopy = program.slice(0);
    noun != undefined && (programCopy[1] = noun);
    verb != undefined && (programCopy[2] = verb);

    const {exitCode} = intcodeComputer(programCopy, userInput, ouput);

    if (exitCode != OK) throw new Error(`RunProgram failed! Cause: ${exitCode}`);

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

module.exports = {intcodeComputer, runProgram, readProgramFromFile, runProgramAsyncInput, exitCodes: {OK, USER_INPUT_INTERRUPT, UNKNOWN_INSTRUCTION}};
