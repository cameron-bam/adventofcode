module.exports = function processInput(string) {
    return string
    .split("\n")
    .map((strPath) => strPath
        .split(",")
        .map((pathSeg) => pathSeg.trim())
        .filter((pathSeg) => pathSeg.length > 0))
    .filter(wirePath => wirePath.length > 0);
} 