const fs = require('fs')
for(let i = 1; i < 5; i++) {
    fs.stat(`./files/file${i}.html`, (err, data) => {
        if (err) throw err;
        console.log(i, data.ctime);
    });
}

