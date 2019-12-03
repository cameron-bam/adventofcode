const readInput = require("../../common/readInput");
const {intcodeComputer, processInput} = require("./part1.soln");

function runProgram(program, noun, verb) {
    const programCopy = program.slice(0);
    programCopy[1] = noun;
    programCopy[2] = verb;
    intcodeComputer(programCopy);

    return programCopy[0]
}

function findNounVerbForValue(program, expectedValue) {
    let noun = 0, verb = 0;

    for (let noun = 0; noun < 100; noun += 1) {
        for (let verb = 0; verb <100; verb += 1) {
            if (runProgram(program, noun, verb) == expectedValue) {
                return {noun, verb};
            }
        }
    }

    throw new Exception(`Target value ${expectedValue} not found!`);
}

function nounVerbMath(noun, verb) {
    return (100 * noun) + verb;
}

function solve() {
    return readInput(__dirname + "/../input/day2/input")
    .then(processInput)
    .then((program) => {
        const {noun, verb} = findNounVerbForValue(program, 19690720);
        console.log(`Day 2, part 2: 100 * noun + verb = ${(100 * noun) + verb}`);
    });
}

module.exports = {solve, nounVerbMath, runProgram};