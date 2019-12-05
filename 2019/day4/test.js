const {validatePassword} = require("./part1.soln");
const assert = require("assert");

describe("Day 4 part 1: validate password", () => {
    it("Should say 111111 is valid", () => {
        assert.strictEqual(validatePassword("111111"), true);
    });

    it("Should say 223450 is invalid", () => {
        assert.strictEqual(validatePassword("223450"), false);
    });

    it("Should say 123789 is invalid", () => {
        assert.strictEqual(validatePassword("123789"), false);
    });
});