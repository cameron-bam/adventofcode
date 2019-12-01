const {readInput, computeTotalMass, logError, logResult} = require("./lib");

function getFuelFromMass(mass) {
    const fuel = Math.floor(mass / 3) - 2;;
    return fuel <= 0 ? 0 : (fuel + getFuelFromMass(fuel));
}

function computeTotalMassPart2(massList) {
    return computeTotalMass(massList, getFuelFromMass)
}

(function() {
    readInput("../input/day1/input")
    .then(computeTotalMassPart2)
    .then(logResult)
    .catch(logError)
})()