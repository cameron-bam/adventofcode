const {runProgram, readProgramFromFile} = require("../lib/intcodeComputer");

function solve() {
   return readProgramFromFile(__dirname + "/../input/day2/input")
    .then((program) => {
        console.log(`Day 2, part 1: Result of program is ${runProgram(program, 12, 2)}`);
    })
}

module.exports = {solve}