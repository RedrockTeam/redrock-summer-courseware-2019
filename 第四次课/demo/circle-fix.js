const fs = require('fs');
(function circle(i) {
    if (i === 5) {
        return
    }
    fs.stat(`./files/file${i}.html`, (err, data) => {
        if (err) throw err;
        console.log(i, data.ctime);
        circle(i + 1);
    });
})(1);
console.log('a');
