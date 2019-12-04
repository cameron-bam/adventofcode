const {findShortestDistance} = require("./part1.soln");
const {findShortestDistanceToIntersections} = require("./part2.soln");
const parsePaths = require("./parsePaths");
const assert = require("assert");

const input1 = `R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83`
const input2 = `R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7`;

describe("Day 3, part 1 - Shortest distance to intersection", () => {
    it(`Should find that the shortest distance is 159 for the input ${input1}`, () => {
        assert.strictEqual(findShortestDistance(parsePaths(input1)), 159);
    });

    it(`Should find that the shortest distance is 135 for the input ${input2}`, () => {
        assert.strictEqual(findShortestDistance(parsePaths(input2)), 135);
    });
});

describe("Day 3, part 2 - shortest wire distance to instersection", () => {
    it(`Should find that the shortest distance is 610 for input ${input1}`, () => {
        assert.strictEqual(findShortestDistanceToIntersections(parsePaths(input1)), 610);
    });

    it(`Should find that the shortest distance is 410 for input ${input2}`, () => {
        assert.strictEqual(findShortestDistanceToIntersections(parsePaths(input2)), 410);
    });
})