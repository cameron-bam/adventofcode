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

function permut(string) {
    if (string.length < 2) return string;
  
    let permutations = [];
    for (let i = 0; i < string.length; i++) {
        const char = string[i];
  
        // skip duplicate characters in the string.
        if (string.indexOf(char) != i) continue;
  
        const remainingString = string.slice(0, i) + string.slice(i + 1, string.length);
  
        for (let subPermutation of permut(remainingString)) permutations.push(char + subPermutation)
    }
    return permutations;
}

function findMaxAmpSettings(program) {
    let maxThrust, maxSettings;

    for (let ampSettings of permut("43210")) {
        ampSettings = ampSettings.split("").map((setting) => parseInt(setting));
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
        const {maxSettings, maxThrust} = findMaxAmpSettings(program);
        console.log(`Day 7, part 1: The maximum thrust value is ${maxThrust}, which is provided by the amp settings [${maxSettings}]`);
    });
}

module.exports = {solve, findThrusterSignal, findMaxAmpSettings, permut};