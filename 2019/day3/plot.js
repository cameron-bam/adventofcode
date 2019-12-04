const Coord = require("./coord");

function plotPath(path, map, startingPoint, intersections, wireId) {
    const direction = path[0];
    const distance = parseInt(path.substring(1));
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

        const crossedWireId = map[curCoord.toString()]

        if (!!crossedWireId && wireId != crossedWireId) intersections.push(curCoord);
        
        map[curCoord.toString()] = wireId;
    }

    return curCoord.toString();
}

function plotWire(wirePath, map, intersections, wireId) {
    let lastPoint = "0,0";

    wirePath.forEach((pathSegment) => {
        lastPoint = plotPath(pathSegment, map, lastPoint, intersections, wireId);
    });
}

function findIntersections(wirePaths) {
    const map = {};
    const intersections = [];

    wirePaths.forEach((wirePath, index) => plotWire(wirePath, map, intersections, index + 1));

    return intersections;
}

module.exports = {plotWire, findIntersections};