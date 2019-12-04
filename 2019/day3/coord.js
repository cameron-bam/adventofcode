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

module.exports = Coord;