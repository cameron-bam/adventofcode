const {buildOrbits, countOrbits} = require("./part1.soln");
const { findOrbitalJumps } = require("./part2.soln");
const assert = require("assert");

describe("Day 6 part 1", () => {
    it("Should find 42 orbits", () => {
        const orbitMap = `COM)B
        B)C
        C)D
        D)E
        E)F
        B)G
        G)H
        D)I
        E)J
        J)K
        K)L`;

        const orbitTree = buildOrbits(orbitMap);
        const orbitCount = countOrbits(orbitTree);

        assert.equal(orbitCount, 42, "Expected to find 42 orbits from the map!");
    });

    it("Should find 4 jumps between YOU and SAN", () => {
        const orbitMap = `COM)B
        B)C
        C)D
        D)E
        E)F
        B)G
        G)H
        D)I
        E)J
        J)K
        K)L
        K)YOU
        I)SAN`;
        
        const orbitTree = buildOrbits(orbitMap);
        const jumpCount = findOrbitalJumps(orbitTree, "YOU", "SAN");

        assert.equal(jumpCount, 4, "Expected to find 4 jumps between YOU and SAN!");
    });
});