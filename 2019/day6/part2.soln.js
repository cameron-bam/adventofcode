const {buildOrbits, findObject} = require("./part1.soln");
const readInput = require("../common/readInput");

function markPath(object, jumps = 0) {
    if (!!object.jumps) return jumps + object.jumps;    
    
    object.jumps = jumps;

    if (object.name === "COM") return;

    return markPath(object.parent, jumps + 1);
}

function findOrbitalJumps(orbitTree, node1, node2) {
    const object1 = findObject((object) => object.name === node1, orbitTree);
    const object2 = findObject((object) => object.name === node2, orbitTree);

    markPath(object1.parent);
    return markPath(object2.parent);
}

function solve() {
    return readInput(__dirname + "/../input/day6/input")
    .then(buildOrbits)
    .then((orbitTree) => {
        console.log(`The number of jumps between YOU and SAN is ${findOrbitalJumps(orbitTree, "YOU", "SAN")}`);
    })
}

module.exports = { findOrbitalJumps, solve };