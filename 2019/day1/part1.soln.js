const {readInput, computeTotalMass, logError} = require("./lib");

function computeFuelForModule(mass) {
    return Math.floor(mass / 3) - 2;
}

function solve() {
    return readInput("../input/day1/input")
    .then((massList) => computeTotalMass(massList, computeFuelForModule))
    .then((result) => console.log(`Day 1, Part 1 Total Fuel: ${result}`))
    .catch(logError);
}

module.exports = {
    computeFuelForModule, solve
};