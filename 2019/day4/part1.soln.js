function validatePassword(password) {
    const digits = 
    ("" + password)
    .split("")
    .map((char) => parseInt(char));

    let alwaysIncreases = true;
    let hasDoubleDigits = false;

    for (i = 1; i < digits.length; i += 1) {
        const prev = digits[i-1];
        const next = digits[i];

        if (prev === next) {
            hasDoubleDigits = true;
        }

        if (prev > next) {
            return false;
        }
    }

    return alwaysIncreases && hasDoubleDigits;
}

function findAllPossiblePasswords(min, max) {
    let matches = 0;

    for (let i = 0; i <= max; i += 1) {
        const password = min + i;
        const isvalid = validatePassword(password);
        if (isvalid) {
            matches += 1;
        }
    }

    return matches;
}

function solve() {
    return Promise
    .resolve()
    .then(() => {
        console.log(`Day 4, part 1: The total number of possible passwords in range is ${findAllPossiblePasswords(271973, 785961)}`)
    });
}

module.exports = {solve, validatePassword};