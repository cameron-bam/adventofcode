const fs = require("fs");
const path = require("path");

module.exports = (pathStr) => {
    return new Promise((resolve, reject) => {
        const loc = path.parse(pathStr);
        fs.readFile(`${loc.dir}/${loc.base}`, (err, data) => {
            if (err) reject(err);
            else resolve(data.toString());
        });
    });
}