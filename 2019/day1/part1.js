const {readInput, computeTotalMass, logError, logResult} = require("./lib");

function getFuelFromModuleMass(mass) {
    return Math.floor(mass / 3) - 2;
}

(function() {
    readInput("../input/day1/input")
    .then((massList) => computeTotalMass(massList, getFuelFromModuleMass))
    .then(logResult)
    .catch(logError)
})();