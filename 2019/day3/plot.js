const Coord = require("./coord");

function parsePathSegment(pathSegment) {
    const direction = pathSegment[0];
    const distance = parseInt(pathSegment.substring(1));
    
    return {direction, distance};
}

function plotPath(path, map, startingPoint, intersections, wireId, previousDistance) {
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

        const coordInfo = map[curCoord.toString()] || {}; 

        if (!coordInfo[wireId]) {
            if (Object.getOwnPropertyNames(coordInfo).length > 0) intersections.push(curCoord);

            coordInfo[wireId] = {
                wireLengths: []
            }
        }
        
        coordInfo[wireId].wireLengths.push(previousDistance + i + 1);

        map[curCoord.toString()] = coordInfo;
    }

    return curCoord.toString();
}

function plotWire(wirePath, map, intersections, wireId) {
    let lastPoint = "0,0";
    let totalDistance = 0;

    wirePath.forEach((pathSegment) => {
        lastPoint = plotPath(pathSegment, map, lastPoint, intersections, wireId, totalDistance);
        totalDistance += parsePathSegment(pathSegment).distance;
    });
}

function findIntersections(wirePaths) {
    const map = {};
    const intersections = [];

    wirePaths.forEach((wirePath, index) => plotWire(wirePath, map, intersections, index + 1));

    return {intersections, map};
}

module.exports = {plotWire, findIntersections};