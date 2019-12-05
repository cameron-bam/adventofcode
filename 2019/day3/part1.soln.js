const readInput = require("../../common/readInput");
const {findIntersections} = require("./plot");
const parsePaths = require("./parsePaths");

function findShortestDistance(wirePaths) {
    return findIntersections(wirePaths)
    .intersections
    .reduce((prevValue, curValue) => {
        if (curValue.getManhattanDistance() < prevValue) {
            return curValue.getManhattanDistance();
        }

        return prevValue;
    }, Infinity);
}

function solve() {
    return readInput(__dirname + "/../input/day3/input")
    .then(parsePaths)
    .then((wirePaths) => {
        console.log(`Day 3, part 1: Shortest distance to intersection point is ${findShortestDistance(wirePaths)}`);
    })
}

module.exports = {findShortestDistance, solve};