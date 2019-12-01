const {readInput, computeTotalMass, logError} = require("./lib");

function getFuelFromMassAndFuel(mass) {
    const fuel = Math.floor(mass / 3) - 2;;
    return fuel <= 0 ? 0 : (fuel + getFuelFromMassAndFuel(fuel));
}

function solve() {
    return readInput("../input/day1/input")
    .then((massList) => computeTotalMass(massList, getFuelFromMassAndFuel))
    .then((result) => console.log(`Day 1, Part 2 Total Fuel: ${result}`))
    .catch(logError);
}

module.exports = {getFuelFromMassAndFuel, solve};