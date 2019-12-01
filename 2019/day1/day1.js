const fs = require('fs');

function readInput() {
    return new Promise((resolve, reject) => {
        fs.readFile("../input/day1_part1_input", (err, data) => {
            if (err) reject(err);
            else resolve(data);
        });
    });
}

function processInput(fileData) {
    return fileData
    .toString()
    .split("\n")
    .filter((massStr) => massStr.length > 0)
    .map((massStr) => {
        const mass = parseInt(massStr.trim());
        if (Number.isNaN(mass)) throw new Error(`Couldn't convert ${massStr} to a Number!`);
        return mass;
    });
}

function getFuelFromModuleMass(mass) {
    return Math.floor(mass / 3) - 2;
}

function getFuelFromFuelMass(fuelMass) {
    const fuel = getFuelFromModuleMass(fuelMass);

    if (fuel <= 0) return 0;

    return fuel + getFuelFromFuelMass(fuel);
}

function getFuelFromModuleMassAndFuelMass(mass) {
    const fuel = getFuelFromModuleMass(mass);
    return fuel + getFuelFromFuelMass(fuel);
}

function computeTotalMass(listOfMass) {
    console.log(`Computing the fuel requirements of ${listOfMass.length} modules...\n`)

    return listOfMass
    .map(getFuelFromModuleMassAndFuelMass)
    .reduce((prev, cur) => {
        const next = prev + cur;
        if (Number.isNaN(next)) throw new Error(`Can't compute total fuel requirements! ${prev} + ${cur} resulted in ${next}`);
        return next;
    }, 0);
}

function logResult(totalFuel) {
    console.log(`Fuel requirements computed! Total fuel: ${totalFuel}\n`)
}

function logError(err) {
    console.error(`Error while computing fuel requirements: ${err.message}`);
}

function main() {
    readInput()
    .then(processInput)
    .then(computeTotalMass)
    .then(logResult)
    .catch(logError)
}

main();