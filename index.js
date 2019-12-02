const glob = require('glob');
const {readInput} = require('./2019/day1/lib');

glob("2019/**/*.soln.js", (err, files) => {
    console.log("ADC 2019 solutions:");
    files
     .map((file) => {
         file = file.substring(0, file.length - 3);
         return `./${file}`;
     })
     .map(require)
     .reduce((prev, cur) => !!prev ? prev.then(() => cur.solve()) : cur.solve(), undefined)
     .then(() => readInput(__dirname + "/resources/santa_ascii.txt"))
     .then((santa) => console.log("\n\n\n" + santa));
});