const assert = require('assert');
const {findThrusterSignal, findMaxAmpSettings} = require('./part1.soln');
const {findThrusterSignalFeedback} = require("./part2.soln");

const testCasesPart1 = [
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
    },
];

const testCasesPart2 = [
    {
        maxSequence: [9,8,7,6,5],
        maxValue: 139629729,
        program: [
            3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,
            27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5
        ]
    }, {
        maxValue: 18216,
        maxSequence: [9,7,8,5,6],
        program: [
            3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
            -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
            53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10
        ]
    }
]

describe("Day 7, part 1: Maximize thruster signal with amplifier settings", () => {
    testCasesPart1.forEach(({program, maxSequence, maxValue}, i) => {
        it(`Should find the thruster value ${maxValue} for [${maxSequence}]`, () => {
            const output = findThrusterSignal(program, maxSequence);
            assert.equal(output, maxValue, `Expected return value to be ${maxValue}!`);
        });

        it(`Should find the max amp setting is [${maxSequence}]`, () => {
            const {maxSettings, maxThrust} = findMaxAmpSettings(program);

            assert.equal(maxThrust, maxValue, `Expected maxThrust to be equal to ${maxValue}`);
            assert.deepEqual(maxSettings, maxSequence, `Expected output to be equal to [${maxSequence}]!`);
        });
    });
});

describe("Day 7, part 2: Maximize thruster signal with feedback", () => {
    testCasesPart2.forEach(({maxValue, maxSequence, program}) => {
        it(`Should find the thruster value ${maxValue} for [${maxSequence}]`, () => {
            const output = findThrusterSignalFeedback(program, maxSequence);
            assert.equal(output, maxValue, `Expected return value to be ${maxValue}!`);
        });
    });
});