const {runProgramAsyncInput, readProgramFromFile, exitCodes} = require('../lib/intcodeComputer');
const {permut} = require('./part1.soln');

function findThrusterSignalFeedback(program, ampSettings) {
    let index = 0;
    const ampThreads = {}

    while (true) {
        let outputSignal = [];
        let lastThread = ampThreads[(index + ampSettings.length - 1) % ampSettings.length];
        let inputSignal = lastThread && lastThread.output || [0];

        // every amp thread has terminated, return the output of the last one
        if (ampThreads[index] && ampThreads[index].executable === undefined) return lastThread.output[0];

        // either start the amp program or resume execution
        let executable = !!ampThreads[index] && ampThreads[index].executable || ((userInput, output) => runProgramAsyncInput(program, [ampSettings[index], ...userInput], output));
        const exitCode = executable(inputSignal, outputSignal);

        // store the function handle in an object so we can resume it later
        if (typeof exitCode === "function") {
            ampThreads[index] = {
                executable: exitCode,
                output: outputSignal
            }
            index = (index + 1) % ampSettings.length;
            continue;
        }

        if (exitCode === exitCodes.OK) {
            ampThreads[index] = { output: outputSignal };
            index = (index + 1) % ampSettings.length;
            continue;
        }

        throw new Error(`Program exited with code: ${exitCode}!`);
    }
}

function findMaxAmpSettingsFeedback(program) {
    let maxThrust, maxSettings;

    for (let ampSettings of permut("98765")) {
        ampSettings = ampSettings.split("").map((setting) => parseInt(setting));
        const thrusterValue = findThrusterSignalFeedback(program, ampSettings);
        if (thrusterValue > maxThrust || !maxSettings) {
            maxSettings = ampSettings;
            maxThrust = thrusterValue;
        }
    }

    return {maxSettings, maxThrust};
}

function solve() {
    return Promise.resolve(true);
}

module.exports = {solve, findThrusterSignalFeedback, findMaxAmpSettingsFeedback};