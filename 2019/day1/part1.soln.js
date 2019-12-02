const {processInput, computeTotalMass, logError} = require("./lib");
const readInput = require("../../common/readInput");

function computeFuelForModule(mass) {
    return Math.floor(mass / 3) - 2;
}

function solve() {
    return readInput(__dirname + "/../input/day1/input")
    .then(processInput)
    .then((massList) => computeTotalMass(massList, computeFuelForModule))
    .then((result) => console.log(`Day 1, Part 1 Total Fuel: ${result}`))
    .catch(logError);
}

module.exports = {
    computeFuelForModule, solve
};