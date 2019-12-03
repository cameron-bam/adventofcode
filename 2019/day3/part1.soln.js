class Coord {
    static fromString(string) {
        const xy = string.split(",");
        if (xy.length != 2) throw new Error(`Couldn't convert string '${string}' to Coords!`);
        return new Coord(xy[0], xy[1]);
    }

    constructor(x, y) {
        this.x = parseInt(x);
        this.y = parseInt(y);

        if (Number.isNaN(this.x) || Number.isNaN(this.y)) throw new Error(`Invalid values supplied! Coords cannot be NaN. Provided value:  ${this.toString()}`);
    }

    getX() {
        return this.x;
    }

    getY() {
        return this.y;
    }

    getManhattanDistance() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    toString() {
        return `${this.x},${this.y}`;
    }
}

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

    wirePath.forEach((pathSegment, index) => {
        lastPoint = plotPath(pathSegment, map, lastPoint, intersections, wireId);
    });
}

function findShortestDistance(wirePaths) {
    const map = {};
    const intersections = [];

    wirePaths.forEach((wirePath, index) => plotWire(wirePath, map, intersections, index + 1));

    return intersections.reduce((prevValue, curValue) => {
        if (curValue.getManhattanDistance() < prevValue) {
            return curValue.getManhattanDistance();
        }

        return prevValue;
    }, Infinity);
}

function processInput(string) {
    return string
    .split("\n")
    .map((strPath) => strPath.split(",").map((pathSeg) => pathSeg.trim()));
}

function solve() {
    return Promise.resolve();
}

module.exports = {findShortestDistance, processInput};