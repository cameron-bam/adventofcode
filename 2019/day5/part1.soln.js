const {intcodeComputer, readProgramFromFile} = require("../lib/intcodeComputer");

function solve() {
    return readProgramFromFile(__dirname + "/../input/day5/input")
    .then((program) => {
        console.log("Day 5, part 1: Diagnostic tests for the intcode computer");
        intcodeComputer(program, [1]);
    });
}

module.exports = {solve};