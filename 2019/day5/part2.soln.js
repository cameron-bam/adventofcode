const {readProgramFromFile, runProgram} = require("../lib/intcodeComputer");

function solve() {
    return readProgramFromFile(__dirname + "/../input/day5/input")
    .then((program) => {
        const input = [5];
        const output = [];
        runProgram(program, undefined, undefined, input, output);
        console.log(`Day 5, part 2: Output of intcode computer is [${output}]`);
    })
}

module.exports = {solve};