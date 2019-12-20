const {runProgram, readProgramFromFile} = require('../lib/intcodeComputer');

function findThrusterSignal(program, ampSettings) {
    if (!Array.isArray(ampSettings)) {
        throw new Error("Must provide an array of amp settings!");
    }

    return ampSettings.reduce((prevOutput, ampSetting) => {
        const output = [];
        runProgram(program, undefined, undefined, [ampSetting, prevOutput], output);
        return output[0];
    }, 0);
}

function getAmpSettings(integer, numAmps) {
    if (typeof integer != 'number') throw new Error('Amp settings must be an integer!');
    let base5 = integer.toString(5);
    const startingLength = base5.length

    for (let i = 0; i < numAmps - startingLength; i += 1) {
        base5 = `0${base5}`;
    }

    return base5.split("").map((setting) => parseInt(setting));
}

function findMaxAmpSettings(program, numAmps) {
    let maxThrust, maxSettings;

    for (let i = 0; i < Math.pow(5, numAmps); i += 1) {
        const ampSettings = getAmpSettings(i, numAmps);
        const thrusterValue = findThrusterSignal(program, ampSettings);
        if (thrusterValue > maxThrust || !maxSettings) {
            maxSettings = ampSettings;
            maxThrust = thrusterValue;
        }
    }

    return {maxSettings, maxThrust};
}

function solve() {
    return readProgramFromFile(__dirname + "/../input/day7/input")
    .then((program) => {
        const output = [];
        runProgram(program, undefined, undefined, [2], output);
    });
}

module.exports = {solve, findThrusterSignal, findMaxAmpSettings};