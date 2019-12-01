var glob = require('glob');

glob("2019/**/*.soln.js", (err, files) => {
    console.log("ADC 2019 solutions:");
    files
     .map((file) => {
         file = file.substring(0, file.length - 3);
         return `./${file}`;
     })
     .map(require)
     .reduce((prev, cur) => {
         return !!prev ? prev.then(() => cur.solve()) : cur.solve();
     }, undefined);
});