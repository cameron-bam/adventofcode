const readInput = require("../common/readInput");
const {findIntersections} = require("./plot");
const parsePaths = require("./parsePaths");

function findShortestDistanceToIntersections(wirePaths) {
    const {intersections, map} = findIntersections(wirePaths);

    return intersections
        .map((intersection) => map[intersection.toString()])
        .map((coordInfo) => {
            return Object
                .getOwnPropertyNames(coordInfo)
                .reduce((prev, next) => {
                    return prev + coordInfo[next].wireLengths.reduce((prev, next) => Math.min(prev, next), Infinity);
                }, 0);
        })
        .reduce((prev, cur) => Math.min(prev, cur), Infinity);
}

function solve() {
    return readInput(__dirname + "/../input/day3/input")
    .then(parsePaths)
    .then((wirePaths) => {
        console.log(`Day 3, part 2: Shortest wire path distance to intersection point is ${findShortestDistanceToIntersections(wirePaths)}`);
    })
}

module.exports = {findShortestDistanceToIntersections, solve};