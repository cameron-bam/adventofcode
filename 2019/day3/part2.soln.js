const readInput = require("../../common/readInput");
const {findIntersections, findDistanceToIntersection} = require("./plot");
const parsePaths = require("./parsePaths");

function findShortestDistanceToIntersections(wirePaths) {
    const intersections = findIntersections(wirePaths);

    return intersections
        .map((intersection) => {
            return wirePaths
                .map((wirePath) => findDistanceToIntersection(wirePath, intersection))
                .reduce((prev, cur) => {
                    return prev + cur;
                }, 0)
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