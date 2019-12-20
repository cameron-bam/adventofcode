const assert = require('assert');
const {findThrusterSignal, findMaxAmpSettings} = require('./part1.soln');

const testCases = [
    {
        program: [3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0],
        maxSequence: [4,3,2,1,0],
        maxValue: 43210
    }, {
        program: [3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0],
        maxSequence: [0,1,2,3,4],
        maxValue: 54321
    }, {
        program: [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0],
        maxSequence: [1,0,4,3,2],
        maxValue: 65210
    }
];

describe("Day 7, part 1: Maximize thruster signal with amplifier settings", () => {
    testCases.forEach(({program, maxSequence, maxValue}, i) => {
        it(`Should find the thruster value ${maxValue} for [${maxSequence}]`, () => {
            const output = findThrusterSignal(program, maxSequence);
            assert.equal(output, maxValue, `Expected return value to be ${maxValue}!`);
        });

        it(`Should find the max amp setting is [${maxSequence}]`, () => {
            const {maxSettings, maxThrust} = findMaxAmpSettings(program);

            assert.equal(maxThrust, maxValue, `Expected maxThrust to be equal to ${maxValue}`)
            assert.deepEqual(maxSettings, maxSequence, `Expected output to be equal to [${maxSequence}]!`);
        })
    })
});