const {runProgramAsyncInput, readProgramFromFile, exitCodes} = require('../lib/intcodeComputer');

function findThrusterSignalFeedback(program, ampSettings) {
    let index = 0;
    const ampThreads = {}

    while (true) {
        let output = [];
        let lastThread = ampThreads[(index + ampSettings.length - 1) % ampSettings.length];
        let lastOutput = lastThread && lastThread.output || [0];

        if (ampThreads[index] && ampThreads[index].executable === undefined) return lastThread.output[0];

        let executable = !!ampThreads[index] && ampThreads[index].executable || ((userInput, output) => runProgramAsyncInput(program, [ampSettings[index], ...userInput], output));

        const exitCode = executable(lastOutput, output);

        if (typeof exitCode === "function") {
            ampThreads[index] = {
                executable: exitCode,
                output
            }
            index = (index + 1) % ampSettings.length;
            continue;
        }

        if (exitCode === exitCodes.OK) {
            ampThreads[index] = { output };
            index = (index + 1) % ampSettings.length;
            continue;
        }

        throw new Error(`Program exited with code: ${exitCode}!`);
    }
}

function solve() {
    return Promise.resolve(true);
}

module.exports = {solve, findThrusterSignalFeedback};