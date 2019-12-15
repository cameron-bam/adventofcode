function findObject(condition, tree) {
    for (let i = 0; i < tree.orbits.length; i += 1) {
        if (condition(tree.orbits[i])) return tree.orbits[i];
        const node = findObject(condition, tree.orbits[i]);
        if (!!node) return node;
    }
}

function addOrbit(targetNode, newNode, tree) {
    const object = findObject((orbit) => orbit.name === targetNode, tree);

    if (!object) {
        throw new Error(`Could not find targetNode ${targetNode}!`);
    }

    const newObject = {};
    newObject.parent = object;
    newObject.depth = object.depth + 1;
    newObject.orbits = [];
    newObject.name = newNode;

    object.orbits.push(newObject);
}

function calculateDirectAndIndirectOrbits(object) {
    object.counted = true;

    if (object.depth === 1) return 1;

    if (object.parent.counted) return object.depth;

    return object.depth + calculateDirectAndIndirectOrbits(object.parent);
}

function countOrbits(tree, orbitCount = 0) {
    for (let i = 0; i < tree.orbits.length; i += 1) {
        const object = tree.orbits[i];
        if (object.orbits.length === 0) {
            orbitCount += calculateDirectAndIndirectOrbits(object);
        }
        else {
            orbitCount = countOrbits(object, orbitCount);
        }
    }

    return orbitCount;
}

function buildTree(orbits) {
    const COM = {
        name: "COM",
        orbits: [],
        depth: 0
    };

    COM.parent = COM;

    const ROOT = {orbits: [COM]};

    orbits.map(({targetNode, newNode}) => {
        addOrbit(targetNode, newNode, ROOT);
    });

    return ROOT;
}

function buildOrbits(orbitMap) {
    const orbits = orbitMap
    .split("\n")
    .map((orbitString) => orbitString.trim())
    .map((orbitString) => {
        const nodes = orbitString.split(")");

        if (nodes.length != 2) return false;

        return {
            targetNode: nodes[0],
            newNode: nodes[1]
        };
    })
    .filter((node) => !!node);

    return buildTree(orbits);
}

function solve() {
    return Promise.resolve(true);
}

module.exports = { solve, buildOrbits, countOrbits };