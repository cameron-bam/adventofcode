const Coord = require("./coord");

function parsePathSegment(pathSegment) {
    const direction = pathSegment[0];
    const distance = parseInt(pathSegment.substring(1));
    
    return {direction, distance};
}

function plotPath(path, map, startingPoint, intersections, wireId, targetIntersection) {
    const {direction, distance} = parsePathSegment(path);
    let curCoord = Coord.fromString(startingPoint);

    if (Number.isNaN(distance)) throw new Error(`Couldn't parse path ${path}: distance was ${distance}`);

    for (let i = 0; i < distance; i += 1) {
        switch (direction) {
            case "R":
                curCoord = new Coord(curCoord.getX() + 1, curCoord.getY());
                break;
            case "L": 
                curCoord = new Coord(curCoord.getX() - 1, curCoord.getY());
                break;
            case "U": 
                curCoord = new Coord(curCoord.getX(), curCoord.getY() + 1);
                break;
            case "D":
                curCoord = new Coord(curCoord.getX(), curCoord.getY() - 1);
                break;
            default:
                throw new Error(`Unrecognized direction ${direction}!`)
        }

        const crossedWireIds = map[curCoord.toString()] || []

        if (crossedWireIds.length > 0 && crossedWireIds.indexOf(wireId) == -1) intersections.push(curCoord);
        
        crossedWireIds.push(wireId);

        map[curCoord.toString()] = crossedWireIds;

        if (curCoord.toString() == targetIntersection) return curCoord.toString();
    }

    return curCoord.toString();
}

function plotWire(wirePath, map, intersections, wireId) {
    let lastPoint = "0,0";

    wirePath.forEach((pathSegment) => {
        lastPoint = plotPath(pathSegment, map, lastPoint, intersections, wireId);
    });
}

function findDistanceToIntersection(wirePath, targetIntersection) {
    const map = {};
    const intersections = [];
    const wireId = 1;
    let lastPoint = "0,0";
    let intersectionFound = false;

    for (let i = 0; i < wirePath.length; i += 1) {
        lastPoint = plotPath(wirePath[i], map, lastPoint, intersections, wireId, targetIntersection);

        if (lastPoint == targetIntersection) {
            intersectionFound = true;
            break;
        }
    }

    if (!intersectionFound) throw new Error("Could not find intersection on wire path!");

    return Object.getOwnPropertyNames(map).length;
}

function findIntersections(wirePaths) {
    const map = {};
    const intersections = [];

    wirePaths.forEach((wirePath, index) => plotWire(wirePath, map, intersections, index + 1));

    return intersections;
}

module.exports = {plotWire, findIntersections, findDistanceToIntersection};