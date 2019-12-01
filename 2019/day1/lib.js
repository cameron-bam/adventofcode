const fs = require('fs');
const path = require('path');

function processInput(fileData) {
    return fileData
    .toString()
    .split("\n")
    .map((mass) => mass.trim())
    .filter((massStr) => massStr.length > 0)
    .map((massStr) => {
        const mass = parseInt(massStr);
        if (Number.isNaN(mass)) throw new Error(`Couldn't convert ${massStr} to a Number!`);
        return mass;
    });
}

exports.readInput = (relPath) => {
    return new Promise((resolve, reject) => {
        const loc = path.parse(__dirname + "/" + relPath);
        fs.readFile(`${loc.dir}/${loc.name}`, (err, data) => {
            if (err) reject(err);
            else resolve(processInput(data));
        });
    });
}

exports.computeTotalMass = (listOfMass, fuelComputeFunc) => {
    return listOfMass
    .map(fuelComputeFunc)
    .reduce((prev, cur) => {
        const next = prev + cur;
        if (Number.isNaN(next)) throw new Error(`Can't compute total fuel requirements! ${prev} + ${cur} resulted in ${next}`);
        return next;
    }, 0);
}

exports.logError = (err) => {
    console.error(`Error while computing fuel requirements: ${err.message}`);
}