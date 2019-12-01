const assert = require('assert');
const {computeFuelForModule} = require("./part1");
const {getFuelFromMassAndFuel} = require("./part2");

describe("Day 1, part 1", () => {
    it('should calculate fuel requirements for fuel mass 12', () =>{
        assert.strictEqual(computeFuelForModule(14), 2);
    });
    
    it('should calculate fuel requirements for module mass 1969', () => {
        assert.strictEqual(computeFuelForModule(1969), 654);
    });

    it('should calculate fuel requirements for module mass 100756', () => {
        assert.strictEqual(computeFuelForModule(100756), 33583);
    });
});

describe("Day 1, part 2", () => {
    it('should calculate fuel requirements for module mass 14', () => {
        assert.strictEqual(getFuelFromMassAndFuel(14), 2);
    });

    it('should calculate fuel requirements for module mass 1969', () => {
        assert.strictEqual(getFuelFromMassAndFuel(1969), 966);
    });

    it('should calculate fuel requirements for module mass 100756', () => {
        assert.strictEqual(getFuelFromMassAndFuel(100756), 50346);
    });
});