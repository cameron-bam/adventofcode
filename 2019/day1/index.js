const part1 = require('./part1');
const part2 = require('./part2');

exports.solve = async function() {
    console.log("Day 1 Solutions");
    await part1.solve();
    await part2.solve();
}