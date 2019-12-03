const {runProgram, readProgramFromFile} = require("./intcodeComputer");

function findNounVerbForValue(program, expectedValue) {
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
    return readProgramFromFile(__dirname + "/../input/day2/input")
    .then((program) => {
        const {noun, verb} = findNounVerbForValue(program, 19690720);
        console.log(`Day 2, part 2: 100 * noun + verb = ${(100 * noun) + verb}`);
    });
}

module.exports = {solve, nounVerbMath};