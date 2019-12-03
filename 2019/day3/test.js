const {findShortestDistance, processInput} = require("./part1.soln");
const assert = require("assert");

describe("Day 3, part 1 - Shortest distance to intersection", () => {
    it("Should find that the shortest distance is 159 for the given inputs", () => {
        const input = `R75,D30,R83,U83,L12,D49,R71,U7,L72
        U62,R66,U55,R34,D71,R55,D58,R83`;

        assert.strictEqual(findShortestDistance(processInput(input)), 159);
    });

    it("Should find that the shortest distance is 135 for the given inputs", () => {
        const input = `R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
        U98,R91,D20,R16,D67,R40,U7,R15,U6,R7`;

        assert.strictEqual(findShortestDistance(processInput(input)), 135);
    });
});