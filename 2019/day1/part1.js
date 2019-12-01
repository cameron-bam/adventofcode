const {readInput, computeTotalMass, logError, logResult} = require("./lib");

function getFuelFromModuleMass(mass) {
    return Math.floor(mass / 3) - 2;
}

function computeTotalMassPart1(massList) {
    return computeTotalMass(massList, getFuelFromModuleMass)
}

(function() {
    readInput("../input/day1/input")
    .then(computeTotalMassPart1)
    .then(logResult)
    .catch(logError)
})();