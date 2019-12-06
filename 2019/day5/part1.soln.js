const {intcodeComputer, readProgramFromFile} = require("../lib/intcodeComputer");

function solve() {
    return readProgramFromFile(__dirname + "/../input/day5/input")
    .then((program) => {
        const output = [];
        intcodeComputer(program, [1], output);
        console.log(`Day 5, part 1: Diagnostic tests for the intcode computer: ${output}`);
    });
}

module.exports = {solve};